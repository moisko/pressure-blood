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
	}
};