package com.okeydokey.space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.okeydokey.space.service.ProjectService;

@Controller
public class ProjectController {

	private final ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@GetMapping("/projects")
	public String projects(Model model) {
		model.addAttribute("currentPage", "projects");
		model.addAttribute("projects", projectService.getAllProjects());
		return "projects";
	}

}
