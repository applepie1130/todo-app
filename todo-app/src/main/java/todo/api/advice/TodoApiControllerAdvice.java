package todo.api.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import todo.api.model.entity.TodoResponseEntity;

@RestControllerAdvice
public class TodoApiControllerAdvice {

	@ExceptionHandler(TodoApiException.class)
	public ResponseEntity<TodoResponseEntity> handler(TodoApiException e) {
		
		TodoResponseEntity response = TodoResponseEntity.builder()
			.message(e.getMessage())
			.status(e.getStatus())
			.build();
		
		return new ResponseEntity<>(response, e.getStatus());
	}
}
