var measuresTable = {
		populateMeasuresTable : function(dataTables) {
			$.get("/pressure-blood-web/o.getMeasures", function(measures) {
				$.each(measures, function(index, measure) {
					dataTables.fnAddData([	getSbp(measure),
												getDbp(measure),
												getHand(measure),
												getPulse(measure),
												getDatetime(measure),
												getRemoveLink(measure)	]);
				});
			});
			getId = function(measure) {
				return measure.id;
			};
			getSbp = function(measure) {
				return measure.pressureBlood.sbp;
			};
			getDbp = function(measure) {
				return measure.pressureBlood.dbp;
			};
			getHand = function(measure) {
				return measure.hand;
			};
			getPulse = function(measure) {
				var pulse = measure.pulse;
				if (_.isUndefined(pulse)) {
					pulse = "";
				}
				return pulse;
			};
			getDatetime = function(measure) {
				return measure.datetime;
			};
			getRemoveLink = function(measure) {
				var removeLink = "<a id=\"" + measure.id + "\" href=\"\">Delete measure</a>";
				return removeLink;
			};
		},
		addMeasure : function(dataTables) {
			$.ajax({
				url : "/pressure-blood-web/o.addMeasure",
				type : "PUT",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify({
					"pressureBlood" : {
						"sbp" : parseInt($("#sbp").val(), 10),
						"dbp" : parseInt($("#dbp").val(), 10)
					},
					"datetime" : $("#datetimepicker").val(),
					"hand" : $("#hand").val(),
					"pulse" : parseInt($("#pulse").val(), 10)
				}),
				success : function(measure) {
					dataTables.fnAddData([	getSbp(measure),
												getDbp(measure),
												getHand(measure),
												getPulse(measure),
												getDatetime(measure),
												getRemoveLink(measure)	]);

					$("#statistics").show();
					drawChart();

					$("#sbp").val("");
					$("#dbp").val("");
					$("#pulse").val("");
					$("#datetimepicker").val("");
				},
				error : function(xhr, status) {
					alert("Failed to add measure.\nServer returned: " + xhr.responseText);

					$("#sbp").val("");
					$("#dbp").val("");
					$("#pulse").val("");
					$("#datetimepicker").val("");
				}
			});
			getSbp = function(measure) {
				return measure.pressureBlood.sbp;
			};
			getDbp = function(measure) {
				return measure.pressureBlood.dbp;
			};
			getHand = function(measure) {
				return measure.hand;
			};
			getPulse = function(measure) {
				var pulse = measure.pulse;
				if(_.isUndefined(pulse)) {
					pulse = "";
				}
				return pulse;
			};
			getDatetime = function(measure) {
				return measure.datetime;
			};
			getRemoveLink = function(measure) {
				var removeLink = "<a id=\"" + measure.id + "\" href=\"\">Delete measure</a>";
				return removeLink;
			};
			drawChart = function() {
				var json = getData();
				addColumnNames(json);
				var data = new google.visualization.arrayToDataTable($.parseJSON(JSON.stringify(json)));
				var chart = new google.visualization.ColumnChart($("#column-chart").get(0));
				chart.draw(data);
			};
			getData = function() {
				var jsonData = $.ajax({
					url : "/pressure-blood-web/o.getMeasuresForDataVizualisation",
					dataType : "json",
					async : false
				}).responseText;
				var json = JSON.parse(jsonData);
				return json;
			};
			addColumnNames = function(json) {
				var columnNames = [ "Datetime", "SBP", "DBP" ];
				json.unshift(columnNames);
			};
		},
		deleteMeasure : function(dataTables, rowToDelete, id) {
			$.ajax({
				type : "POST",
				url : "/pressure-blood-web/o.deleteMeasure",
				data : "id=" + id,
				success : function() {
					dataTables.fnDeleteRow(rowToDelete);
					var json = getData();
					if(!_.isEmpty(json)) {
						drawChart(json);
					} else {
						$("#statistics").hide();
					}
				},
				error : function(xhr, status) {
					alert("Failed to delete measure.\nServer returned: " + xhr.statusText);
				}
			});
			drawChart = function(json) {
				addColumnNames(json);
				var data = new google.visualization.arrayToDataTable($.parseJSON(JSON.stringify(json)));
				var chart = new google.visualization.ColumnChart($("#column-chart").get(0));
				chart.draw(data);
			};
			getData = function() {
				var jsonData = $.ajax({
					url : "/pressure-blood-web/o.getMeasuresForDataVizualisation",
					dataType : "json",
					async : false
				}).responseText;
				var json = JSON.parse(jsonData);
				return json;
			};
			addColumnNames = function(json) {
				var columnNames = [ "Datetime", "SBP", "DBP" ];
				json.unshift(columnNames);
			};
		}
};