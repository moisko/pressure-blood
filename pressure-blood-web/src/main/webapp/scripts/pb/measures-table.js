function MeasuresTable(dataTables, dictionary, pageNumber) {
	this.dataTables = dataTables;
	this.dictionary = dictionary;
	this.pageNumber = pageNumber;
}

MeasuresTable.prototype.populateMeasuresTable = function() {
	var dictionaryRef = this.dictionary;
	var dataTablesRef = this.dataTables;
	var pageNumberRef = this.pageNumber;
	$.get("/pressure-blood-web/o.getMeasures", function(measures) {
		// Dictionary

		dictionaryRef.addAll(measures);

		// Measures table

		var measuresData = dictionaryRef.toMeasuresData();
		$.each(measuresData, function(index, measureData) {
			dataTablesRef.fnAddData(measuresData[index]);
		});

		// Statistics
		function calculateBeginIndex() {
			return pageNumberRef * 10;
		};

		function calculateEndIndex() {
			var endIndex = calculateBeginIndex() + 10;
			if (endIndex > dictionaryRef.count()) {
				endIndex = dictionaryRef.count();
			}
			return endIndex;
		};

		if (!_.isEmpty(measures)) {
			var beginIndex = calculateBeginIndex();
			var endIndex = calculateEndIndex();
			var chartData = dictionaryRef.toChartData().splice(beginIndex,
					endIndex);
			chartData.unshift([ "Datetime", "SBP", "DBP" ]);
			Statistics.showStatisticsHeader();
			Statistics.drawChart(chartData);
		}
	});
}

MeasuresTable.prototype.addMeasure = function() {
	var dataTablesRef = this.dataTables;
	var dictionaryRef = this.dictionary;
	var pageNumberRef = this.pageNumber;
	$.ajax({
		url : "/pressure-blood-web/o.addMeasure",
		type : "PUT",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify({
			"pressureBlood" : {
				"sbp" : parseInt(MeasureForm.getSbp(), 10),
				"dbp" : parseInt(MeasureForm.getDbp(), 10)
			},
			"datetime" : new Date(Date.parse(MeasureForm.getDatetime())).getTime(),
			"hand" : MeasureForm.getHand(),
			"pulse" : parseInt(MeasureForm.getPulse(), 10)
		}),
		success : function(measure) {
			// Add row to measures table

			dataTablesRef.fnAddData([ PbMeasure.getSbp(measure),
					PbMeasure.getDbp(measure), PbMeasure.getHand(measure),
					PbMeasure.getPulse(measure),
					PbMeasure.getDatetime(measure),
					PbMeasure.getRemoveLink(measure) ]);

			// Add measure to dictionary

			dictionaryRef.add(PbMeasure.getId(measure), measure);

			// Statistics

			function calculateBeginIndex() {
				return pageNumberRef * 10;
			};

			function calculateEndIndex() {
				var endIndex = calculateBeginIndex() + 10;
				if (endIndex > dictionaryRef.count()) {
					endIndex = dictionaryRef.count();
				}
				return endIndex;
			};

			var beginIndex = calculateBeginIndex();
			var endIndex = calculateEndIndex();
			var chartData = dictionaryRef.toChartData().splice(beginIndex, endIndex);
			chartData.unshift([ "Datetime", "SBP", "DBP" ]);
			Statistics.showStatisticsHeader();
			Statistics.drawChart(chartData);

			// Clear form

			MeasureForm.clear();
		},
		error : function(xhr, status) {
			alert("Failed to add measure.\nServer returned: "
					+ xhr.responseText);

			MeasureForm.clear();
		}
	});
}

MeasuresTable.prototype.deleteMeasure = function(tableRow) {
	var dataTablesRef = this.dataTables;
	var dictionaryRef = this.dictionary;
	var pageNumberRef = this.pageNumber;
	var getMeasureIdFromTableRow = this.getMeasureIdFromTableRow;
	$.ajax({
		type : "POST",
		url : "/pressure-blood-web/o.deleteMeasure",
		data : "id=" + getMeasureIdFromTableRow(tableRow),
		success : function() {
			// Delete row from measures table

			dataTablesRef.fnDeleteRow(tableRow);

			// Remove measure from dictionary

			dictionaryRef.remove(getMeasureIdFromTableRow(tableRow));

			// Statistics

			function calculateBeginIndex() {
				return pageNumberRef * 10;
			};

			function calculateEndIndex() {
				var endIndex = calculateBeginIndex() + 10;
				if (endIndex > dictionaryRef.count()) {
					endIndex = dictionaryRef.count();
				}
				return endIndex;
			};

			var endIndex = calculateEndIndex();
			if((endIndex % 10) == 0) {
				beginIndex = endIndex - 10;
			} else {
				var beginIndex = calculateBeginIndex();
			}
			var chartData = dictionaryRef.toChartData().splice(beginIndex, endIndex);
			if (!_.isEmpty(chartData)) {
				chartData.unshift([ "Datetime", "SBP", "DBP" ]);
				Statistics.drawChart(chartData);
			} else {
				Statistics.hideStatisticsHeader();
			}
		},
		error : function(xhr, status) {
			alert("Failed to delete measure.\nServer returned: "
					+ xhr.statusText);
		}
	});
}

MeasuresTable.prototype.getMeasuresCount = function() {
	return this.dataTables.fnGetData().length;
}

MeasuresTable.prototype.getMeasureIdFromTableRow = function(tableRow) {
	return tableRow.find("td a").attr("id");
}
