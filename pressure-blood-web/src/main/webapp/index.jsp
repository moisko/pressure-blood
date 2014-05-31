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

<script src = "scripts/pb/measure.js"></script>
<script src="scripts/pb/measuresTable.js"></script>
<script>
	// When the browser is ready ...
	$(function() {
		var measuresTable = $("#measures-table").dataTable();

		pbMeasuresTable.initMeasuresTable(measuresTable);

		$("#measures-table").delegate("tbody tr td a", "click", function(event) {
			var id = $(this).attr("id");
			var rowToDelete = $(this).parent().parent();
			measure.deleteMeasure(measuresTable, rowToDelete, id);
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
			<table id="measures-table">
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
						<th>DELETE</th>
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
					<label for="sbp-input">SBP*:</label>
					<input id="sbp-input" name="sbp-input" type="number" min="0" max="300" size="3" class="required">
				</div>

				<div id="dbp-measure">
					<label for="dbp-input">DBP*:</label>
					<input id="dbp-input" name="dbp-input" type="number" min="0" max="300" size="3" class="required">
				</div>

				<div id="handSelector">
					<label for="hand">HAND:</label>
					<select id="hand" name="hand">
						<option value="LEFT_HAND">Left hand</option>
						<option value="RIGHT_HAND">Right hand</option>
					</select>
				</div>

				<div id="pulse-measure">
					<label for="pulse-input">PULSE:</label>
					<input id="pulse-input" name="pulse-input" type="number" min="0" max="300" size="3">
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
	</div>
</body>
</html>
