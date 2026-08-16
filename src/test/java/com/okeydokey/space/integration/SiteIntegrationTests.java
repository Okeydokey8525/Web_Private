package com.okeydokey.space.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SiteIntegrationTests {

	private static final List<String> ROUTES = List.of(
			"/", "/projects", "/journey", "/garden", "/playground", "/style-guide");
	private static final Pattern ID_PATTERN = Pattern.compile("\\sid=\"([^\"]+)\"");
	private static final Pattern ARIA_REFERENCE_PATTERN = Pattern.compile(
			"aria-(?:controls|labelledby|describedby)=\"([^\"]+)\"");
	private static final Pattern EXTERNAL_LINK_PATTERN = Pattern.compile(
			"<a[^>]*target=\"_blank\"[^>]*>");

	@Autowired
	private MockMvc mockMvc;

	@Test
	void primaryRoutesRenderAndUnimplementedRoutesRemainAbsent() throws Exception {
		for (String route : ROUTES) {
			mockMvc.perform(get(route)).andExpect(status().isOk());
		}

		for (String route : List.of(
				"/contact",
				"/projects/riceguard-ai",
				"/journey/ai-computer-vision",
				"/garden/database-notes",
				"/playground/pixel-brush")) {
			mockMvc.perform(get(route)).andExpect(status().isNotFound());
		}
	}

	@Test
	void defaultProfileDoesNotSendStrictTransportSecurity() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(header().doesNotExist("Strict-Transport-Security"));
	}

	@Test
	void homepagePreservesSectionsHeroProfileProjectsAndContact() throws Exception {
		String html = getPage("/");
		assertInOrder(html, "id=\"home\"", "id=\"about\"", "id=\"projects\"",
				"id=\"journey\"", "id=\"garden\"", "id=\"playground\"", "id=\"contact\"");
		for (String marker : List.of(
				"Student • Developer • Builder",
				"WHO_AM_I_",
				"PRIMARY_STACK_",
				"TOOLS_I_WORK_WITH_",
				"CURRENTLY_EXPLORING_",
				"BUILD_MODE_",
				"RICEGUARD_AI_",
				"CAFE_NOSQL_",
				"STUDY_TOOLS_STORE_",
				"Private_repository_",
				"leducluong.08052005@gmail.com")) {
			assertTrue(html.contains(marker), () -> "Missing Homepage marker: " + marker);
		}
		assertTrue(html.contains("<span class=\"hero__title-prefix\">I'm</span>"));
		assertTrue(html.contains("<span class=\"hero__title-name\">Okeydokey.</span>"));
		assertTrue(html.contains("Explore my work"));
		assertInOrder(html, "<span>Learn</span>", "<span>Build</span>", "<span>Share</span>", "<span>Repeat</span>");
		assertInOrder(html, "LET'S", "CONNECT_");
		assertTrue(html.contains("href=\"https://github.com/Okeydokey8525\""));
		assertEquals(2, count(html, "/images/profile/okeydokey-profile.jpg"));
		assertEquals(1, count(html, "alt=\"Portrait of Okeydokey\""));
		assertEquals(1, count(html, "alt=\"\" aria-hidden=\"true\""));
		assertFalse(html.contains("PROFILE_IMAGE_PENDING_"));
	}

	@Test
	void projectArchivePreservesOrderFiltersPublicLinksAndCafePrivacy() throws Exception {
		String html = getPage("/projects");
		assertTrue(html.contains("[ PROJECT_ARCHIVE ]"));
		assertInOrder(html, "Things", "I've built.");
		assertEquals(3, count(html, "data-project-card"));
		assertInOrder(html, "project-riceguard-ai", "project-cafe-nosql", "project-study-tools-store");

		List<String> filters = extract(html, "data-project-filter=\"([^\"]+)\"");
		assertEquals(List.of("ALL", "WEB", "AI_ML", "DATABASE", "MOBILE", "GAME"), filters);
		assertTrue(html.contains("data-project-filter=\"ALL\" aria-pressed=\"true\""));
		assertEquals(5, count(html, "aria-pressed=\"false\""));

		assertEquals(1, count(html, "https://github.com/Okeydokey8525/DeepLearning_Lua"));
		assertEquals(1, count(html, "https://github.com/Okeydokey8525/Java_DungCuHocTap"));
		String cafeCard = html.substring(
				html.indexOf("id=\"project-cafe-nosql\""),
				html.indexOf("id=\"project-study-tools-store\""));
		assertTrue(cafeCard.contains("Private_repository_"));
		assertFalse(cafeCard.contains("View_repository"));
		assertFalse(cafeCard.contains("href=\"\""));
	}

	@Test
	void journeyPreservesCuratedProgressionDatesAndSemantics() throws Exception {
		String html = getPage("/journey");
		List<String> nodeIds = extract(html, "data-node-id=\"([^\"]+)\"");
		assertEquals(List.of(
				"huit-computer-science", "start-coding", "web-development", "database-systems",
				"ai-exploration", "building-more", "java", "flutter", "game-dev",
				"ai-computer-vision", "whats-next"), nodeIds);
		assertEquals(List.of("2023", "2024"),
				extract(html, "class=\"journey-node__year pixel-label\"[^>]*>\\s*([^<]+)"));
		assertTrue(html.contains("<ol class=\"journey-map__list\""));
		assertEquals(1, count(html, "aria-current=\"step\""));
		assertTrue(html.contains("CURRENT"));
		assertTrue(html.contains("NEXT"));
		assertInOrder(html, "JAVA_", "FLUTTER_", "GAME_DEV_", "AI / COMPUTER_VISION_", "WHAT&#39;S_NEXT?_");
	}

	@Test
	void gardenPreservesEntriesStatusesAndInternalCafeRelationship() throws Exception {
		String html = getPage("/garden");
		assertEquals(List.of("ai-computer-vision", "database-notes", "web-ui-experiments"),
				extract(html, "data-garden-id=\"([^\"]+)\""));
		assertEquals(List.of("GROWING", "GROWING", "SEED"),
				extract(html, "data-garden-status=\"([^\"]+)\""));
		assertEquals(1, count(html, "garden-card--featured"));
		assertTrue(html.contains("href=\"/projects#project-cafe-nosql\""));
		assertTrue(html.contains("CAFE_NOSQL_"));
		assertFalse(html.contains("DeepLearning_Lua"));
		assertFalse(html.contains("Java_DungCuHocTap"));
	}

	@Test
	void playgroundPreservesExactlyTwoAccessibleEventDrivenLabs() throws Exception {
		String html = getPage("/playground");
		assertEquals(2, count(html, "class=\"playground-lab "));
		assertTrue(html.contains("LAB_001"));
		assertTrue(html.contains("PIXEL_BRUSH_"));
		assertTrue(html.contains("LAB_002"));
		assertTrue(html.contains("DITHER_MACHINE_"));
		assertFalse(html.contains("LAB_003"));
		assertTrue(html.contains("<canvas"));
		assertTrue(html.contains("aria-label=\"Pixel Brush drawing area\""));
		assertTrue(html.contains("DRAG_TO_DRAW_"));
		assertTrue(html.contains("data-pixel-brush-clear"));
		assertEquals(2, count(html, "type=\"range\""));
		assertEquals(2, count(html, "<output"));
		assertFalse(html.contains("type=\"file\""));
	}

	@Test
	void allPagesPreserveCoreAccessibilityAndExternalLinkSecurity() throws Exception {
		for (String route : ROUTES) {
			String html = getPage(route);
			assertTrue(html.contains("<html lang=\"en\""), route);
			assertTrue(html.contains("name=\"viewport\" content=\"width=device-width, initial-scale=1.0\""), route);
			assertEquals(1, count(html, "<title>"), route);
			assertEquals(1, count(html, "<main "), route);
			assertEquals(1, count(html, "<h1 "), route);
			assertEquals(1, count(html, "id=\"main-content\""), route);
			assertEquals(1, count(html, "class=\"skip-link\" href=\"#main-content\""), route);
			assertTrue(html.contains("aria-label=\"Primary navigation\""), route);
			assertTrue(html.contains("aria-label=\"Footer navigation\""), route);
			assertFalse(Pattern.compile("tabindex=\"[1-9][0-9]*\"").matcher(html).find(), route);
			assertReferencesAndIdsAreValid(html, route);

			for (String tag : extract(html, "(<a[^>]*target=\"_blank\"[^>]*>)")) {
				assertTrue(tag.contains("rel=\"noopener noreferrer\""), route + ": " + tag);
			}
		}
	}

	private String getPage(String route) throws Exception {
		return mockMvc.perform(get(route))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
	}

	private void assertReferencesAndIdsAreValid(String html, String route) {
		List<String> ids = extract(html, ID_PATTERN);
		assertEquals(ids.size(), new HashSet<>(ids).size(), route + " has duplicate IDs");
		Set<String> idSet = Set.copyOf(ids);
		Matcher references = ARIA_REFERENCE_PATTERN.matcher(html);
		while (references.find()) {
			for (String id : references.group(1).split("\\s+")) {
				assertTrue(idSet.contains(id), route + " has dangling ARIA reference: " + id);
			}
		}
	}

	private static void assertInOrder(String value, String... markers) {
		int previous = -1;
		for (String marker : markers) {
			int position = value.indexOf(marker);
			assertTrue(position > previous, () -> "Missing or out-of-order marker: " + marker);
			previous = position;
		}
	}

	private static int count(String value, String marker) {
		return value.split(Pattern.quote(marker), -1).length - 1;
	}

	private static List<String> extract(String value, String expression) {
		return extract(value, Pattern.compile(expression));
	}

	private static List<String> extract(String value, Pattern pattern) {
		Matcher matcher = pattern.matcher(value);
		java.util.ArrayList<String> matches = new java.util.ArrayList<>();
		while (matcher.find()) {
			matches.add(matcher.group(1).trim());
		}
		return List.copyOf(matches);
	}

}
