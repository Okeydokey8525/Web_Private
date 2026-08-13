package com.okeydokey.space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StyleGuideController {

	@GetMapping("/style-guide")
	public String styleGuide() {
		return "style-guide";
	}

}
