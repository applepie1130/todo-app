package todo.api.model.type;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public enum MessageType {
	
	TODO_SUCCESS_SELECT("todo.success.select"),
	TODO_SUCCESS_PROCESS("todo.success.process"),
	
	TODO_ERROR_NODATA("todo.error.nodata"),
	TODO_ERROR_REQURIED_ID("todo.error.requried.id"),
	TODO_ERROR_REQURIED_KEYWORD("todo.error.required.keyword"),
	TODO_ERROR_REQURIED_CONTENT("todo.error.required.content"),
	TODO_ERROR_REFER_SELF("todo.error.refer.self"),
	TODO_ERROR_REFER_EXISTREFERID("todo.error.refer.existReferId"),
	TODO_ERROR_REFER_ALREADYREFERID("todo.error.refer.alreadyReferId"),
	TODO_ERROR_REFER_EXISTSNONCOMPLETEDREFERID("todo.error.refer.existsNonCompletedReferId"),
	TODO_ERROR_SAVE("todo.error.save"),
	TODO_ERROR_UPDATE_DEFAULT("todo.error.update.default"),
	TODO_ERROR_DEFAULT("todo.error.default"),
	
	;
	private String code;
	
	private MessageType(final String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
	
	public static MessageType getTypeByCode(final String code) {
		if ( StringUtils.isBlank(code) ) {
			return null;
		}
		
		return Arrays.stream(MessageType.values())
					 .filter(type -> StringUtils.equals(type.getCode(), code))
					 .findAny()
					 .orElse(null);
	} 
	
	

}



