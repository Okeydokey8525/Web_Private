package com.okeydokey.space.model;

import java.util.List;

public record GardenEntry(
		String id,
		String title,
		String status,
		List<String> tags,
		String excerpt,
		List<String> topics,
		String relatedProjectId,
		String lastWatered,
		boolean featured) {

	public GardenEntry {
		tags = List.copyOf(tags);
		topics = List.copyOf(topics);
	}

	public boolean hasRelatedProject() {
		return relatedProjectId != null && !relatedProjectId.isBlank();
	}

	public boolean isGrowing() {
		return "GROWING".equals(status);
	}

	public boolean isSeed() {
		return "SEED".equals(status);
	}

}
