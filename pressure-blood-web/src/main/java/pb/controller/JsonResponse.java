package pb.controller;

import java.io.Serializable;

public class JsonResponse implements Serializable {

	public enum Status {
		SUCCESS("success"), ERROR("error"), EXISTS("exists");

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
	private Exception cause;

	public JsonResponse() {

	}

	public JsonResponse(Status status, String message, Exception cause) {
		this.status = status;
		this.message = message;
		this.cause = cause;
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

	public Exception getCause() {
		return cause;
	}

	public void setCause(Exception cause) {
		this.cause = cause;
	}

}
