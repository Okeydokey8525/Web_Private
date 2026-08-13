package com.okeydokey.space.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.okeydokey.space.model.Project;

@Service
public class ProjectService {

	private static final List<Project> PROJECTS = List.of(
			new Project(
					"riceguard-ai",
					"RICEGUARD_AI_",
					"An AI-powered system for analyzing rice leaf conditions from images, with diagnosis, history tracking and a disease knowledge library.",
					"AI / FULL-STACK",
					"COMPUTER VISION / MOBILE / WEB",
					List.of("Python", "Java", "Flutter", "FastAPI", "Spring Boot", "MySQL"),
					List.of("AI_ML", "WEB", "MOBILE"),
					"IN DEVELOPMENT",
					"https://github.com/Okeydokey8525/DeepLearning_Lua",
					false,
					true),
			new Project(
					"cafe-nosql",
					"CAFE_NOSQL_",
					"A Spring Boot coffee-shop platform exploring how relational and NoSQL databases can work together inside a real application.",
					"BACKEND / DATABASE",
					"POLYGLOT PERSISTENCE / E-COMMERCE",
					List.of("Java", "Spring Boot", "MySQL", "MongoDB", "Redis", "Docker"),
					List.of("WEB", "DATABASE"),
					"ACTIVE",
					"",
					true,
					false),
			new Project(
					"study-tools-store",
					"STUDY_TOOLS_STORE_",
					"A Java web application for browsing and purchasing school and study supplies, with authentication, checkout and administration features.",
					"TEAM LEADER + BACKEND / DATABASE / SECURITY",
					"WEB / E-COMMERCE",
					List.of("Java", "Spring Boot", "Spring Security", "JPA", "MySQL", "Thymeleaf"),
					List.of("WEB", "DATABASE"),
					"COMPLETE",
					"https://github.com/Okeydokey8525/Java_DungCuHocTap",
					false,
					false));

	public List<Project> getAllProjects() {
		return PROJECTS;
	}

	public List<Project> getFeaturedProjects() {
		return PROJECTS.stream()
				.filter(Project::featured)
				.toList();
	}

	public Optional<Project> findById(String id) {
		return PROJECTS.stream()
				.filter(project -> project.id().equals(id))
				.findFirst();
	}

}
