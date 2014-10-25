var MeasuresTable = {
	populateMeasuresTable : function(dataTables, dictionary) {
		$.get("/pressure-blood-web/o.getMeasures", function(measures) {
			$.each(measures, function(index, measure) {
				MeasuresTable.addRow(dataTables, measure);

				dictionary.add(PbMeasure.getId(measure), measure);
			});

			// Statistics

			if (!_.isEmpty(measures)) {
				var data = dictionary.toDataTable();
				data.unshift([ "Datetime", "SBP", "DBP" ]);
				Statistics.showStatisticsHeader();
				Statistics.drawChart(data);
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
					"sbp" : parseInt($("#sbp").val(), 10),
					"dbp" : parseInt($("#dbp").val(), 10)
				},
				"datetime" : $("#datetimepicker").val(),
				"hand" : $("#hand").val(),
				"pulse" : parseInt($("#pulse").val(), 10)
			}),
			success : function(measure) {
				// Add row to measures table

				MeasuresTable.addRow(dataTables, measure);

				// Add measure to dictionary

				dictionary.add(PbMeasure.getId(measure), measure);

				// Statistics

				var data = dictionary.toDataTable();
				data.unshift([ "Datetime", "SBP", "DBP" ]);
				Statistics.showStatisticsHeader();
				Statistics.drawChart(data);

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
	deleteMeasure : function(dataTables, rowToDelete, id, dictionary) {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/o.deleteMeasure",
			data : "id=" + id,
			success : function(id) {
				// Delete row from measures table

				MeasuresTable.deleteRow(dataTables, rowToDelete);

				// Remove measure from dictionary

				dictionary.remove(_.values(id));

				// Statistics

				var data = dictionary.toDataTable();
				if (!_.isEmpty(data)) {
					data.unshift([ "Datetime", "SBP", "DBP" ]);
					Statistics.drawChart(data);
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
	deleteRow : function(dataTables, rowToDelete) {
		dataTables.fnDeleteRow(rowToDelete);
	}
};