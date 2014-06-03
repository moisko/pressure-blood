var measure = {
	addMeasure : function(measuresTable) {
		$.ajax({
			url : "/pressure-blood-web/o.addMeasure",
			type : "PUT",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"pressureBlood" : {
					"sbp" : parseInt($("#sbp").val()),
					"dbp" : parseInt($("#dbp").val())
				},
				"datetime" : $("#datetimepicker").val(),
				"hand" : $("#hand").val(),
				"pulse" : parseInt($("#pulse").val())
			}),
			success : function(json) {
				var measure = json;
				measuresTable.fnAddData([ getSbp(measure),
							getDbp(measure),
							getHand(measure),
							getPulse(measure),
							getDatetime(measure),
							getRemoveLink(measure) ]);

				$("#sbp").val("");
				$("#dbp").val("");
				$("#pulse").val("");
				$("#datetimepicker").val("");
			},
			error : function(xhr, status) {
				alert("Failed to add measure in db. Server returned status code " + xhr.status);

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
			if(!pulse) {
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
	deleteMeasure : function(measuresTable, rowToDelete, id) {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/o.deleteMeasure",
			data : "id=" + id,
			statusCode : {
				200 : function(response) {
					measuresTable.fnDeleteRow(rowToDelete);
				},
				404 : function(resposne) {
					alert("Measure with id " + $("#measure-id-input").val()
							+ " not found");
				},
				500 : function(response) {
					alert("Failed to delete measure with id "
							+ $("#measure-id-input").val());
				}
			}
		});
	},
	validateAddMeasureForm : function() {
		$("#add-measure-form").validate({
			rules : {
				sbp : {
					required : true,
					min : 0,
					max : 300
				},
				dbp : {
					required : true,
					min : 0,
					max : 300
				},
				pulse : {
					required : false,
					min : 0,
					max : 300
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
	isAddMeasureFormValid : function() {
		return $("#add-measure-form").valid();
	}
};