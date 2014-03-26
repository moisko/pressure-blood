package pb.controller;

import java.io.Serializable;

public class JsonResponse implements Serializable {

	// Register operation
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String EXISTS = "exists";

	// Delete operation
	public static final String RECORD_FOUND = "recordFound";
	public static final String RECORD_NOT_FOUND = "recordNotFound";

	private static final long serialVersionUID = 1L;

	private String status;
	private String message;

	public JsonResponse() {

	}

	public JsonResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
