package tspsolver.model.comparators.grid;

import java.util.Comparator;

import tspsolver.model.scenario.grid.Edge;

public class EdgeWeightTreeSetComparator implements Comparator<Edge> {

	@Override
	public int compare(Edge edge1, Edge edge2) {
		int result = edge1.getWeight() - edge2.getWeight();

		if (result == 0) { // same weight
			// This is a fix for the TreeSet in the MinimumSpanningTreeHeuristic
			result = -1;
		}

		return result;
	}

}