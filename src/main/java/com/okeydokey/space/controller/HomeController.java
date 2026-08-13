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
import com.okeydokey.space.service.JourneyService;
import com.okeydokey.space.service.ProjectService;

@Controller
public class HomeController {
	private final ProjectService projectService;
	private final JourneyService journeyService;
	private final GardenService gardenService;

	public HomeController(
			ProjectService projectService,
			JourneyService journeyService,
			GardenService gardenService) {
		this.projectService = projectService;
		this.journeyService = journeyService;
		this.gardenService = gardenService;
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("currentPage", "home");
		List<GardenEntry> gardenEntries = gardenService.getAllEntries();
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("journeyNodes", journeyService.getAllNodes());
		model.addAttribute("gardenEntries", gardenEntries);
		model.addAttribute("gardenRelatedProjects", resolveRelatedProjects(gardenEntries));
		return "index";
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
