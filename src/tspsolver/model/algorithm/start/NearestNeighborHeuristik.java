package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;

public class NearestNeighborHeuristik extends StartAlgorithm {

	private final Set<Node> nodesToVisit;

	private Node currentNode;

	public NearestNeighborHeuristik() {
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
			Edge shortestEdge = null;

			// Remove the current node from the set of nodes to visit
			this.nodesToVisit.remove(this.getCurrentNode());

			// Get all available edges from the current node
			Edge[] edges = this.getCurrentNode().getEdges();

			// Get the shortest edge to a node that we still have to visit
			for (Edge edge : edges) {

				// Does this edge lead to a node that we still have to visit?
				boolean validEdge = false;
				for (Node nodeToVisit : this.nodesToVisit) {
					if (edge.getFirstNode() == nodeToVisit || edge.getSecondNode() == nodeToVisit) {
						validEdge = true;
						break;
					}
				}

				// If this edge does not lead to a node we want to visit we will continue with the next one
				if (!validEdge) {
					continue;
				}

				// Check if this edge is the shortest
				if (shortestEdge == null || shortestEdge.getWeight() > edge.getWeight()) {
					shortestEdge = edge;
				}

			}

			if (shortestEdge != null) {
				// Add the new edge to the path
				this.getPathUpdater().addEdge(shortestEdge);

				// Set the new current node
				if (shortestEdge.getFirstNode() == this.getCurrentNode()) {
					this.setCurrentNode(shortestEdge.getSecondNode());
				}
				else {
					this.setCurrentNode(shortestEdge.getFirstNode());
				}
			}
			else {
				System.err.println("No edge found, we stop here...");
				successfulStep = false;
			}
		}
		else {
			// Link the last node with the starting node
			Edge lastEdge = this.getCurrentNode().getEdgeToNode(this.getStartingNode());
			if (lastEdge != null) {
				this.getPathUpdater().addEdge(lastEdge);
				this.setCurrentNode(this.getStartingNode());

				this.finishedSuccessfully();
			}
			else {
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
