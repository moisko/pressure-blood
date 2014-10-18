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

<script src = "scripts/pb/measureForm.js"></script>
<script src = "scripts/pb/registerForm.js"></script>
<script src = "scripts/pb/statistics.js"></script>
<script src = "scripts/pb/measuresTable.js"></script>
<script>
	// When the browser is ready ...
	google.load("visualization", "1", {packages : [ "corechart" ]});
	google.setOnLoadCallback(function() {
		$(function() {
			// Measures table init

			var dataTables = $("#measures-table").dataTable({
				"aoColumnDefs" : [{
					"bSortable" : false,
					"aTargets" : [ "delete-column" ]
				}],
				"order" : [[ 4, "asc" ]]
			});

			// Measures table populate

			measuresTable.populateMeasuresTable(dataTables);

			// Add measure

			$("#measures-table").delegate("tbody tr td a", "click", function(event) {
				if (confirm("Are you sure you want to delete this measure") == true) {
					var measureId = $(this).attr("id");
					var rowToDelete = $(this).parent().parent();
					measuresTable.deleteMeasure(dataTables, rowToDelete, measureId);
				}
				event.preventDefault();
			});

			$("#datetimepicker").datetimepicker({
				format : "d.m.Y H:i",
				step : 10
			});

			$("#add-measure-form").submit(function(event) {
				measureForm.validateAddMeasureForm();
				if (measureForm.isAddMeasureFormValid()) {
					measuresTable.addMeasure(dataTables);
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
						<th class="measures-header" colspan="6">Measures</th>
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
