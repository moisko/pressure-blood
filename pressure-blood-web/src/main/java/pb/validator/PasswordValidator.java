package pb.validator;

public class PasswordValidator {

	public static boolean isPasswordEqualToConfirmedPassword(String password1,
			String password2) {
		if (password1 != null && !password1.isEmpty()) {
			return password1.equals(password2);
		}
		return false;
	}

	public static void validatePassword(String password) {
		if (password == null) {
			throw new IllegalArgumentException("Password value not set");
		} else if (password.length() < 4) {
			throw new IllegalArgumentException(
					"Password length must be more than four characters");
		}
	}
}
