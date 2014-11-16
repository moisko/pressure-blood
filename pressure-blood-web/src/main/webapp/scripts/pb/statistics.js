var Statistics = {
	drawChart : function(chartData) {
		var data = new google.visualization.DataTable();
		data.addColumn("string", "Datetime");
		data.addColumn("number", "SBP");
		data.addColumn("number", "DBP");
		chartData.forEach(function(measureData) {
			var oDate = new Date(measureData[0]);
			var date = oDate.getDate();
			var month = oDate.getMonth();// January is 0
			if(month == 0) {
				month++;
			}
			var fullYear = oDate.getFullYear();
			var hh = oDate.getHours();
			var mm = oDate.getMinutes();
			var formattedDate = date + "/" + month + "/" + fullYear + " " + hh
					+ ":" + mm;
			data.addRow([ formattedDate, measureData[1], measureData[2] ]);
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