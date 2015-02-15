require.config({
	paths : {
		jquery : "jquery/jquery-1.11.1.min",
		Register : "pb/Register",
		RegisterForm : "pb/form/RegisterForm",
		validate : "jquery/jquery.validate.min"
	},
	shim : {
		validate : {
			deps : ["jquery"]
		}
	}
});

require(["jquery", "Register", "RegisterForm", "validate"], function($, Register, RegisterForm) {
	$("#register-form").submit(function(event) {
		RegisterForm.validateRegisterForm();
		if (RegisterForm.isRegisterFormValid()) {
			Register.registerUser();
		}
		event.preventDefault();
	});
});