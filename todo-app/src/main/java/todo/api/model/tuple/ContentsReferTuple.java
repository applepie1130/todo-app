
package todo.api.model.tuple;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="ContentsReferTuple", description="TODO 일정 및 참조ID전달객체")
public class ContentsReferTuple implements Serializable {
	
	private static final long serialVersionUID = -7289951249534662551L;

	@ApiModelProperty(notes = "일정내용", name = "contents", required = true)
	private String contents;
	
	@ApiModelProperty(notes = "참조ID리스트", name = "referIdList", required = false)
	private List<String> referIdList;
	
}
