package todo.api.model.tuple;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.api.model.type.StatusType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "todos")
public class TodoTuple {
	
	@Transient
    public static final String SEQUENCE_NAME = "sequence";
	
	@Id
	private String id;
	
	private String contents;
	
	private StatusType status;
	
	private Date insertDate;
	
	private Date updateDate;
	
}
