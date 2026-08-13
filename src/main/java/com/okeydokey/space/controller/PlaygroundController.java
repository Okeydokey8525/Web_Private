package com.okeydokey.space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaygroundController {

	@GetMapping("/playground")
	public String playground(Model model) {
		model.addAttribute("currentPage", "playground");
		return "playground";
	}

}
