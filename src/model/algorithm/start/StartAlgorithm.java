package model.algorithm.start;

import java.util.Set;

import model.algorithm.Algorithm;
import model.grid.Node;

public abstract class StartAlgorithm extends Algorithm {

	private final Set<Node> nodes;
	private final Node startingNode;

	public StartAlgorithm(Set<Node> nodes, Node startingNode) {
		super();

		this.nodes = nodes;
		this.startingNode = startingNode;
	}

	@Override
	public final void validateArguments() {
		boolean validArguments = true;

		if (this.startingNode == null) {
			System.err.println("The starting node cannot be null");
			validArguments = false;
		}

		if (!this.nodes.contains(this.startingNode)) {
			System.err.println("The starting node must be in the node set");
			validArguments = false;
		}

		if (this.nodes.size() < 2) {
			System.err.println("We need at least two nodes in the node set");
			validArguments = false;
		}

		// TODO: further validations...

		this.setValidArguments(validArguments);
	}

	public Set<Node> getNodes() {
		return this.nodes;
	}

	public Node getStartingNode() {
		return this.startingNode;
	}

}
