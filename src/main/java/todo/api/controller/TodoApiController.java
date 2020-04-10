package todo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.api.model.criteria.SearchCriteria;
import todo.api.model.entity.TodoResponseEntity;
import todo.api.model.tuple.TodoTuple;
import todo.api.model.type.MessageType;
import todo.api.service.MessageService;
import todo.api.service.TodoService;


/**
 * The type Todo api controller.
 */
@Api(tags = "TodoApiController", value = "TODO API", produces = "application/json")
@Log4j2
@RestController
@RequestMapping(path = "/api/v1/", produces = "application/json")
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
			value = "TODO 리스트 조회",
			notes = "키워드로 TODO 일정 리스트 정보를 검색한다.",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "keyword", value = "검색키워드", required = true, dataType = "string", paramType = "query", example = "해야할 목록1"),
		@ApiImplicitParam(name = "page", value = "페이징번호", required = true, dataType = "string", paramType = "query", example = "1")
	})
	public ResponseEntity<TodoResponseEntity> getSearchTodoList(@ModelAttribute SearchCriteria searchCriteria) {
		
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
			value = "TODO 일정등록",
			notes = "TODO 일정을 등록한다",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "contents", value = "일정", required = true, dataType = "string", paramType = "query", example = "해야할 목록1")
	})
	public ResponseEntity<TodoResponseEntity> saveContents(@ModelAttribute("contents") final String contents) {
		
		todoService.saveContents(contents);
		
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
			value = "TODO 일정수정",
			notes = "TODO 일정을 수정한다",
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
			value = "TODO 일정 삭제",
			notes = "TODO 일정을 삭제한다",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "path", example = "")
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
			value = "TODO 일정완료",
			notes = "TODO 일정을 완료한다",
			response = TodoResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "path", example = "")
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
