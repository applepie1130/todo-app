package todo.api.advice;

import org.springframework.http.HttpStatus;

public class TodoApiException extends RuntimeException {

	private static final long serialVersionUID = -5426414230378049770L;

	private HttpStatus status;
	private String message;

	public TodoApiException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}