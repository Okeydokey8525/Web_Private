package com.okeydokey.space.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.okeydokey.space.model.Project;

class ProjectServiceTests {

	private ProjectService projectService;

	@BeforeEach
	void setUp() {
		projectService = new ProjectService();
	}

	@Test
	void returnsAllProjectsInCuratedOrder() {
		List<Project> projects = projectService.getAllProjects();

		assertEquals(3, projects.size());
		assertEquals(
				List.of("riceguard-ai", "cafe-nosql", "study-tools-store"),
				projects.stream().map(Project::id).toList());
		assertThrows(UnsupportedOperationException.class, projects::clear);
	}

	@Test
	void returnsOnlyRiceGuardAsFeatured() {
		List<Project> featuredProjects = projectService.getFeaturedProjects();

		assertEquals(1, featuredProjects.size());
		assertEquals("riceguard-ai", featuredProjects.get(0).id());
		assertThrows(UnsupportedOperationException.class, featuredProjects::clear);
	}

	@Test
	void reportsRepositoryAvailabilityWithoutExposingPrivateRepositories() {
		List<Project> projects = projectService.getAllProjects();
		Project riceGuard = projects.get(0);
		Project cafeNoSql = projects.get(1);
		Project studyTools = projects.get(2);

		assertTrue(riceGuard.hasPublicRepository());
		assertEquals("https://github.com/Okeydokey8525/DeepLearning_Lua", riceGuard.repositoryUrl());

		assertTrue(cafeNoSql.privateRepository());
		assertEquals("", cafeNoSql.repositoryUrl());
		assertFalse(cafeNoSql.hasPublicRepository());

		assertTrue(studyTools.hasPublicRepository());
		assertEquals("https://github.com/Okeydokey8525/Java_DungCuHocTap", studyTools.repositoryUrl());
	}

	@Test
	void findsCafeByIdAndReturnsEmptyForAnUnknownProject() {
		Project cafe = projectService.findById("cafe-nosql").orElseThrow();

		assertEquals("CAFE_NOSQL_", cafe.title());
		assertTrue(cafe.privateRepository());
		assertFalse(cafe.hasPublicRepository());
		assertTrue(projectService.findById("missing").isEmpty());
	}

}
