package com.okeydokey.space.model;

import java.util.List;

public record Project(
		String id,
		String title,
		String description,
		String role,
		String focus,
		List<String> technologies,
		List<String> categories,
		String status,
		String repositoryUrl,
		boolean privateRepository,
		boolean featured) {

	public Project {
		technologies = List.copyOf(technologies);
		categories = List.copyOf(categories);
	}

	public boolean hasPublicRepository() {
		return !privateRepository && repositoryUrl != null && !repositoryUrl.isBlank();
	}

}
