package todo.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import todo.api.model.tuple.TodoTuple;
import todo.api.service.TodoService;


@Api(tags = "TodoApiController", value = "TODO API", produces = "application/json")
@RestController
@RequestMapping(path = "/api/v1/", produces = "application/json")
public class TodoApiController {
	
	private TodoService todoService;

	@Autowired
	public TodoApiController(TodoService todoService) {
		super();
		this.todoService = todoService;
	}

	@GetMapping(path="/todo")
	@ApiOperation(
			httpMethod = "GET",
			value = "TODO 리스트 조회",
			notes = "입력한 TODO 일정 리스트 정보 조회한다",
			response = ResponseEntity.class
			)
	public ResponseEntity<List<TodoTuple>> getTodoList() {
		
		List<TodoTuple> todoList = todoService.getTodoList();
		return new ResponseEntity<List<TodoTuple>>(todoList, HttpStatus.OK);
		
	}

	@PostMapping(path="/todo")
	@ApiOperation(
			httpMethod = "POST",
			value = "TODO 일정등록",
			notes = "TODO 일정을 등록한다",
			response = ResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "contents", value = "일정", required = true, dataType = "string", paramType = "query", example = "해야할 목록1")
	})
	public ResponseEntity<Boolean> saveContents(@ModelAttribute("contents") final String contents) {
		
		todoService.saveContents(contents);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		
	}
	
	@PutMapping(path="/todo")
	@ApiOperation(
			httpMethod = "PUT",
			value = "TODO 일정수정",
			notes = "TODO 일정을 수정한다",
			response = ResponseEntity.class
			)
	public ResponseEntity<Boolean> updateContents(@RequestBody TodoTuple todoTuple) {
		
		todoService.updateContents(todoTuple);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		
	}
	
	@DeleteMapping(path="/todo/{id}")
	@ApiOperation(
			httpMethod = "DELETE",
			value = "TODO 일정 삭제",
			notes = "TODO 일정을 삭제한다",
			response = ResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "path", example = "")
	})
	public ResponseEntity<Boolean> deleteContents(@PathVariable("id") final String id) {
		
		todoService.deleteContents(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		
	}
	
	
	@PutMapping(path="/complete/{id}")
	@ApiOperation(
			httpMethod = "PUT",
			value = "TODO 일정완료",
			notes = "TODO 일정을 완료한다",
			response = ResponseEntity.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "path", example = "")
	})
	public ResponseEntity<Boolean> completeTodo(@PathVariable("id") final String id) {
		
		todoService.updateStatus(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		
	}
}
