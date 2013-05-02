package tspsolver.model.grid.comparators;

import java.util.Comparator;

import tspsolver.model.grid.Edge;


public class EdgeWeightComparator implements Comparator<Edge> {

	@Override
	public int compare(Edge edge1, Edge edge2) {
		Double edge1Weight = Double.valueOf(edge1.getWeight());
		Double edge2Weight = Double.valueOf(edge2.getWeight());

		return edge1Weight.compareTo(edge2Weight);
	}

}