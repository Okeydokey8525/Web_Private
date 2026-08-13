package com.okeydokey.space.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

class PlaygroundControllerTests {

	@Test
	void playgroundUsesPlaygroundTemplate() {
		PlaygroundController controller = new PlaygroundController();
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("playground", controller.playground(model));
		assertEquals("playground", model.getAttribute("currentPage"));
	}

}
