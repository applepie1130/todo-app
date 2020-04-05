package todo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class TodoController {

	@GetMapping("/")
	public String mainPage(Model model) {
		model.addAttribute("pageTitle", "Todo");
		model.addAttribute("title", "Todo List");

//		// given
//		DBObject objectToSave = BasicDBObjectBuilder.start()
//				.add("key", "value")
//				.get();
//		// when
//		mongoTemplate.save(objectToSave, "collection");
//		
//		List<DBObject> findAll = mongoTemplate.findAll(DBObject.class, "collection");
//		log.debug(findAll);

		return "/main/main";
	}
}
