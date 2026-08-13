package com.okeydokey.space.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.okeydokey.space.model.GardenEntry;

@Service
public class GardenService {

	private static final List<GardenEntry> GARDEN_ENTRIES = List.of(
			new GardenEntry(
					"ai-computer-vision",
					"AI / COMPUTER_VISION_",
					"GROWING",
					List.of("AI", "Computer Vision", "Deep Learning"),
					"Notes and experiments around image segmentation, dataset preparation, model training and evaluation for computer vision.",
					List.of(
							"Segmentation",
							"Dataset Preparation",
							"Model Training",
							"Dice / IoU / mAP",
							"Computer Vision Experiments"),
					"",
					"RECENTLY_",
					true),
			new GardenEntry(
					"database-notes",
					"DATABASE_NOTES_",
					"GROWING",
					List.of("SQL", "NoSQL", "Database"),
					"Notes on relational and NoSQL databases, data modeling and combining different storage technologies inside one application.",
					List.of("MySQL", "MongoDB", "Redis", "Data Modeling", "Polyglot Persistence"),
					"cafe-nosql",
					"RECENTLY_",
					false),
			new GardenEntry(
					"web-ui-experiments",
					"WEB_UI_EXPERIMENTS_",
					"SEED",
					List.of("Web", "UI/UX", "Experiment"),
					"A collection of interface ideas and experiments around pixel UI, Bento layouts, interaction and responsive design.",
					List.of("Pixel UI", "Bento Grid", "Interaction", "Motion", "Dithering", "Responsive Design"),
					"",
					"RECENTLY_",
					false));

	public List<GardenEntry> getAllEntries() {
		return GARDEN_ENTRIES;
	}

	public List<GardenEntry> getFeaturedEntries() {
		return GARDEN_ENTRIES.stream()
				.filter(GardenEntry::featured)
				.toList();
	}

	public List<GardenEntry> getGrowingEntries() {
		return GARDEN_ENTRIES.stream()
				.filter(GardenEntry::isGrowing)
				.toList();
	}

}
