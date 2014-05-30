var measure = {
	initMeasuresTable : function(measuresTable) {
		$.get("/pressure-blood-web/o.getMeasures", function(json) {
			$.each(json, function(index, object) {
				var measure = json[index];
				measuresTable.fnAddData([ getId(measure),
							getSbp(measure),
							getDbp(measure),
							getHand(measure),
							getPulse(measure),
							getDatetime(measure) ]);
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
			if(!pulse) {
				pulse = "";
			}
			return pulse;
		};
		getDatetime = function(measure) {
			return measure.datetime;
		};
	},
	addMeasure : function(measuresTable) {
		$.ajax({
			url : "/pressure-blood-web/o.addMeasure",
			type : "PUT",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"pressureBlood" : {
					"sbp" : parseInt($("#sbp-input").val()),
					"dbp" : parseInt($("#dbp-input").val())
				},
				"datetime" : $("#datetimepicker").val(),
				"hand" : $("#hand").val(),
				"pulse" : parseInt($("#pulse-input").val())
			}),
			success : function(json) {
				var measure = json;
				measuresTable.fnAddData([ getId(measure),
							getSbp(measure),
							getDbp(measure),
							getHand(measure),
							getPulse(measure),
							getDatetime(measure) ]);

				$("#sbp-input").val("");
				$("#dbp-input").val("");
				$("#pulse-input").val("");
				$("#datetimepicker").val("");
			},
			error : function(xhr, status) {
				alert("Failed to add measure in db. Server returned status code " + xhr.status);

				$("#sbp-input").val("");
				$("#dbp-input").val("");
				$("#datetimepicker").val("");
				$("#pulse-input").val("");
			}
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
			if(!pulse) {
				pulse = "";
			}
			return pulse;
		};
		getDatetime = function(measure) {
			return measure.datetime;
		};
	},
	deleteMeasure : function(measuresTable) {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/o.deleteMeasure",
			data : "id=" + $("#measure-id-input").val(),
			statusCode : {
				200 : function(response) {
					window.location.reload();
				},
				404 : function(resposne) {
					alert("Measure with id " + $("#measure-id-input").val()
							+ " not found");
					$("#measure-id-input").val("");
				},
				500 : function(response) {
					alert("Failed to delete measure with id "
							+ $("#measure-id-input").val());
					$("#measure-id-input").val("");
				}
			}
		});
	},
	validateAddMeasureForm : function() {
		$("#add-measure-form").validate({
			rules : {
				sbp : {
					required : true
				},
				dbp : {
					required : true
				},
				datetimepicker : {
					required : true
				}
			},
			errorPlacement : function(error, element) {
			},
			highlight : function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "red");
			},
			unhighlight : function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "black");
			}
		});
	},
	validateDeleteMeasureForm : function() {
		$("#delete-measure-form").validate({
			rules : {
				required : true
			},
			errorPlacement : function(error, element) {
			},
			highlight : function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "red");
			},
			unhighlight : function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "black");
			}
		});
	},
	isAddMeasureFormValid : function() {
		return $("#add-measure-form").valid();
	},
	isDeleteMeasureFormValid : function() {
		return $("#delete-measure-form").valid();
	}
};