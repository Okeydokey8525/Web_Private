package com.okeydokey.space.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.okeydokey.space.model.JourneyNode;

@Service
public class JourneyService {

	private static final List<JourneyNode> JOURNEY_NODES = List.of(
			new JourneyNode(
					"huit-computer-science",
					"HUIT / COMPUTER_SCIENCE_",
					"2023",
					"MILESTONE",
					null,
					1,
					false),
			new JourneyNode(
					"start-coding",
					"START_CODING_",
					"2024",
					"MILESTONE",
					"huit-computer-science",
					2,
					false),
			new JourneyNode(
					"web-development",
					"WEB_DEVELOPMENT_",
					"",
					"MILESTONE",
					"start-coding",
					3,
					false),
			new JourneyNode(
					"database-systems",
					"DATABASE_SYSTEMS_",
					"",
					"MILESTONE",
					"web-development",
					4,
					false),
			new JourneyNode(
					"ai-exploration",
					"AI_EXPLORATION_",
					"",
					"MILESTONE",
					"database-systems",
					5,
					false),
			new JourneyNode(
					"building-more",
					"BUILDING_MORE_",
					"",
					"GROUP",
					"ai-exploration",
					6,
					false),
			new JourneyNode(
					"java",
					"JAVA_",
					"",
					"BRANCH",
					"building-more",
					7,
					true),
			new JourneyNode(
					"flutter",
					"FLUTTER_",
					"",
					"BRANCH",
					"building-more",
					8,
					true),
			new JourneyNode(
					"game-dev",
					"GAME_DEV_",
					"",
					"BRANCH",
					"building-more",
					9,
					true),
			new JourneyNode(
					"ai-computer-vision",
					"AI / COMPUTER_VISION_",
					"",
					"CURRENT",
					"building-more",
					10,
					false),
			new JourneyNode(
					"whats-next",
					"WHAT'S_NEXT?_",
					"",
					"NEXT",
					"ai-computer-vision",
					11,
					false));

	public List<JourneyNode> getAllNodes() {
		return JOURNEY_NODES;
	}

	public List<JourneyNode> getBranchNodes() {
		return JOURNEY_NODES.stream()
				.filter(JourneyNode::isBranch)
				.toList();
	}

	public JourneyNode getCurrentNode() {
		return JOURNEY_NODES.stream()
				.filter(JourneyNode::isCurrent)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Current journey node is missing."));
	}

	public JourneyNode getNextNode() {
		return JOURNEY_NODES.stream()
				.filter(JourneyNode::isNext)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Next journey node is missing."));
	}

}
