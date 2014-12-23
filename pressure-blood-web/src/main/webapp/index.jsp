<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String username = request.getRemoteUser();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pressure Blood App</title>

<link rel="stylesheet" type="text/css" href="styles/jquery/jquery.dataTables.min.css">
<link rel="stylesheet" type="text/css" href="styles/jquery/jquery.datetimepicker.css">

<link rel="stylesheet" type="text/css" href="styles/pb/index.css">

</head>
<body>
	<div id="user-info">
		<nav>
			<span><%= username %></span> |
			<a href="/pressure-blood-web/o.logout">logout</a>
		</nav>
	</div>
	<div id="main">
		<div id="measures">
			<table id="measures-table" class="display" border="1">
				<thead>
					<tr>
						<th class="measures-header" colspan="6"/>
					</tr>
					<tr>
						<th class="sbp-column">SBP</th>
						<th class="dbp-column">DBP</th>
						<th class="hand-column">HAND</th>
						<th class="pulse-column">PULSE</th>
						<th class="datetime-column">DATE AND TIME</th>
						<th class="delete-column">DELETE</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

		<div id="add-measure">
			<h2>Add measure</h2>
			<form id="add-measure-form" action="">
				<div id="sbp-measure">
					<label for="sbp">SBP*:</label>
					<input id="sbp" name="sbp" type="number" min="0" max="300" size="3" class="required">
				</div>

				<div id="dbp-measure">
					<label for="dbp">DBP*:</label>
					<input id="dbp" name="dbp" type="number" min="0" max="300" size="3" class="required">
				</div>

				<div id="hand-selector">
					<label for="hand">HAND:</label>
					<select id="hand" name="hand">
						<option value="LEFT_HAND">LEFT_HAND</option>
						<option value="RIGHT_HAND">RIGHT_HAND</option>
					</select>
				</div>

				<div id="pulse-measure">
					<label for="pulse">PULSE:</label>
					<input id="pulse" name="pulse" type="number" min="0" max="300" size="3">
				</div>
				
				<div id="datetime">
					<label for="datetimepicker">DATE AND TIME*:</label>
					<input id="datetimepicker" name="datetimepicker" type="text" size="12" class="required">
				</div>

				<div id="add-measure-button">
					<button type="submit">Add measure</button>
				</div>
			</form>
		</div>
		<br />
		<div id="statistics">
			<h2>Statistics</h2>
			<div id="column-chart"></div>
		</div>
	</div>
</body>

<script data-main="scripts/main" src="scripts/require/require.js"></script>
<script>

	require({
		// Make sure it is enough to load all scripts
		waitSeconds : 10,
		paths : {
			async : "require/async/async",
			goog : "require/goog/goog",
			propertyParser : "require/propertyParser/propertyParser"
		}
	});

	require(["goog!visualization,1,packages:[corechart]"], function() {
		$(function() {

			// Hide Statistics header

			Statistics.hideStatisticsHeader();

			// Create dictionary for holding all masures

			var dictionary = new Dictionary();

			// Measures table init

			var dataTables = $("#measures-table").dataTable({
				"order" : [[ 4, "asc" ]],
				"aoColumnDefs" : [
					{"aTargets" : [5],// DELETE column
					"bSortable" : false
					},
					{"aTargets" : [ "datetime-column" ],
					"mRender" : function(dateTimeInMillis) {
									var localDateTimeString = LocalDateTime.toLocalDateTimeString(dateTimeInMillis);
									return localDateTimeString;
								},
					"sType" : "date-bg"
					}
				],
				"fnHeaderCallback" : function(nHead, aData, iStart, iEnd, aiDisplay) {
					nHead.getElementsByTagName("th")[0].innerHTML = (iEnd - iStart) + " Measures";
				},
				"fnDrawCallback": function (oSettings) {
					$("#measures-table tbody tr td:not(.delete)").editable("/pressure-blood-web/o.updateMeasure", {
						"placeholder" : "Value must be less than or equal to 300",
						"type" : "measure",
						"onblur" : "submit",
						"callback" : function(updatedValue) {

							function convertUpdatedValueToColumnType(column) {
								switch (column) {
								case 0: // SBP column
								case 1: // DBP column
								case 3: // PULSE column
									return updatedValue = parseInt(updatedValue, 10);
									break;
								case 4: // DATETIME column
									return updatedValue = LocalDateTime.parse(updatedValue);
									break;
								default: // HAND column
									return updatedValue;
									break;
								}
							}

							var td = this;

							function updateMeasuresTable() {
								var position = dataTables.fnGetPosition(td),
									row = position[0],
									column = position[1],
									aData = dataTables.fnGetData(row);

								aData[column] = convertUpdatedValueToColumnType(column);
								dataTables.fnUpdate(aData, row);
							}

							function updateDictionary() {
								var id = td.getAttribute("id"),
									tokens = id.split("_"),
									measureProperty = tokens[0],
									measureId = tokens[1];

								dictionary.update(measureId, measureProperty, updatedValue);
							}

							function updateStatistics() {

								function calculateBeginIndex() {
									var beginIndex = 0;
									return beginIndex;
								}

								function calculateEndIndex() {
									var endIndex = 10;
									if (endIndex > dictionary.count()) {
										endIndex = dictionary.count();
									}
									return endIndex;
								}

								var beginIndex = calculateBeginIndex(),
									endIndex = calculateEndIndex(),
									chartData = dictionary.toChartData().splice(beginIndex, endIndex);

								Statistics.drawChart(chartData);
							}

							// Measures table

							updateMeasuresTable();

							// Update dictionary

							updateDictionary();

							// Statistics

							updateStatistics();
						}
					});
				},
				"fnRowCallback" : function(nRow, aData, iStart, iEnd, aiDisplay) {
					var id = $("td a", nRow).attr("id");
					var tdElements = $("td", nRow);
					var index;
					for(index = 0; index < tdElements.length; index++) {
						var tdElement = tdElements[index];
						switch (index) {
						case 0:
							tdElement.setAttribute("id", "sbp_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 1:
							tdElement.setAttribute("id", "dbp_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 2:
							tdElement.setAttribute("id", "hand_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 3:
							tdElement.setAttribute("id", "pulse_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 4:
							tdElement.setAttribute("id", "datetime_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 5:
							tdElement.setAttribute("class", "delete");
						default:
							break;
						}
					}
				}
			});

			var measuresTable = new MeasuresTable(dataTables, dictionary, 0);
			measuresTable.populateMeasuresTable();

			dataTables.on("page.dt", function(event, oSettings) {

				function updatePageNumber() {
					var pageNumber = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength);
					measuresTable.setPageNumber(pageNumber);
					return pageNumber;
				}

				function updateStatistics(pageNumber) {

					function calculateBeginIndex() {
						var beginIndex = pageNumber * 10;
						return beginIndex;
					}

					function calculateEndIndex() {
						var endIndex = beginIndex + 10;
						if(endIndex > dictionary.count()) {
							endIndex = dictionary.count();
						}
						return endIndex;
					}

					var beginIndex = calculateBeginIndex(),
						endIndex = calculateEndIndex(),
						chartData = dictionary.toChartData().splice(beginIndex, endIndex);

					Statistics.showStatisticsHeader();
					Statistics.drawChart(chartData);
				}

				// Update measures table page number

				var pageNumber = updatePageNumber();

				// Statistics

				updateStatistics(pageNumber);

				event.preventDefault();
			});

			dataTables.delegate("tbody tr td a", "click", function(event) {
				if (confirm("Are you sure you want to delete this measure") == true) {
					var tableRow = $(this).parent().parent();
					measuresTable.deleteMeasure(tableRow);
				}

				event.preventDefault();
			});

			$("#datetimepicker").datetimepicker({
				format : "d/m/Y H:i",
				step : 10
			});

			$("#add-measure-form").submit(function(event) {
				MeasureForm.validateAddMeasureForm();
				if (MeasureForm.isAddMeasureFormValid()) {
					measuresTable.addMeasure(dataTables, dictionary);
				}

				event.preventDefault();
			});
		});
	});
</script>
</html>
