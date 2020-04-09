package todo.api.model.entity;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponseEntity implements Serializable {
	
	private static final long serialVersionUID = 8305062862463069334L;
	
	private HttpStatus status;
	private String message;
	private Object result;

}
