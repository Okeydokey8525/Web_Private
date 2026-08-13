package com.okeydokey.space.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import com.okeydokey.space.service.JourneyService;

class JourneyControllerTests {

	@Test
	void journeyUsesJourneyTemplateAndProvidesCuratedMapData() {
		JourneyController controller = new JourneyController(new JourneyService());
		ExtendedModelMap model = new ExtendedModelMap();

		String viewName = controller.journey(model);

		assertEquals("journey", viewName);
		Object nodesAttribute = model.getAttribute("journeyNodes");
		assertNotNull(nodesAttribute);
		assertEquals(11, ((List<?>) nodesAttribute).size());
	}

}
