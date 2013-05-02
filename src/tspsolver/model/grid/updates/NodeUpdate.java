package tspsolver.model.grid.updates;

import tspsolver.model.grid.Node;

public class NodeUpdate {

	private final Node node;
	private final UpdateAction action;

	public NodeUpdate(Node node, UpdateAction action) {
		this.node = node;
		this.action = action;
	}

	public Node getNode() {
		return this.node;
	}

	public UpdateAction getAction() {
		return this.action;
	}

}
