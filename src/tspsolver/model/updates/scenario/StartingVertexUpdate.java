package tspsolver.model.updates.scenario;

import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.updates.ElementUpdate;

public class StartingVertexUpdate extends ElementUpdate<Vertex, StartingVertexUpdateAction> {

	public StartingVertexUpdate(Vertex element, StartingVertexUpdateAction action) {
		super(element, action);
	}

}
