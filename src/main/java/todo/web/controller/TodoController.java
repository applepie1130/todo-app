package todo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoController {
	
	@GetMapping("/")
	public String mainPage(Model model) {
		model.addAttribute("pageTitle", "TODO");
		model.addAttribute("message", "Hello World");
		
		return "main";
	}
}
