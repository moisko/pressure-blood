var Statistics = {
	drawChart : function(chartData) {
		var data = new google.visualization.DataTable();
		data.addColumn("string", "Datetime");
		data.addColumn("number", "SBP");
		data.addColumn("number", "DBP");
		chartData.forEach(function(measureData) {
			var datetimeInMillis = measureData[0];
			var datetimeString = Datetime.toString(datetimeInMillis);
			var sbp = measureData[1];
			var dbp = measureData[2];
			data.addRow([ datetimeString, sbp, dbp ]);
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