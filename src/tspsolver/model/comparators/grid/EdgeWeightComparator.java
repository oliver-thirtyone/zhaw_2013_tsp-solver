package tspsolver.model.comparators.grid;

import java.util.Comparator;

import tspsolver.model.scenario.grid.Edge;

public class EdgeWeightComparator implements Comparator<Edge> {

	@Override
	public int compare(Edge edge1, Edge edge2) {
		int edge1Weight = edge1.getWeight();
		int edge2Weight = edge2.getWeight();

		int result = edge1Weight - edge2Weight;
		if (result == 0) { // same weight
			// This is a fix for the TreeSet in the MinimumSpanningTreeHeuristik
			result = -1;
		}
		return result;
	}

}