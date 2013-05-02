package tspsolver.model.grid.updates;

import tspsolver.model.grid.Edge;

public class EdgeUpdate {

	private final Edge edge;
	private final UpdateAction action;

	public EdgeUpdate(Edge edge, UpdateAction action) {
		this.edge = edge;
		this.action = action;
	}

	public Edge getEdge() {
		return this.edge;
	}

	public UpdateAction getAction() {
		return this.action;
	}

}
