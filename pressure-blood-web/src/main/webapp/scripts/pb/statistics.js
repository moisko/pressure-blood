var statistics = {
		drawChart : function(json) {
			addColumnNames = function() {
				var columnNames = [ "Datetime", "SBP", "DBP" ];
				json.unshift(columnNames);
			};
			addColumnNames(json);
			var data = new google.visualization.arrayToDataTable($.parseJSON(JSON.stringify(json)));
			var chart = new google.visualization.ColumnChart($("#column-chart").get(0));
			chart.draw(data);
		},
		getData : function() {
			var jsonData = $.ajax({
				url : "/pressure-blood-web/o.getMeasuresForDataVizualisation",
				dataType : "json",
				async : false
			}).responseText;
			var json = JSON.parse(jsonData);
			return json;
		}
};