var statistics = {
		drawChart : function(json) {
			addColumnNames = function() {
				var columnNames = [ "Datetime", "SBP", "DBP" ];
				json.unshift(columnNames);
			};
			createAndPopulateDataFromJson = function(json) {
				return new google.visualization.arrayToDataTable(json);
			};
			drawChart = function(data) {
				var chart = new google.visualization.ColumnChart($("#column-chart").get(0));
				chart.draw(data);
			};
			addColumnNames(json);
			var data = createAndPopulateDataFromJson(json);
			drawChart(data);
		},
		getDataAsJson : function() {
			var text = $.ajax({
				url : "/pressure-blood-web/o.getMeasuresForDataVizualisation",
				dataType : "json",
				async : false
			}).responseText;
			var json = JSON.parse(text);
			return json;
		}
};