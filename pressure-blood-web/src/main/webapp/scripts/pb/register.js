var register = {
		registerUser : function() {
			$.ajax({
				url : "/pressure-blood-web/o.registerUser",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify({
					"username" : $("#username").val(),
					"password" : $("#password").val(),
					"confirmedPassword" : $("#confirmedPassword").val(),
					"email" : $("#email").val()
				}),
				success : function(json) {
					var status = json.status;
					switch (status) {
						case "SUCCESS" :
							window.location.href = "/pressure-blood-web/index.jsp";
							break;
						case "EXISTS":
							alert(json.message);

							$("#username").val("");
							$("#password").val("");
							$("#confirmedPassword").val("");
							$("#email").val("");
							break;
					}
				},
				error : function(xhr, status) {
					alertl("Failed to register user.\nServer returned " + xhr.status);

					$("#username").val("");
					$("#password").val("");
					$("#confirmedPassword").val("");
					$("#email").val("");
				}
			});
		}
}