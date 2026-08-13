package com.okeydokey.space.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import com.okeydokey.space.model.Project;
import com.okeydokey.space.service.GardenService;
import com.okeydokey.space.service.ProjectService;

class GardenControllerTests {

	@Test
	void gardenUsesGardenTemplateAndResolvesRelatedProjects() {
		GardenController controller = new GardenController(new GardenService(), new ProjectService());
		ExtendedModelMap model = new ExtendedModelMap();

		String viewName = controller.garden(model);

		assertEquals("garden", viewName);
		Object entriesAttribute = model.getAttribute("gardenEntries");
		Object relatedAttribute = model.getAttribute("relatedProjects");
		assertNotNull(entriesAttribute);
		assertNotNull(relatedAttribute);
		assertEquals(3, ((List<?>) entriesAttribute).size());

		Map<?, ?> relatedProjects = (Map<?, ?>) relatedAttribute;
		assertEquals(1, relatedProjects.size());
		Project cafe = (Project) relatedProjects.get("database-notes");
		assertNotNull(cafe);
		assertEquals("cafe-nosql", cafe.id());
	}

}
