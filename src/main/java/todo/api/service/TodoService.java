package todo.api.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Todo service.
 */
@Log4j2
@Service
public class TodoService {
	
	private final TodoRepository todoRepository;
	private final GenerateSequenceService generateSequenceService;
	private final MessageService messageService;

	/**
	 * Instantiates a new Todo service.
	 *
	 * @param todoRepository          the todo repository
	 * @param generateSequenceService the generate sequence service
	 * @param messageService          the message service
	 */
	@Autowired
	public TodoService(TodoRepository todoRepository, GenerateSequenceService generateSequenceService, MessageService messageService) {
		super();
		this.todoRepository = todoRepository;
		this.generateSequenceService = generateSequenceService;
		this.messageService = messageService;
	}

	private String getSequence() {
		return generateSequenceService.generateSequence(TodoTuple.SEQUENCE_NAME).toString();
	}

	/**
	 * Gets search todo list.
	 *
	 * @param searchCriteria the search criteria
	 * @return the search todo list
	 */
	public Page<TodoTuple> getSearchTodoList(final SearchCriteria searchCriteria) {
		
		if ( searchCriteria == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_DEFAULT.getCode()));
		}
		
		// 최근수정일자로 descending
		int page = searchCriteria.getPage();
		int pageSize = 5;
		Pageable pageableRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updateDate");
		Page<TodoTuple> result = todoRepository.findByContentsLike(searchCriteria.getKeyword(), pageableRequest);
		return result;
	}

	/**
	 * Save contents.
	 *
	 * @param contents the contents
	 */
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

	/**
	 * Update contents.
	 *
	 * @param todoTuple the todo tuple
	 */
	public void updateContents(final TodoTuple todoTuple) {
		
		if ( todoTuple == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		} 
		if ( StringUtils.isBlank(todoTuple.getContents()) ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_CONTENT.getCode()));
		}
		
		todoTuple.setUpdateDate(new Date());
		
		TodoTuple result = todoRepository.save(todoTuple);
		
		if ( result == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_UPDATE.getCode()));
		}
	}

	/**
	 * Delete contents.
	 *
	 * @param id the id
	 */
	public void deleteContents(final String id) {
		if ( StringUtils.isBlank(id) ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		}
		
		todoRepository.deleteById(id);
	}


	/**
	 * Update status.
	 *
	 * @param id the id
	 */
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
