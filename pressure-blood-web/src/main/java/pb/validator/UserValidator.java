package pb.validator;

import pb.model.Users;

import static pb.validator.PasswordValidator.*;
import static pb.validator.EmailValidator.*;

public class UserValidator {

	public static void validateUser(Users user) {
		ensureUserInstanceNotNull(user);

		String username = user.getUsername();
		validateUsername(username);

		String password = user.getPassword();
		validateUserPassword(password);

		String email = user.getEmail();
		validateEmail(email);
	}

	public static void validateUsername(String username) {
		if (username == null) {
			throw new IllegalArgumentException("Username value not set");
		} else if (username.length() < 2) {
			throw new IllegalArgumentException(
					"Username value must be greater than or qeual to 2");
		}
	}

	private static void validateEmail(String email) {
		if (email == null) {
			throw new IllegalArgumentException("Email value not set");
		} else {
			boolean emailValid = isEmailValid(email);
			if (!emailValid) {
				throw new IllegalArgumentException("Email " + email
						+ " is not valid");
			}
		}
	}

	private static void ensureUserInstanceNotNull(Users user) {
		if (user == null) {
			throw new IllegalArgumentException("User instance is null");
		}
	}
}
