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

<script src = "scripts/pb/measure.js"></script>
<script>
	var initMeasuresTable = function(measuresTable) {
		$.get("/pressure-blood-web/o.getMeasures", function(measures) {
			$.each(measures, function(index, measure) {
				measuresTable.fnAddData([	getSbp(measure),
											getDbp(measure),
											getHand(measure),
											getPulse(measure),
											getDatetime(measure),
											getRemoveLink(measure)	]);
			});
		});
		getId = function(measure) {
			return measure.id;
		};
		getSbp = function(measure) {
			return measure.pressureBlood.sbp;
		};
		getDbp = function(measure) {
			return measure.pressureBlood.dbp;
		};
		getHand = function(measure) {
			return measure.hand;
		};
		getPulse = function(measure) {
			var pulse = measure.pulse;
			if (_.isUndefined(pulse)) {
				pulse = "";
			}
			return pulse;
		};
		getDatetime = function(measure) {
			return measure.datetime;
		};
		getRemoveLink = function(measure) {
			var removeLink = "<a id=\"" + measure.id + "\" href=\"\">Delete measure</a>";
			return removeLink;
		}
	};

	var drawChart = function(json) {
		addColumnNames(json);
		var data = new google.visualization.arrayToDataTable($.parseJSON(JSON.stringify(json)));
		var chart = new google.visualization.ColumnChart(document.getElementById("column-chart"));
		chart.draw(data);
	};

	var getData = function() {
		var jsonData = $.ajax({
			url : "/pressure-blood-web/o.getMeasuresForDataVizualisation",
			dataType : "json",
			async : false
		}).responseText;
		var json = JSON.parse(jsonData);
		return json;
	};

	var addColumnNames = function(json) {
		var columnNames = [ "Datetime", "SBP", "DBP" ];
		json.unshift(columnNames);
	};

	// When the browser is ready ...
	google.load("visualization", "1", {packages : [ "corechart" ]});
	google.setOnLoadCallback(function() {
	$(function() {
		var measuresTable = $("#measures-table").dataTable({
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ "no-sort" ]
			} ]
		});

		initMeasuresTable(measuresTable);

		var json = getData();
		if(!_.isEmpty(json)) {
			drawChart(json);
		} else {
			$("#statistics").hide();
		}

		$("#measures-table").delegate("tbody tr td a", "click", function(event) {
			if (confirm("Are you sure you want to delete this measure") == true) {
				var measureId = $(this).attr("id");
				var rowToDelete = $(this).parent().parent();
				measure.deleteMeasure(measuresTable, rowToDelete, measureId);
			}
			event.preventDefault();
		});

		$("#datetimepicker").datetimepicker({
			format : "d.m.Y H:i",
			step : 10
		});

		$("#add-measure-form").submit(function(event) {
			measure.validateAddMeasureForm();
			if (measure.isAddMeasureFormValid()) {
				measure.addMeasure(measuresTable);
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
			<span>${pageContext.request.remoteUser}</span> |
			<a href="/pressure-blood-web/o.logout">logout</a>
		</nav>
	</div>
	<div id="main">
		<div id="measures">
			<table id="measures-table" class="display" border="1">
				<thead>
					<tr>
						<th colspan="6">Measures</th>
					</tr>
					<tr>
						<th>SBP</th>
						<th>DBP</th>
						<th>HAND</th>
						<th>PULSE</th>
						<th>DATE AND TIME</th>
						<th class="no-sort">DELETE</th>
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
			<div id="column-chart" style="width: 900px; height: 500px;"></div>
		</div>
	</div>
</body>
</html>
