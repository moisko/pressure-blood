var statistics = {
		drawChart : function(json) {
			addColumnNames = function() {
				var columnNames = [ "Datetime", "SBP", "DBP" ];
				json.unshift(columnNames);
			};

			createAndPopulateDataTable = function(json) {
				return new google.visualization.arrayToDataTable(json);
			};

			drawChart = function(data) {
				var chart = new google.visualization.ColumnChart($("#column-chart").get(0));
				chart.draw(data);
			};

			addColumnNames(json);

			var data = createAndPopulateDataTable(json);

			drawChart(data);
		}
};