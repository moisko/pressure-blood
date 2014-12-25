/*global $,google,LocalDateTime*/
var Statistics = {
	drawChart : function(chartData) {
		var data = new google.visualization.DataTable(),
		chart = new google.visualization.ColumnChart($("#column-chart").get(0));

		data.addColumn("string", "Datetime");
		data.addColumn("number", "SBP");
		data.addColumn("number", "DBP");
		chartData.forEach(function(measureData) {
			var dateTimeInMillis = measureData[0],
			localDateTimeString = LocalDateTime.toLocalDateTimeString(dateTimeInMillis),
			sbp = measureData[1],
			dbp = measureData[2];
			data.addRow([ localDateTimeString, sbp, dbp ]);
		});

		chart.draw(data);
	},
	showStatisticsHeader : function() {
		$("#statistics").show();
	},
	hideStatisticsHeader : function() {
		$("#statistics").hide();
	}
};