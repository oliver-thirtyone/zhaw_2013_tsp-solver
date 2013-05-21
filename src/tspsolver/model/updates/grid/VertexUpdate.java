package tspsolver.model.updates.grid;

import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.updates.ElementUpdate;

public class VertexUpdate extends ElementUpdate<Vertex, VertexUpdateAction> {

	public VertexUpdate(Vertex element, VertexUpdateAction action) {
		super(element, action);
	}

}
