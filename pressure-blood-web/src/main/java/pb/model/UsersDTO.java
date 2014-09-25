package pb.model;

public class UsersDTO {

	private String username;
	private String password;
	private String confirmedPassword;
	private String email;

	public UsersDTO() {

	}

	public UsersDTO(String username, String password, String confirmedPassword,
			String email) {
		this.username = username;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
