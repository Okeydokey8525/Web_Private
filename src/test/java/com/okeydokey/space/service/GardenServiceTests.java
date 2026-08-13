package com.okeydokey.space.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.okeydokey.space.model.GardenEntry;

class GardenServiceTests {

	private GardenService gardenService;

	@BeforeEach
	void setUp() {
		gardenService = new GardenService();
	}

	@Test
	void returnsThreeImmutableEntriesInCuratedOrderWithoutDuplicateIds() {
		List<GardenEntry> entries = gardenService.getAllEntries();

		assertEquals(3, entries.size());
		assertEquals(
				List.of("ai-computer-vision", "database-notes", "web-ui-experiments"),
				entries.stream().map(GardenEntry::id).toList());
		assertEquals(entries.size(), new HashSet<>(entries.stream().map(GardenEntry::id).toList()).size());
		assertThrows(UnsupportedOperationException.class, entries::clear);
	}

	@Test
	void keepsTheExactCuratedEntryMetadata() {
		List<GardenEntry> entries = gardenService.getAllEntries();
		GardenEntry ai = entries.get(0);
		GardenEntry database = entries.get(1);
		GardenEntry webUi = entries.get(2);

		assertEquals("AI / COMPUTER_VISION_", ai.title());
		assertEquals("GROWING", ai.status());
		assertEquals(List.of("AI", "Computer Vision", "Deep Learning"), ai.tags());
		assertEquals(
				"Notes and experiments around image segmentation, dataset preparation, model training and evaluation for computer vision.",
				ai.excerpt());
		assertEquals(
				List.of("Segmentation", "Dataset Preparation", "Model Training", "Dice / IoU / mAP", "Computer Vision Experiments"),
				ai.topics());
		assertEquals("", ai.relatedProjectId());
		assertTrue(ai.featured());

		assertEquals("DATABASE_NOTES_", database.title());
		assertEquals("GROWING", database.status());
		assertEquals(List.of("SQL", "NoSQL", "Database"), database.tags());
		assertEquals(List.of("MySQL", "MongoDB", "Redis", "Data Modeling", "Polyglot Persistence"), database.topics());
		assertEquals("cafe-nosql", database.relatedProjectId());
		assertFalse(database.featured());

		assertEquals("WEB_UI_EXPERIMENTS_", webUi.title());
		assertEquals("SEED", webUi.status());
		assertEquals(List.of("Web", "UI/UX", "Experiment"), webUi.tags());
		assertEquals(List.of("Pixel UI", "Bento Grid", "Interaction", "Motion", "Dithering", "Responsive Design"), webUi.topics());
		assertEquals("", webUi.relatedProjectId());
		assertFalse(webUi.featured());

		assertTrue(entries.stream().allMatch(entry -> "RECENTLY_".equals(entry.lastWatered())));
	}

	@Test
	void derivesFeaturedAndGrowingEntriesFromTheMasterList() {
		List<GardenEntry> featured = gardenService.getFeaturedEntries();
		List<GardenEntry> growing = gardenService.getGrowingEntries();

		assertEquals(1, featured.size());
		assertEquals("ai-computer-vision", featured.get(0).id());
		assertThrows(UnsupportedOperationException.class, featured::clear);

		assertEquals(2, growing.size());
		assertEquals(
				List.of("ai-computer-vision", "database-notes"),
				growing.stream().map(GardenEntry::id).toList());
		assertThrows(UnsupportedOperationException.class, growing::clear);
	}

	@Test
	void reportsStatusAndRelatedProjectFromTheirSourceFields() {
		List<GardenEntry> entries = gardenService.getAllEntries();
		GardenEntry ai = entries.get(0);
		GardenEntry database = entries.get(1);
		GardenEntry webUi = entries.get(2);

		assertTrue(ai.isGrowing());
		assertFalse(ai.isSeed());
		assertFalse(ai.hasRelatedProject());

		assertTrue(database.isGrowing());
		assertFalse(database.isSeed());
		assertTrue(database.hasRelatedProject());

		assertFalse(webUi.isGrowing());
		assertTrue(webUi.isSeed());
		assertFalse(webUi.hasRelatedProject());
	}

	@Test
	void defensivelyCopiesTagsAndTopicsAndKeepsThemImmutable() {
		List<String> mutableTags = new ArrayList<>(List.of("Tag"));
		List<String> mutableTopics = new ArrayList<>(List.of("Topic"));
		GardenEntry entry = new GardenEntry(
				"test-entry",
				"TEST_ENTRY_",
				"SEED",
				mutableTags,
				"Test excerpt.",
				mutableTopics,
				"",
				"RECENTLY_",
				false);

		mutableTags.add("Changed");
		mutableTopics.add("Changed");

		assertEquals(List.of("Tag"), entry.tags());
		assertEquals(List.of("Topic"), entry.topics());
		assertThrows(UnsupportedOperationException.class, () -> entry.tags().add("Changed"));
		assertThrows(UnsupportedOperationException.class, () -> entry.topics().add("Changed"));
	}

}
