package tspsolver.model.updates;

import tspsolver.model.grid.Node;

public class StartingNodeUpdate {

	private final Node node;
	private final StartingNodeUpdateAction action;

	public StartingNodeUpdate(Node node, StartingNodeUpdateAction action) {
		this.node = node;
		this.action = action;
	}

	public Node getNode() {
		return this.node;
	}

	public StartingNodeUpdateAction getAction() {
		return this.action;
	}

}
