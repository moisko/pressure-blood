var Register = {
	registerUser : function() {
		$.ajax({
			url : "/pressure-blood-web/o.registerUser",
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"username" : RegisterForm.getUsername(),
				"password" : RegisterForm.getPassword(),
				"confirmedPassword" : RegisterForm.getConfirmedPassword(),
				"email" : RegisterForm.getEmail()
			}),
			success : function(json) {
				var status = json.status;
				switch (status) {
				case "SUCCESS":
					window.location.href = "/pressure-blood-web/index.jsp";
					break;
				case "EXISTS":
					alert(json.message);

					RegisterForm.clear();

					break;
				}
			},
			error : function(xhr, status) {
				alert("Failed to register user.\nServer returned "
						+ xhr.status);

				RegisterForm.clear();
			}
		});
	}
}