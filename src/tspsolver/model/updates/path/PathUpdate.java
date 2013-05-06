package tspsolver.model.updates.path;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.updates.ElementUpdate;

public class PathUpdate extends ElementUpdate<Edge, PathUpdateAction> {

	public PathUpdate(Edge element, PathUpdateAction action) {
		super(element, action);
	}

}
