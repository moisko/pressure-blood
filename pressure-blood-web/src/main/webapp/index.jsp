<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pressure Blood App</title>

<link rel="stylesheet" type="text/css" href="styles/jquery/jquery.dataTables.css">
<link rel="stylesheet" type="text/css" href="styles/jquery/jquery.datetimepicker.css">

<link rel="stylesheet" type="text/css" href="styles/pb/index.css">

<script src="scripts/jquery/jquery-1.10.2.js"></script>
<script src="scripts/jquery/jquery.validate.js"></script>
<script src="scripts/jquery/jquery.dataTables.js"></script>
<script src="scripts/jquery/jquery.datetimepicker.js"></script>

<script src = "scripts/pb/measure.js"></script>
<script>
	var getMeasures = function() {
		$.ajax({
			url : "/pressure-blood-web/o.getMeasures",
			context : document.body,
			success : function(s) {
				$(this).html(s);
			}
		});
	};

	// When the browser is ready ...
	$(function() {
		$("#measures-table").dataTable();

		$("#datetimepicker").datetimepicker({
			format : "d.m.Y H:i",
			step : 10
		});

		$("#add-measure-form").submit(function(event) {
			measure.validateAddMeasureForm();
			if (measure.isAddMeasureFormValid()) {
				measure.addMeasure();
			}
			event.preventDefault();
		});

		$("#delete-measure-form").submit(function(event) {
			measure.validateDeleteMeasureForm();
			if (measure.isDeleteMeasureFormValid()) {
				measure.deleteMeasure();
			}
			event.preventDefault();
		});
	});
</script>
</head>
<body onload="getMeasures()">
	<div id="user-info">
		<nav>
			<span>${pageContext.request.remoteUser}</span> |
			<a href="/pressure-blood-web/o.logout">logout</a>
		</nav>
	</div>
	<div id="main">
		<div id="measures">
			<table id="measures-table" border="1" class="display">
				<thead>
					<tr>
						<th colspan="6">${measures.size()} Measures</th>
					</tr>
					<tr>
						<th>ID</th>
						<th>SBP</th>
						<th>DBP</th>
						<th>HAND</th>
						<th>PULSE</th>
						<th>DATE AND TIME</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="measure" items="${measures}">
						<tr>
							<td>${measure.id}</td>
							<td>${measure.pressureBlood.sbp}</td>
							<td>${measure.pressureBlood.dbp}</td>
							<td>${measure.hand}</td>
							<td>${measure.pulse}</td>
							<td>${measure.datetime}</td>
						</tr>
					</c:forEach>
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

		<div id="delete-measure">
			<h2>Delete measure</h2>
			<form id="delete-measure-form" action="">
				<div id="measure-id">
					<label for="measure-id-input">ID*:</label>
					<input id="measure-id-input" name="measure-id-input" type="number" min="1" class="required">
				</div>
				<div id="delete-measure-button">
					<button type="submit">Delete measure</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
