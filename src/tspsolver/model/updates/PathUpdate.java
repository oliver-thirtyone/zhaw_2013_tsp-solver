package tspsolver.model.updates;

import tspsolver.model.grid.Edge;

public class PathUpdate {

	private final Edge edge;
	private final PathUpdateAction action;

	public PathUpdate(Edge edge, PathUpdateAction action) {
		this.edge = edge;
		this.action = action;
	}

	public Edge getEdge() {
		return this.edge;
	}

	public PathUpdateAction getAction() {
		return this.action;
	}
}
