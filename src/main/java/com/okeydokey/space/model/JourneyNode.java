package com.okeydokey.space.model;

public record JourneyNode(
		String id,
		String title,
		String year,
		String kind,
		String parentId,
		int order,
		boolean compact) {

	public boolean hasYear() {
		return year != null && !year.isBlank();
	}

	public boolean isCurrent() {
		return "CURRENT".equals(kind);
	}

	public boolean isNext() {
		return "NEXT".equals(kind);
	}

	public boolean isBranch() {
		return "BRANCH".equals(kind);
	}

	public boolean isGroup() {
		return "GROUP".equals(kind);
	}

}
