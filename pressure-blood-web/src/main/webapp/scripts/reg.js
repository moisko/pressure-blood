require.config({
	paths : {
		jquery : "jquery/jquery-1.11.1.min",
		Register : "pb/Register",
		RegisterForm : "pb/form/RegisterForm",
		validate : "jquery/jquery.validate.min",
		jqueryblockUI : "jquery/jquery.blockUI"
	},
	shim : {
		validate : {
			deps : ["jquery"]
		},
		jqueryblockUI : {
			deps : ["jquery"]
		}
	}
});

require(["jquery", "Register", "RegisterForm", "validate", "jqueryblockUI"], function($, Register, RegisterForm) {
	$("#register-form").submit(function(event) {
		RegisterForm.validateRegisterForm();
		if (RegisterForm.isRegisterFormValid()) {
			Register.registerUser();
		}
		event.preventDefault();
	});
});