package todo.api.model.tuple;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "database_sequences")
public class DatabaseSequenceTuple implements Serializable {
	
	private static final long serialVersionUID = -1056730510005299370L;
	
	@Id
	private String id;
	private long seq;
	
}