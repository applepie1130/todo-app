package todo.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import todo.api.advice.TodoApiException;
import todo.api.model.criteria.SearchCriteria;
import todo.api.model.tuple.ContentsReferTuple;
import todo.api.model.tuple.TodoTuple;
import todo.api.model.type.MessageType;
import todo.api.model.type.StatusType;
import todo.api.repository.TodoRepository;

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
	 * 참조시 자기자신은 참조불가 벨리데이션
	 * 
	 * @param id
	 * @param referIdList
	 * @throws TodoApiException
	 */
	private void validateOfSelfRefer(String id, List<String> referIdList) {
		// 자기자신을 참조할 경우 예외처리
		if (CollectionUtils.isNotEmpty(referIdList)) {
			if ( referIdList.contains(id) ) {
				throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REFER_SELF.getCode()));
			}
		}
	}
	
	/**
	 * 부모일정은 다른 일정의 자식이 될 수 없음
	 * 참조데이터 추가시 유효성 검사
	 * 
	 * @param referedList
	 * @param originReferList
	 * @throws TodoApiException
	 */
	private void validateOfReferer(List<TodoTuple> referedList, List<TodoTuple> originReferList) {
		
		if ( CollectionUtils.isNotEmpty(referedList) ) {
			// 참조ID가 이미 다른 일정의 참조된 일정인지 유효성체크
			List<String> exceptId = null;
			if (CollectionUtils.isNotEmpty(originReferList)) {
				exceptId = originReferList.stream().map(t->t.getId()).collect(Collectors.toList());
			}
			
			for ( TodoTuple t : referedList ) {
				// 추가된 참조ID가 부모가되는 ID인지 유효성체크
				if ( CollectionUtils.isNotEmpty(t.getReferList()) ) {
					for ( TodoTuple s : t.getReferList() ) {
						if (s != null) {
							throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REFER_EXISTREFERID.getCode()));
						}
					}
				}
				
				// 신규로 추가되는 참조ID에 대해서만 유효성검사
				if (exceptId != null) {
					if (!exceptId.contains(t.getId()) && t.getIsRefered() == Boolean.TRUE) {
						throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REFER_ALREADYREFERID.getCode()));	
					}
					continue;
				} else if (t.getIsRefered() == Boolean.TRUE) {
					throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REFER_ALREADYREFERID.getCode()));
				}
			}
		}
	}
	
	/**
	 * 참조할 ID의 일정정보를 조회
	 * 
	 * @param referIdList
	 * @return
	 */
	private List<TodoTuple> getReferTargetTodoList(List<String> referIdList) {
		List<TodoTuple> resultList = null;
		if ( CollectionUtils.isNotEmpty(referIdList) ) {
			// ID정보로 조회
			resultList = todoRepository.findByIdIn(referIdList);
		}
		return resultList;
	}

	/**
	 * Gets search todo list.
	 *
	 * @param searchCriteria the search criteria
	 * @return the search todo list
	 * @throws TodoApiException
	 */
	public Page<TodoTuple> getSearchTodoList(final SearchCriteria searchCriteria) {
		
		if ( searchCriteria == null ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_DEFAULT.getCode()));
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
	 * @throws TodoApiException
	 */
	public void saveContents(final ContentsReferTuple contentsReferTuple) {
		
		if ( contentsReferTuple == null ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_CONTENT.getCode()));
		}
		
		if ( StringUtils.isBlank(contentsReferTuple.getContents()) ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_CONTENT.getCode()));
		}
		
		List<TodoTuple> resultReferIdList = this.getReferTargetTodoList(contentsReferTuple.getReferIdList());
		
		// 참조할 대상 유효성검사
		this.validateOfReferer(resultReferIdList, null);
		
		List<String> referIdList = null;
		if ( CollectionUtils.isNotEmpty(resultReferIdList) ) {
			// 다른 일정의 하위 멤버가 되는 경우 여부값 추가
			resultReferIdList.stream().forEach(t->t.setIsRefered(Boolean.TRUE)); // 참조여부 추가
			
			// 요청받은 ID값이 유효한지는 데이터의 존재유무로 판단하여 존재한 ID값에 대해서만 추가
			referIdList = resultReferIdList.stream().map(t->t.getId()).collect(Collectors.toList());
			todoRepository.saveAll(resultReferIdList);
		}
		
		TodoTuple todoTuple = TodoTuple.builder()
			.id(this.getSequence())
			.insertDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.contents(contentsReferTuple.getContents())
			.referIdList(referIdList)
			.referList(resultReferIdList)
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
	 * @throws TodoApiException
	 */
	public void updateContents(final TodoTuple todoTuple) {
		
		if ( todoTuple == null ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		} 
		if ( StringUtils.isBlank(todoTuple.getContents()) ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_CONTENT.getCode()));
		}
		
		// 자기자신은 참조 불가
		this.validateOfSelfRefer(todoTuple.getId(), todoTuple.getReferIdList());
		
		List<TodoTuple> resultReferIdList = this.getReferTargetTodoList(todoTuple.getReferIdList());
		
		// 참조할 대상 유효성검사
		this.validateOfReferer(resultReferIdList, todoTuple.getReferList());

		// 이전 참조일정의 참조여부 값 초기화
		List<TodoTuple> originReferList = todoTuple.getReferList();
		if ( CollectionUtils.isNotEmpty(originReferList) ) {
			originReferList.stream().forEach(t->t.setIsRefered(Boolean.FALSE)); // 참조여부 추가
			todoRepository.saveAll(originReferList);
		}
		
		List<String> referIdList = null;
		if ( CollectionUtils.isNotEmpty(resultReferIdList) ) {
			// 다른 일정의 하위 멤버가 되는 경우 여부값 추가
			resultReferIdList.stream().forEach(t->t.setIsRefered(Boolean.TRUE)); // 참조여부 추가
			// 요청받은 ID값이 유효한지는 데이터의 존재유무로 판단하여 존재한 ID값에 대해서만 추가
			referIdList = resultReferIdList.stream().map(t->t.getId()).collect(Collectors.toList());
			todoRepository.saveAll(resultReferIdList);
		}
		
		todoTuple.setUpdateDate(LocalDateTime.now());
		todoTuple.setReferIdList(referIdList);
		todoTuple.setReferList(resultReferIdList);
		
		TodoTuple result = todoRepository.save(todoTuple);
		
		if ( result == null ) {
			throw new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_UPDATE_DEFAULT.getCode()));
		}
	}

	/**
	 * Delete contents.
	 *
	 * @param id the id
	 * @throws TodoApiException
	 */
	public void deleteContents(final String id) {
		if ( StringUtils.isBlank(id) ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		}
		
		// 참조ID의 참조여부 상태 변경
		Optional<TodoTuple> findByIdResult = todoRepository.findById(id);
		findByIdResult.filter(Objects::nonNull).orElseThrow(() -> new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_NODATA.getCode())));
		
		if ( findByIdResult.isPresent() ) {
			// 삭제
			todoRepository.deleteById(id);
			
			// 부모 일정이 삭제될 경우 하위 일정 참조여부 해제
			TodoTuple todoTuple = findByIdResult.get();
			List<TodoTuple> referList = todoTuple.getReferList();
			if ( CollectionUtils.isNotEmpty(referList) ) {
				referList.forEach( t-> {
					if (t != null) {
						t.setIsRefered(Boolean.FALSE);
					}
				}); 
				todoRepository.saveAll(referList);
			}
		}
	}

	/**
	 * Update status.
	 *
	 * @param id the id
	 * @throws TodoApiException
	 */
	public void updateStatus(final String id) {
		
		if ( StringUtils.isBlank(id) ) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REQURIED_ID.getCode()));
		}
		
		Optional<TodoTuple> findByIdResult = todoRepository.findById(id);
		findByIdResult.filter(Objects::nonNull).orElseThrow(() -> new TodoApiException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessageType.TODO_ERROR_NODATA.getCode())));
		
		if ( findByIdResult.isPresent() ) {
			TodoTuple todoTuple = findByIdResult.get();
			
			switch (todoTuple.getStatus()) {
			case COMPLETED:
				todoTuple.setStatus(StatusType.ING);
				break;
				
			case ING:
				/**
				 * 참조 일정이 존재 할 경우, 일정 완료시 모든 참조 일정의 상태가 완료상태인 경우만 가능
				 */
				if ( CollectionUtils.isNotEmpty(todoTuple.getReferList()) ) {
					Long count = todoTuple
							.getReferList()
							.stream()
							.filter(Objects::nonNull)
							.filter(t->t.getStatus() == StatusType.ING)
							.count();
					
					if ( count > 0 ) {
						throw new TodoApiException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessageType.TODO_ERROR_REFER_EXISTSNONCOMPLETEDREFERID.getCode()));
					}
				}
				
				todoTuple.setStatus(StatusType.COMPLETED);				
				break;

			default:
				break;
			}
			
			todoRepository.save(todoTuple);
		}
	}
}
