package todo.api.model.tuple;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.api.model.type.StatusType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "todos")
@ApiModel(value="TodoTuple", description="TODO일정정보")
public class TodoTuple implements Serializable {
	
	@Transient
	private static final long serialVersionUID = 6604805003448004748L;

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime insertDate;
	
	@ApiModelProperty(notes = "일정수정일자", name = "updateDate", required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updateDate;
	
	@ApiModelProperty(notes = "참조ID리스트", name = "referIdList", required = false)
	private List<String> referIdList;
	
	@DBRef
	@ApiModelProperty(notes = "참조ID 일정정보 리스트", name = "referList", required = false)
	private List<TodoTuple> referList;
	
	@ApiModelProperty(notes = "참조된일정여부", name = "isRefered", required = false)
	private Boolean isRefered;
	
}
