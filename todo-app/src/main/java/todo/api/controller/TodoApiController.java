package todo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import todo.api.model.criteria.SearchCriteria;
import todo.api.model.entity.TodoResponseEntity;
import todo.api.model.tuple.ContentsReferTuple;
import todo.api.model.tuple.TodoTuple;
import todo.api.model.type.MessageType;
import todo.api.service.MessageService;
import todo.api.service.TodoService;


/**
 * The type Todo api controller.
 */
@Log4j2
@RestController
@RequestMapping(path = "/api/v1/", produces = "application/json")
@Api(tags = "TodoApiController", value = "TODO API", produces = "application/json")
public class TodoApiController {
	
	private final TodoService todoService;
	private final MessageService messageService;

	/**
	 * Instantiates a new Todo api controller.
	 *
	 * @param todoService    the todo service
	 * @param messageService the message service
	 */
	@Autowired
	public TodoApiController(TodoService todoService, MessageService messageService) {
		super();
		this.todoService = todoService;
		this.messageService = messageService;
	}

	/**
	 * Gets search todo list.
	 *
	 * @param searchCriteria the search criteria
	 * @return the search todo list
	 */
	@GetMapping(path="/todo")
	@ApiOperation(
			httpMethod = "GET",
			value = "TODO 리스트 조회 API",
			notes = "키워드로 TODO 일정 리스트 정보를 검색한다.",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "keyword", value = "검색키워드", required = false, dataType = "string", paramType = "query", example = ""),
		@ApiImplicitParam(name = "page", value = "페이징번호", required = true, dataType = "string", paramType = "query", example = "1")
	})
	public ResponseEntity<TodoResponseEntity> getSearchTodoList(@ModelAttribute final SearchCriteria searchCriteria) {
		
		Page<TodoTuple> todoList = todoService.getSearchTodoList(searchCriteria);
		
		return new ResponseEntity<>(TodoResponseEntity.builder()
									.result(todoList)
									.status(HttpStatus.OK)
									.message(messageService.getMessage(MessageType.TODO_SUCCESS_SELECT.getCode(), null))
									.build()
									, HttpStatus.OK);
	}


	/**
	 * Save contents response entity.
	 *
	 * @param contents the contents
	 * @return the response entity
	 */
	@PostMapping(path="/todo")
	@ApiOperation(
			httpMethod = "POST",
			value = "TODO 일정등록 API",
			notes = "TODO 일정을 등록한다.",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "contents", value = "일정내용", required = true, dataType = "string", paramType = "query", example = "해야할 목록1"),
		@ApiImplicitParam(name = "referIdList", value = "참조할 일정ID", required = false, dataType = "string", paramType = "query", example = "")
	})
	public ResponseEntity<TodoResponseEntity> saveContents(@ModelAttribute final ContentsReferTuple contentsReferTuple) {
		
		todoService.saveContents(contentsReferTuple);
		
		return new ResponseEntity<>(TodoResponseEntity.builder()
				.status(HttpStatus.OK)
				.message(messageService.getMessage(MessageType.TODO_SUCCESS_PROCESS.getCode(), null))
				.build()
				, HttpStatus.OK);
	}


	/**
	 * Update contents response entity.
	 *
	 * @param todoTuple the todo tuple
	 * @return the response entity
	 */
	@PutMapping(path="/todo")
	@ApiOperation(
			httpMethod = "PUT",
			value = "TODO 일정수정 API",
			notes = "TODO 일정을 수정한다.",
			response = TodoResponseEntity.class
			)
	public ResponseEntity<TodoResponseEntity> updateContents(@RequestBody TodoTuple todoTuple) {
		
		todoService.updateContents(todoTuple);
		
		return new ResponseEntity<>(TodoResponseEntity.builder()
				.status(HttpStatus.OK)
				.message(messageService.getMessage(MessageType.TODO_SUCCESS_PROCESS.getCode(), null))
				.build()
				, HttpStatus.OK);
		
	}


	/**
	 * Delete contents response entity.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@DeleteMapping(path="/todo/{id}")
	@ApiOperation(
			httpMethod = "DELETE",
			value = "TODO 일정 삭제 API",
			notes = "TODO 일정을 삭제한다.",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "일정 고유ID", required = true, dataType = "string", paramType = "path", example = "1")
	})
	public ResponseEntity<TodoResponseEntity> deleteContents(@PathVariable("id") final String id) {
		
		todoService.deleteContents(id);
		
		return new ResponseEntity<>(TodoResponseEntity.builder()
				.status(HttpStatus.OK)
				.message(messageService.getMessage(MessageType.TODO_SUCCESS_PROCESS.getCode(), null))
				.build()
				, HttpStatus.OK);
		
	}


	/**
	 * Complete todo response entity.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@PutMapping(path="/complete/{id}")
	@ApiOperation(
			httpMethod = "PUT",
			value = "TODO 일정 상태변경 API",
			notes = "TODO 일정을 완료 혹은 진행중으로 수정한다.",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "일정 고유ID", required = true, dataType = "string", paramType = "path", example = "")
	})
	public ResponseEntity<TodoResponseEntity> completeTodo(@PathVariable("id") final String id) {
		
		todoService.updateStatus(id);
		
		return new ResponseEntity<>(TodoResponseEntity.builder()
				.status(HttpStatus.OK)
				.message(messageService.getMessage(MessageType.TODO_SUCCESS_PROCESS.getCode(), null))
				.build()
				, HttpStatus.OK);
	}
}
