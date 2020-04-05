package todo.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
			response = Map.class
			)
	public ResponseEntity<List<TodoTuple>> getTodoList() {
		
		try {
			// test
			todoService.saveContents(null);
			
			List<TodoTuple> todoList = todoService.getTodoList();
			return new ResponseEntity<List<TodoTuple>>(todoList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<TodoTuple>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@PostMapping(path="/todo")
	@ApiOperation(
			httpMethod = "POST",
			value = "TODO 일정등록",
			notes = "TODO 일정을 등록한다",
			response = Map.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "contents", value = "일정", required = true, dataType = "string", paramType = "query", example = "해야할 목록1")
	})
	public String saveContents(String contents) {
		return "하아하";
	}
	
	@PutMapping(path="/todo")
	@ApiOperation(
			httpMethod = "PUT",
			value = "TODO 일정수정",
			notes = "TODO 일정을 수정한다",
			response = Map.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "query", example = ""),
		@ApiImplicitParam(name = "contents", value = "일정", required = true, dataType = "string", paramType = "query", example = "해야할 목록2")
	})
	public String updateContents(String id, String contents) {
		return "";
	}
	
	@DeleteMapping(path="/todo")
	@ApiOperation(
			httpMethod = "DELETE",
			value = "TODO 일정 삭제",
			notes = "TODO 일정을 삭제한다",
			response = Map.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "query", example = ""),
		@ApiImplicitParam(name = "contents", value = "일정", required = true, dataType = "string", paramType = "query", example = "해야할 목록2")
	})
	public String deleteContents(String id, String contents) {
		return "";
	}
	
	
	@PutMapping(path="/complete")
	@ApiOperation(
			httpMethod = "PUT",
			value = "TODO 일정완료",
			notes = "TODO 일정을 완료한다",
			response = Map.class
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "query", example = ""),
		@ApiImplicitParam(name = "contents", value = "일정", required = true, dataType = "string", paramType = "query", example = "해야할 목록2")
	})
	public String completeTodo(String id, String contents) {
		return "";
	}
}
