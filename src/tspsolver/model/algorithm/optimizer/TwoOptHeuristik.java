package tspsolver.model.algorithm.optimizer;

import java.util.HashSet;
import java.util.Vector;

import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class TwoOptHeuristik extends OptimizerAlgorithm {

	private final Vector<Node> nodesInOrder;

	public TwoOptHeuristik() {
		this.nodesInOrder = new Vector<Node>();
	}

	@Override
	protected void doInitialize() {
		// Get the node in line
		Node currentNode = this.getStartingNode();

		this.nodesInOrder.add(currentNode);

		final int numberOfEdges = this.getPath().getNumberOfEdges();

		final HashSet<Edge> edges = new HashSet<Edge>();
		for (final Edge edge : this.getPath().getEdges()) {
			edges.add(edge);
		}

		while (this.nodesInOrder.size() < numberOfEdges) {
			for (final Edge edge : edges) {
				if (edge.getFirstNode() == currentNode) {

					edges.remove(edge);

					currentNode = edge.getSecondNode();

					this.nodesInOrder.add(currentNode);
					break;
				}
				else if (edge.getSecondNode() == currentNode) {

					edges.remove(edge);

					currentNode = edge.getFirstNode();

					this.nodesInOrder.add(currentNode);
					break;
				}
			}

		}

	}

	@Override
	protected void doReset() {
		this.nodesInOrder.clear();
	}

	@Override
	protected boolean doStep() {
		try {

			final int numberOfNodes = this.getPath().getNumberOfEdges();

			final double lightestWeight = this.getPath().getWeight();

			for (int i = 0; i < numberOfNodes; i++) {
				for (int k = i + 1; k < numberOfNodes - 1; k++) {

					final Path newPath = this.generateNewPath(i, k);

					final double newWeight = newPath.getWeight();

					if (newWeight < lightestWeight) {

						this.getPathUpdater().clearPath();
						this.getPathUpdater().addPath(newPath);
						this.getPathUpdater().updatePath();
						return true;
					}
				}
			}

			this.finishedSuccessfully();
			return true;

		}
		catch (final IllegalStateException ex) {
			return false;
		}
	}

	private Path generateNewPath(int i, int k) {

		final Vector<Node> newNodePath = new Vector<Node>();

		for (int j = 0; j < i; j++) {
			newNodePath.add(this.nodesInOrder.get(j));
		}

		for (int j = k; j >= i; j--) {
			newNodePath.add(this.nodesInOrder.get(j));
		}

		for (int j = k + 1; j < this.nodesInOrder.size(); j++) {
			newNodePath.add(this.nodesInOrder.get(j));
		}

		return this.convertNodesToPath(newNodePath);
	}

	private Path convertNodesToPath(Vector<Node> nodePath) {

		final Path path = new Path();
		final PathUpdater pathUpdater = new PathUpdater(path);

		for (int j = 1; j <= this.nodesInOrder.size(); j++) {
			final Node firstNode = nodePath.get(j - 1);
			final Node secondNode = (j == this.nodesInOrder.size()) ? nodePath.get(0) : nodePath.get(j);

			final Edge edge = firstNode.getEdgeToNode(secondNode);
			if (edge == null) {
				// FIXME: this path does not work, what do we do now?
				throw new IllegalStateException();
			}

			pathUpdater.addEdge(edge);
		}

		pathUpdater.updatePath();

		return pathUpdater.getPath();
	}

}
