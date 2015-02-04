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

</html>
