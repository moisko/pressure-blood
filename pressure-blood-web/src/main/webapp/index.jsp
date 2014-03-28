<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Pressure Blood App</title>
<link rel="stylesheet" type="text/css" href="styles/index.css">
<script src="scripts/jquery-1.10.2.js"></script>
<script src="scripts/moment.js"></script>
<script src = "scripts/Html.js"></script>
<script type="text/javascript"></script>
<script>

	var parseDate = function(dateTime) {
		var date = moment(dateTime).format("lll");
		return date;
	};

	var getElementValueById = function(elementId) {
		var elementValue = document.getElementById(elementId).value;
		if(elementValue == null || elementValue == "") {
			throw new Error("getElementValueById(): value of element with id " + elementId + " not set");
		}
		return elementValue;
	};

	var reloadBody = function() {
		var html = new Html();
		html.reloadBody("/pressure-blood-web/GetResultServlet");
	};

	var getHand = function() {
		return $("#hand").val();
	};

	var addRecord = function() {
		$.ajax({
			url : "/pressure-blood-web/AddRecordServlet",
			type : "PUT",
			dataType : "html",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"pressureBlood" : {"sbp": getElementValueById("sbp_input"),
									"dbp": getElementValueById("dbp_input")},
				"datetime" : parseDate(getElementValueById("datetime_picker")),
				"hand" : getHand()
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

	var deleteRecord = function() {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/DeleteRecordServlet",
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
		// Add record to db
		$("#add_button").click(addRecord);

		// Delete record from db
		$("#del_button").click(deleteRecord);
	});

</script>
</head>
<body onload="reloadBody()">
	<div class="user-info">
		<h3>
			User: ${pageContext.request.remoteUser} <a
				href="/pressure-blood-web/LogoutServlet">Logout</a>
		</h3>
	</div>
	<div class="center">
		<table id="entries_table" border="1">
			<thead>
				<tr>
					<th colspan="5">${measurements.size()} Measures</th>
				</tr>
				<tr>
					<th>ID</th>
					<th>TIME</th>
					<th>SBP</th>
					<th>DBP</th>
					<th>HAND</th>
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
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<h2>Add measure</h2>
		<label for="sbp_input">SBP: </label>
		<input id="sbp_input" name="sbp_input" type="number" min="0" max="300">
		<label for="dbp_input">DBP: </label>
		<input id="dbp_input" name="dbp_input" type="number" min="0" max="300">
		<label for="datetime_picker">DATETIME: </label>
		<input id="datetime_picker" name="datetime_picker" type="datetime-local">
		<select id="hand">
			<option value="LEFT_HAND">Left hand</option>
			<option value="RIGHT_HAND">Right hand</option>
		</select>
		<button id="add_button">Add record</button>

		<h2>Delete measure</h2>
		<label for="del_input">ID:</label>
		<input id="del_input" name="del_input" type="number" min="1">
		<button id="del_button">Delete record</button>
	</div>
</body>
</html>
