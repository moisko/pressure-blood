var statistics = {
		drawChart : function(measuresArray) {
			addColumnNames = function() {
				var columnNames = [ "Datetime", "SBP", "DBP" ];
				measuresArray.unshift(columnNames);
			};

			createAndPopulateDataTable = function(json) {
				return new google.visualization.arrayToDataTable(json);
			};

			drawChart = function(data) {
				var chart = new google.visualization.ColumnChart($("#column-chart").get(0));
				chart.draw(data);
			};

			addColumnNames(measuresArray);

			var data = createAndPopulateDataTable(measuresArray);

			drawChart(data);
		}
};