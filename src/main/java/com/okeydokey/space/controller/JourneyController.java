package com.okeydokey.space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.okeydokey.space.service.JourneyService;

@Controller
public class JourneyController {

	private final JourneyService journeyService;

	public JourneyController(JourneyService journeyService) {
		this.journeyService = journeyService;
	}

	@GetMapping("/journey")
	public String journey(Model model) {
		model.addAttribute("currentPage", "journey");
		model.addAttribute("journeyNodes", journeyService.getAllNodes());
		return "journey";
	}

}
