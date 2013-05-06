package tspsolver.model.algorithm.start;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Node;

public class RandomAlgorithm extends StartAlgorithm {

	private final SecureRandom random;
	private final Set<Node> nodesToVisit;

	private Node currentNode;

	public RandomAlgorithm() {
		this.random = new SecureRandom();
		this.nodesToVisit = new HashSet<Node>();

		this.reset();
	}

	@Override
	protected void doInitialize() {
		for (Node node : this.getGrid().getNodes()) {
			this.nodesToVisit.add(node);
		}
		this.setCurrentNode(this.getStartingNode());
	}

	@Override
	protected void doReset() {
		this.nodesToVisit.clear();
		this.setCurrentNode(null);
	}

	@Override
	protected boolean doStep() {
		boolean successfulStep = true;

		if (this.nodesToVisit.size() > 1) {
			Edge randomEdge = null;

			// Remove the current node from the set of nodes to visit
			this.nodesToVisit.remove(this.getCurrentNode());

			// Get all available edges from the current node
			List<Edge> possibleEdges = new ArrayList<Edge>();
			for (Edge edge : this.getCurrentNode().getEdges()) {
				possibleEdges.add(edge);
			}

			// Get the a random edge to a node that we still have to visit
			while (possibleEdges.size() > 0 && randomEdge == null) {
				Edge edge = possibleEdges.get(this.random.nextInt(possibleEdges.size()));
				possibleEdges.remove(edge);

				// If this edge does lead to a node we want to visit we will use it
				for (Node nodeToVisit : this.nodesToVisit) {
					if (edge.getFirstNode() == nodeToVisit || edge.getSecondNode() == nodeToVisit) {
						randomEdge = edge;
						break;
					}
				}
			}

			if (randomEdge != null) {
				// Add the new edge to the path
				this.getPathUpdater().addEdge(randomEdge);

				// Set the new current node
				if (randomEdge.getFirstNode() == this.getCurrentNode()) {
					this.setCurrentNode(randomEdge.getSecondNode());
				} else {
					this.setCurrentNode(randomEdge.getFirstNode());
				}
			} else {
				System.err.println("No edge found, we stop here...");
				successfulStep = false;
			}
		} else {
			// Link the last node with the starting node
			Edge lastEdge = this.getCurrentNode().getEdgeToNode(this.getStartingNode());
			if (lastEdge != null) {
				this.getPathUpdater().addEdge(lastEdge);
				this.setCurrentNode(this.getStartingNode());

				this.finishedSuccessfully();
			} else {
				// FIXME: If the last node has no accessible edge to the starting node we fail here
				System.err.println("Jetzt hämmer es Problem...");
				successfulStep = false;
			}
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	public Node getCurrentNode() {
		return this.currentNode;
	}

	protected void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

}
