/*global $,_,alert,MeasureForm,PbMeasure,Statistics,LocalDateTime*/
function MeasuresTable(dataTables, dictionary, pageNumber) {
	this.dataTables = dataTables;
	this.dictionary = dictionary;
	this.pageNumber = pageNumber;
}

MeasuresTable.prototype.populateMeasuresTable = function() {
	$.get("/pressure-blood-web/o.getMeasures", $.proxy(function(measures) {
		// Dictionary

		this.getDictionary().initDictionary(measures);

		// Measures table

		var measuresData = this.getDictionary().toMeasuresData(),
		dataTablesRef = this.getDataTables();
		measuresData.forEach(function(measureData) {
			dataTablesRef.fnAddData([ measureData[0], measureData[1],
					measureData[2], measureData[3], measureData[4],
					measureData[5] ]);
		});

		// Statistics
		if (!_.isEmpty(measures)) {
			this.updateStatisticsChart();
		}
	}, this));
};

MeasuresTable.prototype.addMeasure = function() {
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
			"datetime" : LocalDateTime.parse(MeasureForm.getDatetime()),
			"hand" : MeasureForm.getHand(),
			"pulse" : parseInt(MeasureForm.getPulse(), 10)
		}),
		success : $.proxy(function(measure) {
			// Add row to measures table

			this.getDataTables().fnAddData(
					[ PbMeasure.getSbp(measure), PbMeasure.getDbp(measure),
							PbMeasure.getHand(measure),
							PbMeasure.getPulse(measure),
							PbMeasure.getDatetime(measure),
							PbMeasure.getRemoveLink(measure) ]);

			// Add measure to dictionary

			this.getDictionary().add(PbMeasure.getId(measure), measure);

			// Statistics

			this.updateStatisticsChart();

			// Clear form

			MeasureForm.clear();
		}, this),
		error : function(xhr, status) {
			alert("Failed to add measure.\nServer returned: "
					+ xhr.responseText);

			MeasureForm.clear();
		}
	});
};

MeasuresTable.prototype.deleteMeasure = function(tableRow) {
	$.ajax({
		type : "POST",
		url : "/pressure-blood-web/o.deleteMeasure",
		data : "id=" + this.getMeasureIdFromTableRow(tableRow),
		success : $.proxy(function() {
			// Delete row from measures table

			this.getDataTables().fnDeleteRow(tableRow);

			// Remove measure from dictionary

			var measureId = this.getMeasureIdFromTableRow(tableRow);
			this.getDictionary().remove(measureId);

			// Statistics

			this.updateStatisticsChart();
		}, this),
		error : function(xhr, status) {
			alert("Failed to delete measure.\nServer returned: "
					+ xhr.statusText);
		}
	});
};

MeasuresTable.prototype.updateStatisticsChart = function() {
	var beginIndex = this.calculateBeginIndex(),
	endIndex = this.calculateEndIndex(),
	chartData = this.getDictionary().toChartData().splice(beginIndex, endIndex);
	if (!_.isEmpty(chartData)) {
		Statistics.showStatisticsHeader();
		Statistics.drawChart(chartData);
	} else {
		Statistics.hideStatisticsHeader();
	}
};

MeasuresTable.prototype.calculateBeginIndex = function() {
	return 0;
};

MeasuresTable.prototype.calculateEndIndex = function() {
	var endIndex = 10;
	if (endIndex > this.dictionary.count()) {
		endIndex = this.dictionary.count();
	}
	return endIndex;
};

MeasuresTable.prototype.getMeasuresCount = function() {
	return this.dataTables.fnGetData().length;
};

MeasuresTable.prototype.getMeasureIdFromTableRow = function(tableRow) {
	return tableRow.find("td a").attr("id");
};

MeasuresTable.prototype.getDataTables = function() {
	return this.dataTables;
};

MeasuresTable.prototype.getDictionary = function() {
	return this.dictionary;
};

MeasuresTable.prototype.getPageNumber = function() {
	return this.pageNumber;
};

MeasuresTable.prototype.setPageNumber = function(pageNumber) {
	this.pageNumber = pageNumber;
};