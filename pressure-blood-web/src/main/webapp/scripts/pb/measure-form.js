var MeasureForm = {
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
	},
	clear : function() {
		$("#sbp").val("");
		$("#dbp").val("");
		$("#pulse").val("");
		$("#datetimepicker").val("");
	},
	getSbp : function() {
		return $("#sbp").val();
	},
	getDbp : function() {
		return $("#dbp").val();
	},
	getDatetime : function() {
		var dateTimeString = $("#datetimepicker").val();
		var dateTimInMillis = LocalDateTime.parse(dateTimeString);
		return dateTimInMillis;
	},
	getHand : function() {
		return $("#hand").val();
	},
	getPulse : function() {
		return $("#pulse").val();
	}

};