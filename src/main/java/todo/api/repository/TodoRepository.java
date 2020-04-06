package todo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import todo.api.model.tuple.TodoTuple;

@Repository
public interface TodoRepository extends MongoRepository<TodoTuple, String> {
}
