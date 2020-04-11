package todo.api.model.criteria;

import java.io.Serializable;

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
@ApiModel(value="SearchCriteria", description="검색키워드, 페이징번호 파라미터")
public class SearchCriteria implements Serializable {
	
	private static final long serialVersionUID = -7759098517614698442L;
	
	@ApiModelProperty(notes = "키워드", name = "keyword", required = false)
	private String keyword;
	
	@ApiModelProperty(notes = "페이지번호", name = "page", required = true)
	private Integer page;
	
	public String getKeyword() {
		if ( this.keyword == null ) {
			this.keyword = "";
		}
		
		return this.keyword;
	}
	
	public int getPage() {
		if (this.page == null || this.page <= 0) {
			this.page = 1;
		} else {
			this.page--;
		}
		
		return this.page;
	}
}
