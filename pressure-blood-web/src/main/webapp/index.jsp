<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pressure Blood App</title>
<link rel="stylesheet" type="text/css" href="styles/index.css">
<link rel="stylesheet" type="text/css" href="styles/jquery.dataTables.css">
<script src="scripts/jquery-1.10.2.js"></script>
<script src="scripts/moment.js"></script>
<script src="scripts/jquery.dataTables.js"></script>
<script src = "scripts/Html.js"></script>
<script type="text/javascript"></script>
<script>

	var parseDate = function(dateTime) {
		var date = moment(dateTime).format("lll");
		return date;
	};

	var getElementValueById = function(elementId) {
		var elementValue = document.getElementById(elementId).value;
		console.log("value of element with id " + elementId + ": " + elementValue);
		if(!elementValue) {
			throw new Error("Value of element with id " + elementId + " not set");
		}
		return elementValue;
	};

	var reloadBody = function() {
		var html = new Html();
		html.reloadBody("/pressure-blood-web/GetMeasures");
	};

	var getSBP = function() {
		return getElementValueById("sbp_input");
	};

	var getDBP = function() {
		return getElementValueById("dbp_input");
	};

	var getDatetime = function() {
		return getElementValueById("datetime_picker");
	};

	var getHand = function() {
		var hand = $("#hand").val();
		if(!hand) {
			throw new Error("getHand(): value of element with id hand not set");
		}
		return hand;
	};

	var getPulse = function() {
		var pulse = $("#pulse_input").val();
		if(!pulse) {
			pulse = 0;
		}
		return pulse;
	};

	var addMeasure = function() {
		$.ajax({
			url : "/pressure-blood-web/AddMeasure",
			type : "PUT",
			dataType : "html",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"pressureBlood" : {"sbp": getSBP(), "dbp": getDBP()},
				"datetime" : parseDate(getDatetime()),
				"hand" : getHand(),
				"pulse" : getPulse()
			}),
			success : function() {
				reloadBody();
			},
			error : function() {
				alert("Failed to add record in db");
				reloadBody();
			}
		});
	};

	var deleteMeasure = function() {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/DeleteMeasure",
			data : "id=" + getElementValueById("del_input"),
			dataType : "json",
			success : function(json) {
				alert(json.message);
				reloadBody();
			},
			error : function() {
				alert("Error occured while deleting record with id " + getElementValueById("del_input"));
				reloadBody();
			}
		});
	};

	// When the browser is ready ...
	$(function() {
		$("#measuresTable").dataTable();

		// Add record to db
		$("#addMeasure").submit(function(event) {
			addMeasure();

			event.preventDefault();
		});

		// Delete record from db
		$("#deleteMeasure").submit(function(event) {
			deleteMeasure();

			event.preventDefault();
		});
	});

</script>
</head>
<body onload="reloadBody()">
	<div class="user-info">
		<h3>
			${pageContext.request.remoteUser}
			<a href="/pressure-blood-web/LogoutServlet">Logout</a>
		</h3>
	</div>
	<div class="center">
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

		<h2>Add measure</h2>
		<form id="addMeasure" action="">
			<label for="sbp_input">SBP*: </label>
			<input id="sbp_input" name="sbp_input" type="number" min="0" max="300">

			<label for="dbp_input">DBP*: </label>
			<input id="dbp_input" name="dbp_input" type="number" min="0" max="300">

			<label for="hand">HAND*: </label>
			<select id="hand" name="hand">
				<option value="LEFT_HAND">Left hand</option>
				<option value="RIGHT_HAND">Right hand</option>
			</select>

			<label for="datetime_picker">DATETIME*: </label>
			<input id="datetime_picker" name="datetime_picker" type="datetime-local">

			<label for="pulse_input">PULSE: </label>
			<input id="pulse_input" name="pulse_input" type="number" min="0" max="300">

			<button id="add_button" type="submit">Add measure</button>
		</form>

		<h2>Delete measure</h2>
		<form id="deleteMeasure" action="">
			<label for="del_input">ID:</label>
			<input id="del_input" name="del_input" type="number" min="1">
			<button id="del_button" type="submit">Delete measure</button>
		</form>
	</div>
</body>
</html>
