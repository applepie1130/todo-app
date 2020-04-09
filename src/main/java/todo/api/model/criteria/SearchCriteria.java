package todo.api.model.criteria;

import java.io.Serializable;

import lombok.Data;

@Data
public class SearchCriteria implements Serializable {
	
	private static final long serialVersionUID = -7759098517614698442L;
	
	private String keyword; // 키워드
	private Integer page; // 페이지번호
	
	public int getPage() {
		if (this.page == null || this.page == 0) {
			this.page = 1;
		} else {
			this.page--;
		}
		
		return this.page;
	}
}
