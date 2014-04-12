<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pressure Blood App</title>

<link rel="stylesheet" type="text/css" href="styles/jquery/jquery.dataTables.css">
<link rel="stylesheet" type="text/css" href="styles/jquery/jquery.datetimepicker.css">
<link rel="stylesheet" type="text/css" href="styles/jquery/jquery-ui-1.10.3.custom.css">

<link rel="stylesheet" type="text/css" href="styles/pb/index.css">

<script src="scripts/jquery/jquery-1.10.2.js"></script>
<script src="scripts/jquery/jquery.validate.js"></script>
<script src="scripts/jquery/jquery.dataTables.js"></script>
<script src="scripts/jquery/jquery.datetimepicker.js"></script>
<script src="scripts/jquery/jquery-ui-1.10.3.custom.js"></script>

<script src = "scripts/pb/Measure.js"></script>

<script type="text/javascript"></script>
<script>
	var reloadBody = function() {
		$.ajax({
			url : "/pressure-blood-web/GetMeasures",
			context : document.body,
			success : function(s) {
				$(this).html(s);
			}
		});
	};

	// When the browser is ready ...
	$(function() {
		$("#measuresTable").dataTable();

		$("#datetimepicker").datetimepicker({
			format : "d.m.Y H:i",
			step : 10
		});

		$("#addMeasureForm").submit(function(event) {
			Measure.validateAddMeasureForm();
			if (Measure.isAddMeasureFormValid()) {
				Measure.addMeasure();
			}
			event.preventDefault();
		});

		$("#deleteMeasureForm").submit(function(event) {
			Measure.validateDeleteMeasureForm();
			if (Measure.isDeleteMeasureFormValid()) {
				Measure.deleteMeasure();
			}
			event.preventDefault();
		});
	});
</script>
</head>
<body onload="reloadBody()">
	<div class="userInfo">
		<nav>
			<span>${pageContext.request.remoteUser}</span> |
			<a href="/pressure-blood-web/LogoutServlet">logout</a>
		</nav>
	</div>
	<div class="center">
		<div id="measures">
			<table id="measuresTable" border="1">
				<thead>
					<tr>
						<th colspan="6">${measurements.size()} Measures</th>
					</tr>
					<tr>
						<th>ID</th>
						<th>TIME</th>
						<th>SBP</th>
						<th>DBP</th>
						<th>HAND</th>
						<th>PULSE</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="measurement" items="${measurements}">
						<tr>
							<td>${measurement.id}</td>
							<td>${measurement.datetime}</td>
							<td>${measurement.pressureBlood.sbp}</td>
							<td>${measurement.pressureBlood.dbp}</td>
							<td>${measurement.hand}</td>
							<td>${measurement.pulse}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div id="addMeasure">
			<h2>Add measure</h2>
			<form id="addMeasureForm" action="">
				<div id="sbpMeasure">
					<label for="sbp">SBP*:</label>
					<input id="sbp" name="sbp" type="number" min="0" max="300" size="3" class="required">
				</div>

				<div id="dbpMeasure">
					<label for="dbp">DBP*:</label>
					<input id="dbp" name="dbp" type="number" min="0" max="300" size="3" class="required">
				</div>

				<div id="datetime">
					<label for="datetimepicker">DATETIME*:</label>
					<input id="datetimepicker" name="datetimepicker" type="text" size="12" class="required">
				</div>

				<div id="handSelector">
					<label for="hand">HAND:</label>
					<select id="hand" name="hand">
						<option value="LEFT_HAND">Left hand</option>
						<option value="RIGHT_HAND">Right hand</option>
					</select>
				</div>

				<div id="pulseMeasure">
					<label for="pulse">PULSE:</label>
					<input id="pulse" name="pulse" type="number" min="0" max="300" size="3">
				</div>

				<div id="addMeasureButton">
					<button type="submit">Add measure</button>
				</div>
			</form>
		</div>

		<div id="deleteMeasure">
			<h2>Delete measure</h2>
			<form id="deleteMeasureForm" action="">
				<div id="measureId">
					<label for="id">ID*: </label>
					<input id="id" name="id" type="number" min="1" class="required">
				</div>
				<div id="deleteMeasureButton">
					<button type="submit">Delete measure</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
