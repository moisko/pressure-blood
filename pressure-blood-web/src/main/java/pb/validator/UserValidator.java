package pb.validator;

import pb.model.Users;

public class UserValidator {

	public static void validateUser(Users user) {
		String username = user.getUsername();
		if (username == null) {
			throw new IllegalArgumentException("Username value not set");
		} else if (username.length() < 2) {
			throw new IllegalArgumentException(
					"Username value must be greater than or qeual to 2");
		}
		String password = user.getPassword();
		if (password == null) {
			throw new IllegalArgumentException("Password value not set");
		} else if (password.length() < 4) {
			throw new IllegalArgumentException(
					"Password value must be greater than or qeual to 4");
		}
		String email = user.getEmail();
		if (email == null) {
			throw new IllegalArgumentException("Email value not set");
		} else {
			boolean emailValida = EmailValidator.validate(email);
			if (!emailValida) {
				throw new IllegalArgumentException("Email " + email
						+ " is not valid");
			}
		}
	}
}
