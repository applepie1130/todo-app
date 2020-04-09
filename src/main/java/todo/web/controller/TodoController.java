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

		return "/main/main";
	}
	
}
