package todo.api.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import todo.api.model.tuple.DatabaseSequenceTuple;

/**
 * Auto-Generated Field for MongoDB using Spring Boot
 * 
 * @see https://www.baeldung.com/spring-boot-mongodb-auto-generated-field
 * @author sungjunkim
 */
@Service
public class GenerateSequenceService {

	private MongoOperations mongoOperations;
	
	@Autowired
	public GenerateSequenceService(MongoOperations mongoOperations) {
		super();
		this.mongoOperations = mongoOperations;
	}

	public Long generateSequence(String seqName) {
		DatabaseSequenceTuple counter = mongoOperations.findAndModify(
										query(where("_id").is(seqName)),
										new Update().inc("seq", 1), 
										options().returnNew(true).upsert(true), 
										DatabaseSequenceTuple.class);
		
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
}
