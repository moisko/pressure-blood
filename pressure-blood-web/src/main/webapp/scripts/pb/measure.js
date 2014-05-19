var measure = {
	addMeasure : function() {
		$.ajax({
			url : "/pressure-blood-web/o.addMeasure",
			type : "PUT",
			dataType : "html",
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
			success : function() {
				getMeasures();
			},
			error : function(xhr, status) {
				alert("Failed to add measure in db. Server returned status code " + xhr.status);

				$("#sbp-input").val("");
				$("#dbp-input").val("");
				$("#datetimepicker").val("");
				$("#pulse-input").val("");
			}
		});
	},
	deleteMeasure : function() {
		$.ajax({
			type : "POST",
			url : "/pressure-blood-web/o.deleteMeasure",
			data : "id=" + $("#measure-id-input").val(),
			dataType : "json",
			success : function(json) {
				var status = json.status;
				if(status === "MEASURE_FOUND") {
					getMeasures();
				} else if(status === "MEASURE_NOT_FOUND") {
					alert(json.message);

					$("#measure-id-input").val("");
				} 
			},
			error : function() {
				alert("Failed to delete measure with id " + $("#measure-id-input").val());

				$("#measure-id-input").val("");
			}
		});
	},
	validateAddMeasureForm : function() {
		$("#add-measure-form").validate({
			rules: {
				sbp: {
					required: true
				},
				dbp: {
					required: true
				},
				datetimepicker: {
					required: true
				}
			},
			errorPlacement: function(error, element) {},
			highlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "red");
			},
			unhighlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "black");
			}
		});
	},
	validateDeleteMeasureForm : function() {
		$("#delete-measure-form").validate({
			rules: {
				required: true
			},
			errorPlacement: function(error, element) {},
			highlight: function(element, errorClass, validClass) {
				var label = $("label[for='" + element.id + "']");
				$(label).css("color", "red");
			},
			unhighlight: function(element, errorClass, validClass) {
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