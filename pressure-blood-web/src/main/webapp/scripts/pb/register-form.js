var RegisterForm = {
	validateRegisterForm : function() {
		$("#register-form").validate({
			rules : {
				username : {
					required : true,
					minlength : 2
				},
				password1 : {
					required : true,
					minlength : 4
				},
				password2 : {
					required : true,
					minlength : 4,
					equalTo : "#password1"
				},
				email : {
					required : true,
					email : true
				}
			},
			messages : {
				username : {
					required : "Username is required",
					minlength : $.format("At least {0} characters required")
				},
				password : {
					required : "Password is required",
					minlength : $.format("At least {0} characters required")
				},
				confirmedPassword : {
					required : "Confirm password is required",
					minlength : $.format("At least {0} characters required")
				},
				email : {
					required : "Email is required",
					email : $.format("Email is not valid")
				}
			},
			highlight : function(element, errorClass, validClass) {
				$(element).addClass(errorClass).removeClass(validClass);
			},
			unhighlight : function(element, errorClass, validClass) {
				$(element).removeClass(errorClass).addClass(validClass);
			}
		});
	},
	isRegisterFormValid : function() {
		return $("#register-form").valid();
	},
	clear : function() {
		$("#username").val("");
		$("#password").val("");
		$("#confirmedPassword").val("");
		$("#email").val("");
	},
	getUsername : function() {
		return $("#username").val();
	},
	getPassword : function() {
		return $("#password").val();
	},
	getConfirmedPassword : function() {
		return $("#confirmedPassword").val();
	},
	getEmail : function() {
		return $("#email").val();
	}
};