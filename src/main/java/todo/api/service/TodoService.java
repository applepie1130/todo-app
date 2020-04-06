package todo.api.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
	
	private String getSequence() {
		return generateSequenceService.generateSequence(TodoTuple.SEQUENCE_NAME).toString();
	}
	
	public List<TodoTuple> getTodoList() {
		return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "updateDate"));
	}
	
	public void saveContents(String contents) {
		
		if ( StringUtils.isBlank(contents) ) {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "내용을 입려해 주세요.");
		}
		
		TodoTuple todoTuple = TodoTuple.builder()
			.id(this.getSequence())
			.insertDate(new Date())
			.updateDate(new Date())
			.contents(contents)
			.status(TodoStatus.ING)
			.build();
		
		TodoTuple result = todoRepository.insert(todoTuple);
		
		if ( result == null ) {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "저장 중 오류가 발생했습니다.");	
		}
	}
	
	public void updateContents(TodoTuple todoTuple) {
		
		if ( todoTuple == null ) {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "ID값은 필수 입니다.");
		}
		
		todoTuple.setUpdateDate(new Date());
		
		TodoTuple result = todoRepository.save(todoTuple);
		
		if ( result == null ) {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "수정 중 오류가 발생했습니다.");
		}
	}
	
	public void deleteContents(String id) {
		if ( StringUtils.isBlank(id) ) {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "ID값은 필수 입니다.");
		}
		
		todoRepository.deleteById(id);
	}
	

	public void updateStatus(String id) {
		
		if ( StringUtils.isBlank(id) ) {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "ID값은 필수 입니다.");
		}
		
		Optional<TodoTuple> findByIdResult = todoRepository.findById(id);
//		findByIdResult.filter(Objects::nonNull).orElseThrow(() -> new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생했습니다.") )
		
		if ( findByIdResult.isPresent() ) {
			TodoTuple todoTuple = findByIdResult.get();
			switch (todoTuple.getStatus()) {
			case COMPLETED:
				todoTuple.setStatus(TodoStatus.ING);
				break;
				
			case ING:
				todoTuple.setStatus(TodoStatus.COMPLETED);				
				break;

			default:
				break;
			}
			
			todoRepository.save(todoTuple);
			
		} else {
//			new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터를 찾을 수 없습니다.");
		}
	}
}
