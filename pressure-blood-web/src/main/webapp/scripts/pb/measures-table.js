function MeasuresTable(dataTables, dictionary) {
	this.dataTables = dataTables;
	this.dictionary = dictionary;
}

MeasuresTable.prototype.populateMeasuresTable = function() {
	var dataTablesRef = this.dataTables;
	var dictionaryRef = this.dictionary;
	$.get("/pressure-blood-web/o.getMeasures", function(measures) {
		$.each(measures, function(index, measure) {
			dataTablesRef.fnAddData([ PbMeasure.getSbp(measure),
					PbMeasure.getDbp(measure), PbMeasure.getHand(measure),
					PbMeasure.getPulse(measure),
					PbMeasure.getDatetime(measure),
					PbMeasure.getRemoveLink(measure) ]);

			dictionaryRef.add(PbMeasure.getId(measure), measure);
		});

		// Statistics

		if (!_.isEmpty(measures)) {
			var measuresData = dictionaryRef.toDataTable();
			measuresData.unshift([ "Datetime", "SBP", "DBP" ]);
			Statistics.showStatisticsHeader();
			Statistics.drawChart(measuresData);
		}
	});
}

MeasuresTable.prototype.addMeasure = function() {
	var dataTablesRef = this.dataTables;
	var dictionaryRef = this.dictionary;
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
			"datetime" : MeasureForm.getDatetime(),
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

			var measuresData = dictionaryRef.toDataTable();
			measuresData.unshift([ "Datetime", "SBP", "DBP" ]);
			Statistics.showStatisticsHeader();
			Statistics.drawChart(measuresData);

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

			var measuresData = dictionaryRef.toDataTable();
			if (!_.isEmpty(measuresData)) {
				measuresData.unshift([ "Datetime", "SBP", "DBP" ]);
				Statistics.drawChart(measuresData);
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
