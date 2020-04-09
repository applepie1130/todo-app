package todo.api.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import todo.api.advice.TodoApiException;
import todo.api.model.criteria.SearchCriteria;
import todo.api.model.tuple.TodoTuple;
import todo.api.model.type.MessageType;
import todo.api.model.type.StatusType;
import todo.api.repository.TodoRepository;

@Service
public class TodoService {
	
	private final TodoRepository todoRepository;
	private final GenerateSequenceService generateSequenceService;
	private final MessageService messageService;
	
	public TodoService(TodoRepository todoRepository, GenerateSequenceService generateSequenceService, MessageService messageService) {
		super();
		this.todoRepository = todoRepository;
		this.generateSequenceService = generateSequenceService;
		this.messageService = messageService;
	}

	private String getSequence() {
		return generateSequenceService.generateSequence(TodoTuple.SEQUENCE_NAME).toString();
	}
	
	public Page<TodoTuple> getTodoList() {
		// 최근수정일자로 descending
		int page = 0;
		int pageSize = 5;
		Pageable pageableRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updateDate");
		Page<TodoTuple> findAll = todoRepository.findAll(pageableRequest);
		
		return findAll;
	}
	
	public Page<TodoTuple> getSearchTodoList(SearchCriteria searchCriteria) {
		
		if ( searchCriteria == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_DEFAULT.getCode()));
		} else if (StringUtils.isBlank(searchCriteria.getKeyword())) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_KEYWORD.getCode()));
		}
		
		// 최근수정일자로 descending
		int page = searchCriteria.getPage();
		int pageSize = 5;
		Pageable pageableRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updateDate");
		Page<TodoTuple> findAll = todoRepository.findAll(pageableRequest);
		
		return findAll;
	}
	
	public void saveContents(final String contents) {
		
		if ( StringUtils.isBlank(contents) ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_CONTENT.getCode()));
		}
		
		TodoTuple todoTuple = TodoTuple.builder()
			.id(this.getSequence())
			.insertDate(new Date())
			.updateDate(new Date())
			.contents(contents)
			.status(StatusType.ING)
			.build();
		
		TodoTuple result = todoRepository.insert(todoTuple);
		
		if ( result == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_SAVE.getCode()));	
		}
	}
	
	public void updateContents(final TodoTuple todoTuple) {
		
		if ( todoTuple == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		}
		
		todoTuple.setUpdateDate(new Date());
		
		TodoTuple result = todoRepository.save(todoTuple);
		
		if ( result == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_UPDATE.getCode()));
		}
	}
	
	public void deleteContents(final String id) {
		if ( StringUtils.isBlank(id) ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		}
		
		todoRepository.deleteById(id);
	}
	

	public void updateStatus(final String id) {
		
		if ( StringUtils.isBlank(id) ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		}
		
		Optional<TodoTuple> findByIdResult = todoRepository.findById(id);
		findByIdResult.filter(Objects::nonNull).orElseThrow(() -> new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_DEFAULT.getCode())));
		
		if ( findByIdResult.isPresent() ) {
			TodoTuple todoTuple = findByIdResult.get();
			switch (todoTuple.getStatus()) {
			case COMPLETED:
				todoTuple.setStatus(StatusType.ING);
				break;
				
			case ING:
				todoTuple.setStatus(StatusType.COMPLETED);				
				break;

			default:
				break;
			}
			
			todoRepository.save(todoTuple);
			
		} else {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_NODATA.getCode()));
		}
	}
}
