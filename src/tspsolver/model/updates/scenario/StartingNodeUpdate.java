package tspsolver.model.updates.scenario;

import tspsolver.model.scenario.grid.Node;
import tspsolver.model.updates.ElementUpdate;

public class StartingNodeUpdate extends ElementUpdate<Node, StartingNodeUpdateAction> {

	public StartingNodeUpdate(Node element, StartingNodeUpdateAction action) {
		super(element, action);
	}

}
