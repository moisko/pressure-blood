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

<script src="scripts/jquery/jquery-1.11.1.min.js"></script>
<script src="scripts/jquery/jquery.validate.min.js"></script>
<script src="scripts/jquery/jquery.dataTables.min.js"></script>
<script src="scripts/jquery/jquery.datetimepicker.js"></script>
<script src="scripts/underscore/underscore-min.js"></script>
<script src="scripts/underscore/underscore-min.map"></script>

<script src="https://www.google.com/jsapi"></script>

<script src = "scripts/pb/measure-form.js"></script>
<script src = "scripts/pb/register-form.js"></script>
<script src = "scripts/pb/statistics.js"></script>
<script src = "scripts/pb/measures-table.js"></script>
<script src = "scripts/pb/pb-measure.js"></script>
<script src = "scripts/pb/datetime.js"></script>
<script src = "scripts/adt/dictionary.js"></script>
<script>
	// When the browser is ready ...
	google.load("visualization", "1", {packages : [ "corechart" ]});
	google.setOnLoadCallback(function() {
		$(function() {

			// Hide Statistics header

			Statistics.hideStatisticsHeader();

			// Create dictionary for holding all masures

			var dictionary = new Dictionary();

			// Measures table init

			var dataTables = $("#measures-table").dataTable({
				"aoColumnDefs" : [
					{"aTargets" : [ "datetime-column" ], "mRender" : function(datetimeInMillis) {
							var oDate = new Date(datetimeInMillis);
							var date = oDate.getDate();
							var month = oDate.getMonth();
							var fullYear = oDate.getFullYear();
							var hh = oDate.getHours();
							var mm = oDate.getMinutes();
							return date + "/" + month + "/" + fullYear + " " + hh + ":" + mm;
						}
					},
					{"aTargets" : [ "delete-column" ], "bSortable" : false}
				],
				"fnHeaderCallback" : function(nHead, aData, iStart, iEnd, aiDisplay) {
					nHead.getElementsByTagName("th")[0].innerHTML = (iEnd - iStart) + " Measures";
				}
			});

			var measuresTable = new MeasuresTable(dataTables, dictionary, 0);
			measuresTable.populateMeasuresTable();

			dataTables.on("page.dt", function(event, oSettings) {
				var pageNumber = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength);

				// Upadet measures table page number
				measuresTable.setPageNumber(pageNumber);

				var beginIndex = pageNumber * 10;
				var endIndex = beginIndex + 10;
				if(endIndex > dictionary.count()) {
					endIndex = dictionary.count();
				}

				// Statistics
				var chartData = dictionary.toChartData().splice(beginIndex, endIndex);
				chartData.unshift([ "Datetime", "SBP", "DBP" ]);
				Statistics.showStatisticsHeader();
				Statistics.drawChart(chartData);

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
						<option value="LEFT_HAND">Left hand</option>
						<option value="RIGHT_HAND">Right hand</option>
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
</html>
