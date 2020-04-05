package todo.api.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import todo.api.model.tuple.TodoTuple;
import todo.api.model.type.TodoStatus;
import todo.api.repository.TodoRepository;

@Service
public class TodoService {
	
	private TodoRepository todoRepository;
	
	private GenerateSequenceService generateSequenceService;

	@Autowired
	public TodoService(TodoRepository todoRepository, GenerateSequenceService generateSequenceService) {
		super();
		this.todoRepository = todoRepository;
		this.generateSequenceService = generateSequenceService;
	}
	
	public List<TodoTuple> getTodoList() {
		return todoRepository.findAll();
	}
	
	public TodoTuple saveContents(TodoTuple todoTuple) {
		
		todoTuple = TodoTuple.builder()
				.id(generateSequenceService.generateSequence(TodoTuple.SEQUENCE_NAME))
				.contents("집에서 요리하기")
				.insertDate(new Date())
				.updateDate(new Date())
				.status(TodoStatus.ING)
				.build();
		
		return todoRepository.insert(todoTuple);
	}
	
	public TodoTuple updateContents(TodoTuple todoTuple) {
		return todoRepository.save(todoTuple);
	}
	
	public void deleteContents(TodoTuple todoTuple) {
		todoRepository.delete(todoTuple);
	}
}
