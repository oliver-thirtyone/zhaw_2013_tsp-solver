package tspsolver.model.updates.grid;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.updates.ElementUpdate;

public class EdgeUpdate extends ElementUpdate<Edge, EdgeUpdateAction> {

	public EdgeUpdate(Edge element, EdgeUpdateAction action) {
		super(element, action);
	}

}
