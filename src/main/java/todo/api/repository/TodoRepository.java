package todo.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import todo.api.model.tuple.TodoTuple;

/**
 * The interface Todo repository.
 */
@Repository
public interface TodoRepository extends PagingAndSortingRepository<TodoTuple, String>, MongoRepository<TodoTuple, String> {

	/**
	 * 검색키워드, 페이징 번호로 데이터 like검색 
	 * <p>
	 * like검색조건은, '%keyword%' full like
	 * 
	 * @param keyword
	 * @param pageable
	 * @return
	 */
    Page<TodoTuple> findByContentsLike(String keyword, Pageable pageable);

}
