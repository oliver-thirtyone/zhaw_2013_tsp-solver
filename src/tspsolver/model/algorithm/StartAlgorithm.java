package tspsolver.model.algorithm;

import tspsolver.model.scenario.grid.Node;

public abstract class StartAlgorithm extends Algorithm {

	@Override
	public final synchronized void validateArguments() {
		boolean validArguments = true;

		if (this.getStartingNode() == null) {
			System.err.println("The starting node cannot be null");
			validArguments = false;
		}

		if (!this.getGrid().containsNode(this.getStartingNode())) {
			System.err.println("The starting node must be in the node set");
			validArguments = false;
		}

		if (this.getGrid().getNumberOfNodes() < 3) {
			System.err.println("We need at least three nodes in the node set");
			validArguments = false;
		}

		for (Node node : this.getGrid().getNodes()) {
			if (node.getNumberOfEdges() < 2) {
				System.err.println("Each node needs at least two edges");
				validArguments = false;
				break;
			}
		}

		this.setValidArguments(validArguments);
	}

}
