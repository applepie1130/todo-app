package todo.api.advice;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class TodoApiException extends RuntimeException {

	private static final long serialVersionUID = -5426414230378049770L;
	
	private HttpStatus status;
	private String message;
	
	public TodoApiException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
}