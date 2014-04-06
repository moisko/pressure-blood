<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pressure Blood App</title>
<link rel="stylesheet" type="text/css" href="styles/index.css">
<link rel="stylesheet" type="text/css" href="styles/jquery.dataTables.css">
<link rel="stylesheet" type="text/css" href="styles/jquery.datetimepicker.css">
<link rel="stylesheet" type="text/css" href="styles/jquery-ui-1.10.3.custom.css">
<script src="scripts/jquery-1.10.2.js"></script>
<script src="scripts/jquery.validate.js"></script>
<script src="scripts/jquery.dataTables.js"></script>
<script src="scripts/jquery.datetimepicker.js"></script>
<script src="scripts/jquery-ui-1.10.3.custom.js"></script>
<script src = "scripts/Html.js"></script>
<script type="text/javascript"></script>
<script>

	var getElementValueById = function(elementId) {
		var elementValue = document.getElementById(elementId).value;
		return elementValue;
	};

	var reloadBody = function() {
		var html = new Html();
		html.reloadBody("/pressure-blood-web/GetMeasures");
	};

	var getSBP = function() {
		var sbp = getElementValueById("sbp");
		if (!sbp) {
			throw new Error("[" + datetime + "] is not a valid value for sbp");
		}
		return sbp;
	};

	var getDBP = function() {
		var dbp = getElementValueById("dbp");
		if (!dbp) {
			throw new Error("[" + datetime + "] is not a valid value for dbp");
		}
		return dbp;
	};

	var getDatetime = function() {
		var datetime = getElementValueById("datetimepicker");
		if (!datetime) {
			throw new Error("[" + datetime
					+ "] is not a valid value for datetime");
		}
		return datetime;
	};

	var getHand = function() {
		var hand = $("#hand").val();
		return hand;
	};

	var getPulse = function() {
		var pulse = $("#pulse").val();
		if (!pulse) {
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
				"pressureBlood" : {
					"sbp" : getSBP(),
					"dbp" : getDBP()
				},
				"datetime" : getDatetime(),
				"hand" : getHand(),
				"pulse" : getPulse()
			}),
			success : function() {
				reloadBody();
			},
			error : function() {
				alert("Failed to add measure in db");
				reloadBody();
			}
		});
	};

	var deleteMeasure = function() {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/DeleteMeasure",
			data : "id=" + getElementValueById("del"),
			dataType : "json",
			success : function(json) {
				var status = json.status;
				if(status == "MEASURE_NOT_FOUND") {
					alert(json.message);
				}
				reloadBody();
			},
			error : function() {
				alert("Failed to delete measure with id " + getElementValueById("del"));
				reloadBody();
			}
		});
	};

	var validateAddMeasureForm = function() {
		$("#addMeasureForm").validate({
			rules: {
				sbp: {
					required: true
				},
				dbp: {
					required: true
				},
				datetimepicker: {
					required: true
				}
			},
			errorPlacement: function(error, element) {},
			highlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "red");
			},
			unhighlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "black");
			}
		});
	};

	var validateDeleteMeasureForm = function() {
		$("#deleteMeasureForm").validate({
			rules: {
				required: true
			},
			errorPlacement: function(error, element) {},
			highlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "red");
			},
			unhighlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "black");
			}
		});
	};

	var isAddMeasureFormValid = function() {
		return $("#addMeasureForm").valid();
	};

	var isDeleteMeasureFormValid = function() {
		return $("#deleteMeasureForm").valid();
	};

	// When the browser is ready ...
	$(function() {
		$("#measuresTable").dataTable();

		$("#datetimepicker").datetimepicker({
			format: "d.m.Y H:i",
			step: 10
		});

		$("#addMeasureForm").submit(function(event) {
			validateAddMeasureForm();
			if(isAddMeasureFormValid()) {
				addMeasure();
			}
			event.preventDefault();
		});

		$("#deleteMeasureForm").submit(function(event) {
			validateDeleteMeasureForm();
			if(isDeleteMeasureFormValid()) {
				deleteMeasure();
			}
			event.preventDefault();
		});
	});
</script>
</head>
<body onload="reloadBody()">
	<div class="userInfo">
		<nav>
			<h3>
				<span>Hello ${pageContext.request.remoteUser}</span>
				<a id="logout" href="/pressure-blood-web/LogoutServlet">Logout</a>
			</h3>
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
				<label class="control-label" for="sbp">SBP*: </label>
				<input id="sbp" name="sbp" type="number" min="0" max="300" size="3" class="required">

				<label class="control-label" for="dbp">DBP*: </label>
				<input id="dbp" name="dbp" type="number" min="0" max="300" size="3" class="required">

				<label class="control-label" for="datetimepicker">DATETIME*: </label>
				<input id="datetimepicker" name="datetimepicker" type="text" size="12" class="required">

				<label for="hand">HAND: </label>
				<select id="hand" name="hand">
					<option value="LEFT_HAND">Left hand</option>
					<option value="RIGHT_HAND">Right hand</option>
				</select>

				<label for="pulse">PULSE: </label>
				<input id="pulse" name="pulse" type="number" min="0" max="300" size="3">

				<button type="submit">Add measure</button>
			</form>
		</div>

		<div id="deleteMeasure">
			<h2>Delete measure</h2>
			<form id="deleteMeasureForm" action="">
				<label for="del">ID*: </label>
				<input id="del" name="del" type="number" min="1" class="required">
				<button type="submit">Delete measure</button>
			</form>
		</div>
	</div>
</body>
</html>
