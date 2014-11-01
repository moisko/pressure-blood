var Statistics = {
	drawChart : function(chartData) {
		var data = new google.visualization.arrayToDataTable(chartData);

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