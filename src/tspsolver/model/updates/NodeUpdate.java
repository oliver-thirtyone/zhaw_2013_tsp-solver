package tspsolver.model.updates;

import tspsolver.model.grid.Node;

public class NodeUpdate {

	private final Node node;
	private final NodeUpdateAction action;

	public NodeUpdate(Node node, NodeUpdateAction action) {
		this.node = node;
		this.action = action;
	}

	public Node getNode() {
		return this.node;
	}

	public NodeUpdateAction getAction() {
		return this.action;
	}

}
