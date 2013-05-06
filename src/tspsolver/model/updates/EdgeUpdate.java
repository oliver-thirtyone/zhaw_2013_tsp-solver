package tspsolver.model.updates;

import tspsolver.model.grid.Edge;

public class EdgeUpdate {

	private final Edge edge;
	private final EdgeUpdateAction action;

	public EdgeUpdate(Edge edge, EdgeUpdateAction action) {
		this.edge = edge;
		this.action = action;
	}

	public Edge getEdge() {
		return this.edge;
	}

	public EdgeUpdateAction getAction() {
		return this.action;
	}

}
