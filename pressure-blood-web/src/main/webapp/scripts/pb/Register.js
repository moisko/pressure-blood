var Register = {
		registerUser : function() {
			$.ajax({
				url : "/pressure-blood-web/RegisterNewUserServlet",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify({
					"username" : $("#username").val(),
					"password1" : $("#password1").val(),
					"password2" : $("#password2").val(),
					"email" : $("#email").val()
				}),
				success : function(json) {
					var status = json.status;
					switch (status) {
						case "SUCCESS" :
							window.location.href = "/pressure-blood-web/GetMeasures";
							break;
						case "EXISTS":
						case "ERROR":
							alert(json.message);
							window.location.reload();
							break;
					}
				},
				error : function(json) {
					alert(json.responseJSON.message);
					window.location.reload();
				}
			});
		},
		validateRegisterForm : function() {
			$("#registerForm").validate({
				rules: {
					username: {
						required: true,
						minlength: 2
					},
					password1: {
						required: true,
						minlength: 4
					},
					password2: {
						required: true,
						minlength: 4,
						equalTo: "#password1"
					},
					email: {
						required: true,
						email: true
					}
				},
				messages: {
					username: {
						required: "Username is required",
						minlength: $.format("At least {0} characters required")
					},
					password1: {
						required: "Password is required",
						minlength: $.format("At least {0} characters required")
					},
					password2: {
						required: "Confirm password is required",
						minlength: $.format("At least {0} characters required")
					},
					email: {
						required: "Email is required",
						email: $.format("Email is not valid")
					}
				},
				highlight: function(element, errorClass, validClass) {
					$(element).addClass(errorClass).removeClass(validClass);
				},
				unhighlight: function(element, errorClass, validClass) {
					$(element).removeClass(errorClass).addClass(validClass);
				}
			});
		},
		isRegisterFormValid : function() {
			return $("#registerForm").valid();
		}
}