package tspsolver.model.updates.grid;

import tspsolver.model.scenario.grid.Node;
import tspsolver.model.updates.ElementUpdate;

public class NodeUpdate extends ElementUpdate<Node, NodeUpdateAction> {

	public NodeUpdate(Node element, NodeUpdateAction action) {
		super(element, action);
	}

}
