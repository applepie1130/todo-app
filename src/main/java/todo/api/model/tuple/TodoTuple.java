package todo.api.model.tuple;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="TodoTuple", description="TODO일정정보")
public class TodoTuple {
	
	@Transient
    public static final String SEQUENCE_NAME = "sequence";
	
	@Id
	@ApiModelProperty(notes = "id", name = "id", required = true)
	private String id;
	
	@ApiModelProperty(notes = "일정내용", name = "contents", required = true)
	private String contents;
	
	@ApiModelProperty(notes = "일정상태", name = "status", required = true)
	private StatusType status;
	
	@ApiModelProperty(notes = "일정생성일자", name = "insertDate", required = true)
	private Date insertDate;
	
	@ApiModelProperty(notes = "일정수정일자", name = "updateDate", required = true)
	private Date updateDate;
	
}
