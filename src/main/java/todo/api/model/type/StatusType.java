package todo.api.model.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="StatusType", description="TODO일정 상태타입")
public enum StatusType {
	
	@ApiModelProperty(notes = "일정 진행중", name = "ING")
	ING,
	
	@ApiModelProperty(notes = "일정 완료", name = "COMPLETED")
	COMPLETED
}
