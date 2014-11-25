var Statistics = {
	drawChart : function(chartData) {
		var data = new google.visualization.DataTable();
		data.addColumn("string", "Datetime");
		data.addColumn("number", "SBP");
		data.addColumn("number", "DBP");
		chartData.forEach(function(measureData) {
			var dateTimeInMillis = measureData[0];
			var localDateTimeString = LocalDateTime.toLocalDateTimeString(dateTimeInMillis);
			var sbp = measureData[1];
			var dbp = measureData[2];
			data.addRow([ localDateTimeString, sbp, dbp ]);
		});

		var chart = new google.visualization.ColumnChart($("#column-chart")
				.get(0));

		chart.draw(data);
	},
	showStatisticsHeader : function() {
		$("#statistics").show();
	},
	hideStatisticsHeader : function() {
		$("#statistics").hide();
	}
};