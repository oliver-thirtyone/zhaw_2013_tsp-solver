package tspsolver.model.algorithm.start;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;

public class RandomAlgorithm extends AStartAlgorithm {

	private final SecureRandom random;
	private final Set<Node> nodesToVisit;

	private Node currentNode;

	public RandomAlgorithm(Path path, Grid grid) {
		super(path, grid);

		this.random = new SecureRandom();
		this.nodesToVisit = new HashSet<Node>(this.getGrid().getNodes());

		this.reset();
	}

	@Override
	protected boolean doStep() {
		boolean successfulStep = true;

		if (this.nodesToVisit.size() > 1) {
			Edge randomEdge = null;

			// Remove the current node from the set of nodes to visit
			this.nodesToVisit.remove(this.getCurrentNode());

			// Get all available edges from the current node
			Collection<Edge> edges = this.getCurrentNode().getEdges();

			// Get the a random edge to a node that we still have to visit
			List<Edge> possibleEdges = new ArrayList<Edge>(edges);
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
			Edge lastEdge = this.getCurrentNode().getEdgeToNode(this.getGrid().getStartingNode());
			if (lastEdge != null) {
				this.getPathUpdater().addEdge(lastEdge);
				this.setCurrentNode(this.getGrid().getStartingNode());

				this.setFinishedSuccessful(true);
			} else {
				// FIXME: If the last node has no accessible edge to the starting node we fail here
				System.err.println("Jetzt hämmer es Problem...");

				this.setFinishedSuccessful(false);
				successfulStep = false;
			}
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	@Override
	public void reset() {
		super.reset();

		this.nodesToVisit.clear();
		this.nodesToVisit.addAll(this.getGrid().getNodes());
		this.setCurrentNode(this.getGrid().getStartingNode());
	}

	public Node getCurrentNode() {
		return this.currentNode;
	}

	protected void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

}
