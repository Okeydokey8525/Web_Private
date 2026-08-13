package com.okeydokey.space.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.okeydokey.space.model.GardenEntry;
import com.okeydokey.space.model.Project;
import com.okeydokey.space.service.GardenService;
import com.okeydokey.space.service.ProjectService;

@Controller
public class GardenController {

	private final GardenService gardenService;
	private final ProjectService projectService;

	public GardenController(GardenService gardenService, ProjectService projectService) {
		this.gardenService = gardenService;
		this.projectService = projectService;
	}

	@GetMapping("/garden")
	public String garden(Model model) {
		model.addAttribute("currentPage", "garden");
		List<GardenEntry> gardenEntries = gardenService.getAllEntries();
		model.addAttribute("gardenEntries", gardenEntries);
		model.addAttribute("relatedProjects", resolveRelatedProjects(gardenEntries));
		return "garden";
	}

	private Map<String, Project> resolveRelatedProjects(List<GardenEntry> entries) {
		return entries.stream()
				.filter(GardenEntry::hasRelatedProject)
				.flatMap(entry -> projectService.findById(entry.relatedProjectId())
						.map(project -> Map.entry(entry.id(), project))
						.stream())
				.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
