package pb.validator;

public class PasswordValidator {

	public static boolean isPasswordEqualToConfirmedPassword(String password1,
			String password2) {
		if (password1 != null && !password1.isEmpty()) {
			return password1.equals(password2);
		}
		return false;
	}
}
