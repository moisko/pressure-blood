package pb.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(UserRolesId.class)
@Table(name = "USER_ROLES")
public class UserRoles implements Serializable {

	public enum Role {
		GUEST("Guest"), MEMBER("Member"), ADMIN("Admin");

		private String roleName;

		private Role(String roleName) {
			this.roleName = roleName;
		}

		public String getRoleName() {
			return roleName;
		}
	}

	private static final long serialVersionUID = 1L;

	@Id
	private String roleName;

	@Id
	private String username;

	public UserRoles() {

	}

	public UserRoles(String username, String roleName) {
		this.username = username;
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserRoles [roleName=" + roleName + ", username=" + username
				+ "]";
	}

}
