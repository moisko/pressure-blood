package pb.usermanagement;

import java.io.Serializable;

public class RegisterUserResponse implements Serializable {

	public enum Status {
		SUCCESS("success"), EXISTS("exists");

		private String status;

		private Status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}

	private static final long serialVersionUID = 1L;

	private Status status;
	private String message;

	public RegisterUserResponse() {

	}

	public RegisterUserResponse(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
