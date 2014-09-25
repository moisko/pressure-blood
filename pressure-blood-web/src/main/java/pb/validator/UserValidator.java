package pb.validator;

import pb.model.Users;

import static pb.validator.PasswordValidator.*;
import static pb.validator.EmailValidator.*;

public class UserValidator {

	private static final int MIN_USERNAME_CHARACTERS = 2;

	public static void validateUser(Users user) {
		ensureNotNull(user, "User instance is null");

		String username = user.getUsername();
		validateUsername(username);

		String password = user.getPassword();
		validatePassword(password);

		String email = user.getEmail();
		validateEmail(email);
	}

	public static void validateUsername(String username) {
		if (username == null) {
			throw new IllegalArgumentException("Username value not set");
		} else if (username.length() < MIN_USERNAME_CHARACTERS) {
			throw new IllegalArgumentException(
					"Username value must be more than two characters");
		}
	}

	private static void validateEmail(String email) {
		if (email == null) {
			throw new IllegalArgumentException("Email value not set");
		} else {
			if (!isEmailValid(email)) {
				throw new IllegalArgumentException("Email " + email
						+ " is not valid");
			}
		}
	}

	private static void ensureNotNull(Object object, String errorMessage) {
		if (object == null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
