package tspsolver.model.algorithm;

import java.util.ArrayList;
import java.util.List;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;

public abstract class OptimizerAlgorithm extends Algorithm {

	@Override
	public final synchronized void validateArguments() {
		boolean validArguments = true;

		// Add each node twice, because each node must touch exactly two edges
		final List<Node> nodes2Visit = new ArrayList<Node>();
		for (final Node node : this.getGrid().getNodes()) {
			nodes2Visit.add(node);
			nodes2Visit.add(node);
		}

		// Check if the path visits each node once
		for (final Edge edge : this.getPath().getEdges()) {
			final Node fistNode = edge.getFirstNode();
			final Node secondNode = edge.getSecondNode();

			if (!nodes2Visit.remove(fistNode)) {
				validArguments = false;
				System.err.println("More than two edges touch this node: " + fistNode);
			}
			if (!nodes2Visit.remove(secondNode)) {
				validArguments = false;
				System.err.println("More than two edges touch this node: " + secondNode);
			}
		}

		for (final Node node : nodes2Visit) {
			validArguments = false;
			System.err.println("This node gets visited less then two times: " + node);
		}

		this.setValidArguments(validArguments);
	}
}
