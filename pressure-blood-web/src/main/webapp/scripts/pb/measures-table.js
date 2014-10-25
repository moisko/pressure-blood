var MeasuresTable = {
	populateMeasuresTable : function(dataTables, dictionary) {
		$.get("/pressure-blood-web/o.getMeasures", function(measures) {
			$.each(measures, function(index, measure) {
				MeasuresTable.addRow(dataTables, measure);

				dictionary.add(PbMeasure.getId(measure), measure);
			});

			// Statistics

			if (!_.isEmpty(measures)) {
				var measuresData = dictionary.toDataTable();
				measuresData.unshift([ "Datetime", "SBP", "DBP" ]);
				Statistics.showStatisticsHeader();
				Statistics.drawChart(measuresData);
			}
		});
	},
	addMeasure : function(dataTables, dictionary) {
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

				MeasuresTable.addRow(dataTables, measure);

				// Add measure to dictionary

				dictionary.add(PbMeasure.getId(measure), measure);

				// Statistics

				var measuresData = dictionary.toDataTable();
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
	},
	deleteMeasure : function(dataTables, dictionary, tableRow, measureId) {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/o.deleteMeasure",
			data : "id=" + measureId,
			success : function(id) {
				// Delete row from measures table

				MeasuresTable.deleteRow(dataTables, tableRow);

				// Remove measure from dictionary

				dictionary.remove(_.values(id));

				// Statistics

				var measuresData = dictionary.toDataTable();
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
	},
	addRow : function(dataTables, measure) {
		dataTables.fnAddData([ PbMeasure.getSbp(measure),
				PbMeasure.getDbp(measure), PbMeasure.getHand(measure),
				PbMeasure.getPulse(measure), PbMeasure.getDatetime(measure),
				PbMeasure.getRemoveLink(measure) ]);
	},
	deleteRow : function(dataTables, tableRow) {
		dataTables.fnDeleteRow(tableRow);
	}
};