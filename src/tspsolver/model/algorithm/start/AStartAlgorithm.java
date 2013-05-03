package tspsolver.model.algorithm.start;

import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;

public abstract class AStartAlgorithm extends Algorithm {

	private final Grid grid;

	public AStartAlgorithm(Path path, Grid grid) {
		super(path);
		this.grid = grid;
	}

	@Override
	public final void validateArguments() {
		boolean validArguments = true;

		if (this.getGrid().getStartingNode() == null) {
			System.err.println("The starting node cannot be null");
			validArguments = false;
		}

		if (!this.getGrid().getNodes().contains(this.getGrid().getStartingNode())) {
			System.err.println("The starting node must be in the node set");
			validArguments = false;
		}

		if (this.getGrid().getNodes().size() < 3) {
			System.err.println("We need at least three nodes in the node set");
			validArguments = false;
		}

		for (Node node : this.getGrid().getNodes()) {
			if (node.getNumberOfEdges() < 2) {
				System.err.println("Each node needs at least two accessible edges");
				validArguments = false;
				break;
			}
		}

		// TODO: further validations...

		this.setValidArguments(validArguments);
	}

	public Grid getGrid() {
		return this.grid;
	}

}
