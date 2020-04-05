package todo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import todo.api.model.tuple.TodoTuple;

@Repository
public interface TodoRepository extends MongoRepository<TodoTuple, String> {
	
//	public long generateSequence(String seqName) {
//	    DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
//	      new Update().inc("seq",1), options().returnNew(true).upsert(true),
//	      DatabaseSequence.class);
//	    return !Objects.isNull(counter) ? counter.getSeq() : 1;
//	}
}
