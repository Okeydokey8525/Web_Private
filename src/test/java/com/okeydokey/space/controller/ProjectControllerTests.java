package com.okeydokey.space.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import com.okeydokey.space.model.Project;
import com.okeydokey.space.service.ProjectService;

class ProjectControllerTests {

	@Test
	void archiveUsesProjectsTemplateAndProvidesAllProjects() {
		ProjectController controller = new ProjectController(new ProjectService());
		ExtendedModelMap model = new ExtendedModelMap();

		String viewName = controller.projects(model);

		assertEquals("projects", viewName);
		Object attribute = model.getAttribute("projects");
		assertNotNull(attribute);
		List<?> projects = (List<?>) attribute;
		assertEquals(3, projects.size());
		assertEquals(
				List.of("riceguard-ai", "cafe-nosql", "study-tools-store"),
				projects.stream().map(Project.class::cast).map(Project::id).toList());
	}

}
