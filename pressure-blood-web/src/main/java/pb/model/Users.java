package pb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import pb.validator.EmailValidator;
import pb.validator.PasswordValidator;
import pb.validator.UserValidator;

@Entity
@Table(name = "USERS")
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String email;
	private List<Measurement> measurements;
	private List<UserRoles> userRoles;

	public Users() {

	}

	public Users(UsersDTO userDTO) {
		this.username = userDTO.getUsername();
		UserValidator.validateUsername(username);

		if (PasswordValidator.isPasswordEqualToConfirmedPassword(
				userDTO.getPassword(), userDTO.getConfirmedPassword())) {
			this.password = userDTO.getPassword();
		} else {
			throw new IllegalArgumentException("The entered password "
					+ userDTO.getPassword()
					+ " is not the same as the confirmed one "
					+ userDTO.getConfirmedPassword());
		}
		PasswordValidator.validatePassword(userDTO.getPassword());

		if (EmailValidator.isEmailValid(userDTO.getEmail())) {
			this.email = userDTO.getEmail();
		} else {
			throw new IllegalArgumentException(
					"The entered email address is not valid");
		}

		List<UserRoles> userRoles = new ArrayList<UserRoles>();
		assignGuestRole(userRoles, username);
		assignMemberRole(userRoles, username);
		this.userRoles = new ArrayList<UserRoles>(userRoles);
	}

	@Id
	@Column(name = "USERNAME", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", nullable = false, length = 128)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "EMAIL", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "USERNAME")
	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "USERNAME")
	public List<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((measurements == null) ? 0 : measurements.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((userRoles == null) ? 0 : userRoles.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userRoles == null) {
			if (other.userRoles != null)
				return false;
		} else if (!userRoles.equals(other.userRoles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	private void assignGuestRole(List<UserRoles> userRoles, String username) {
		String roleName = UserRoles.Role.GUEST.getRoleName();
		UserRoles guestRole = new UserRoles(username, roleName);
		userRoles.add(guestRole);
	}

	private void assignMemberRole(List<UserRoles> userRoles, String username) {
		String roleName = UserRoles.Role.MEMBER.getRoleName();
		UserRoles membertRole = new UserRoles(username, roleName);
		userRoles.add(membertRole);
	}

}
