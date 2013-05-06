package tspsolver.model.algorithm.optimizer;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class TwoOptHeuristik extends OptimizerAlgorithm {

	private Vector<Node> nodesInOrder;

	public TwoOptHeuristik() {
	}

	@Override
	protected void doInitialize() {

		this.nodesInOrder = new Vector<Node>();

		// Get the node in line
		Node currentNode = this.getStartingNode();

		this.nodesInOrder.add(currentNode);

		int numberOfEdges = this.getPath().getNumberOfEdges();

		HashSet<Edge> edges = new HashSet<Edge>();
		for (Edge edge : this.getPath().getEdges()) {
			edges.add(edge);
		}

		while (this.nodesInOrder.size() < numberOfEdges) {
			for (Edge edge : edges) {
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
	}

	@Override
	protected boolean doStep() {
		try {

			int numberOfNodes = this.getPath().getNumberOfEdges();

			double lightestWeight = this.getPath().getWeight();

			for (int i = 0; i < numberOfNodes; i++) {
				for (int k = i + 1; k < numberOfNodes - 1; k++) {

					Path newPath = this.generateNewPath(i, k);

					double newWeight = newPath.getWeight();

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
		catch (IllegalStateException ex) {
			return false;
		}
	}

	private Path generateNewPath(int i, int k) {

		Vector<Node> newNodePath = new Vector<Node>();

		for (int j = 0; j < i; j++) {
			newNodePath.add(this.nodesInOrder.get(j));
		}

		for (int j = k; j >= i; j--) {
			newNodePath.add(this.nodesInOrder.get(j));
		}

		for (int j = k + 1; j < this.nodesInOrder.size(); j++) {
			newNodePath.add(this.nodesInOrder.get(j));
		}

		return convertNodesToPath(newNodePath);
	}

	private Path convertNodesToPath(Vector<Node> nodePath) {

		Path path = new Path();
		PathUpdater pathUpdater = new PathUpdater(path);

		for (int j = 1; j <= this.nodesInOrder.size(); j++) {
			Node firstNode = nodePath.get(j - 1);
			Node secondNode = (j == this.nodesInOrder.size()) ? nodePath.get(0) : nodePath.get(j);

			Edge edge = firstNode.getEdgeToNode(secondNode);
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
