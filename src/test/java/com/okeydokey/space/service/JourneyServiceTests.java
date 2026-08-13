package com.okeydokey.space.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.okeydokey.space.model.JourneyNode;

class JourneyServiceTests {

	private JourneyService journeyService;

	@BeforeEach
	void setUp() {
		journeyService = new JourneyService();
	}

	@Test
	void returnsElevenImmutableNodesInCuratedOrderWithoutDuplicates() {
		List<JourneyNode> nodes = journeyService.getAllNodes();

		assertEquals(11, nodes.size());
		assertEquals(
				List.of(
						"huit-computer-science",
						"start-coding",
						"web-development",
						"database-systems",
						"ai-exploration",
						"building-more",
						"java",
						"flutter",
						"game-dev",
						"ai-computer-vision",
						"whats-next"),
				nodes.stream().map(JourneyNode::id).toList());
		assertEquals(nodes.size(), nodes.stream().map(JourneyNode::id).collect(Collectors.toSet()).size());
		assertEquals(nodes.size(), nodes.stream().map(JourneyNode::order).collect(Collectors.toSet()).size());
		assertThrows(UnsupportedOperationException.class, nodes::clear);
	}

	@Test
	void keepsOnlyConfirmedYears() {
		List<JourneyNode> nodes = journeyService.getAllNodes();
		List<JourneyNode> datedNodes = nodes.stream().filter(JourneyNode::hasYear).toList();

		assertEquals(List.of("huit-computer-science", "start-coding"),
				datedNodes.stream().map(JourneyNode::id).toList());
		assertEquals(List.of("2023", "2024"), datedNodes.stream().map(JourneyNode::year).toList());
		assertTrue(nodes.stream().skip(2).allMatch(node -> node.year().isBlank()));
	}

	@Test
	void derivesCompactBranchNodesFromTheMasterList() {
		List<JourneyNode> branches = journeyService.getBranchNodes();

		assertEquals(3, branches.size());
		assertEquals(List.of("java", "flutter", "game-dev"),
				branches.stream().map(JourneyNode::id).toList());
		assertTrue(branches.stream().allMatch(JourneyNode::isBranch));
		assertTrue(branches.stream().allMatch(JourneyNode::compact));
		assertThrows(UnsupportedOperationException.class, branches::clear);
	}

	@Test
	void derivesCurrentAndNextNodesFromTheMasterList() {
		JourneyNode current = journeyService.getCurrentNode();
		JourneyNode next = journeyService.getNextNode();

		assertEquals("ai-computer-vision", current.id());
		assertEquals("CURRENT", current.kind());
		assertTrue(current.isCurrent());
		assertFalse(current.hasYear());

		assertEquals("whats-next", next.id());
		assertEquals("NEXT", next.kind());
		assertTrue(next.isNext());
		assertFalse(next.hasYear());
	}

	@Test
	void keepsGroupAndParentRelationshipsValidAndAcyclic() {
		List<JourneyNode> nodes = journeyService.getAllNodes();
		Map<String, JourneyNode> nodesById = nodes.stream()
				.collect(Collectors.toMap(JourneyNode::id, Function.identity()));
		JourneyNode root = nodesById.get("huit-computer-science");
		JourneyNode group = nodesById.get("building-more");

		assertNotNull(root);
		assertNull(root.parentId());
		assertTrue(group.isGroup());

		nodes.stream()
				.filter(node -> node.parentId() != null)
				.forEach(node -> assertTrue(nodesById.containsKey(node.parentId())));

		for (JourneyNode node : nodes) {
			Set<String> visited = new HashSet<>();
			JourneyNode cursor = node;

			while (cursor != null) {
				assertTrue(visited.add(cursor.id()), "Journey relationship contains a cycle.");
				cursor = cursor.parentId() == null ? null : nodesById.get(cursor.parentId());
			}
		}
	}

}
