package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;


public class MinimumSpanningTreeHeuristik extends AStartAlgorithm {

	// FIXME: Refactor this class !!!
	public MinimumSpanningTreeHeuristik(Grid grid) {
		super(grid);
	}

	@Override
	public boolean doStep() {
		Set<Node> nodes = this.getGrid().getNodes();
		Node startingNode = this.getGrid().getStartingNode();

		this.calcSpanningTree(nodes);

		Set<Edge> minSpanningTree = new HashSet<Edge>(this.getCurrentPath().getEdges());

		Set<Edge> path = new HashSet<Edge>();

		Node lastNode = this.doEulerianTrail(minSpanningTree, startingNode, path);

		// Connect the last node from the eulerian path with the start node to
		// close the circle
		path.add(startingNode.getEdgeToNode(lastNode));

		this.setFinishedSuccessful(true);
		return true;
	}

	private void calcSpanningTree(Set<Node> nodes) {

		// Get all eages from the nodes
		Set<Edge> allEdges = this.convertToEdgeSet(nodes);

		// Sort them by length
		PriorityQueue<Edge> sortedEages = new PriorityQueue<Edge>(allEdges);

		// Take so long short eages while all nodes are visited
		HashSet<Node> spanningTreeNodes = new HashSet<Node>();
		int edgeCount = nodes.size() - 1;

		for (Edge currentEdge : sortedEages) {

			// Skip eages where no new eages contains, it will build a circle
			if (spanningTreeNodes.contains(currentEdge.getFirstNode()) == false) {

				this.getCurrentPath().addEdge(currentEdge);

				spanningTreeNodes.add(currentEdge.getFirstNode());

				if (spanningTreeNodes.contains(currentEdge.getSecondNode()) == false) {

					spanningTreeNodes.add(currentEdge.getSecondNode());
				}
			} else if (spanningTreeNodes.contains(currentEdge.getSecondNode()) == false) {

				this.getCurrentPath().addEdge(currentEdge);

				spanningTreeNodes.add(currentEdge.getSecondNode());
			}

			// TODO: step finished here

			// Break if all nodes are connected
			if (edgeCount <= this.getCurrentPath().getEdges().size()) {
				break;
			}
		}

	}

	private Node doEulerianTrail(Set<Edge> subMinSpanningTree2, Node startingNode, Set<Edge> path) {

		Node currentNode = startingNode;

		for (Edge edge : subMinSpanningTree2) {

			// Find all edge from the node in the spanning tree
			if (edge.getFirstNode() == startingNode) {

				// Connect the current node with the node from the new edge
				path.add(currentNode.getEdgeToNode(edge.getSecondNode()));

				// Remove the edge from the spanning tree, because this edge is
				// used
				HashSet<Edge> subMinSpanningTree = new HashSet<Edge>(subMinSpanningTree2);
				subMinSpanningTree.remove(edge);

				// Prepare the currentPath for the gui
				this.getCurrentPath().clearEdges();
				this.getCurrentPath().addEdges(path);
				this.getCurrentPath().addEdges(subMinSpanningTree);

				// TODO: step finished here

				// Go deeper and set the result as new current node, so we can
				// handle the branches.
				currentNode = this.doEulerianTrail(subMinSpanningTree, currentNode, path);

			} else if (edge.getSecondNode() == startingNode) {

				// Connect the current node with the node from the new edge
				path.add(currentNode.getEdgeToNode(edge.getFirstNode()));

				// Remove the edge from the spanning tree, because this edge is
				// used
				Set<Edge> subMinSpanningTree = new HashSet<Edge>(subMinSpanningTree2);
				subMinSpanningTree.remove(edge);

				// Prepare the currentPath for the gui
				this.getCurrentPath().clearEdges();
				this.getCurrentPath().addEdges(path);
				this.getCurrentPath().addEdges(subMinSpanningTree);

				// TODO: step finished here

				// Go deeper and set the result as new current node, so we can
				// handle the branches.
				currentNode = this.doEulerianTrail(subMinSpanningTree, currentNode, path);
			}
		}

		// Return the current node it's always a leaf of the spanning tree.
		return currentNode;
	}

	private Set<Edge> convertToEdgeSet(Set<Node> nodes) {

		Set<Edge> allEdges = new HashSet<Edge>();

		for (Node node : nodes) {
			allEdges.addAll(node.getAccessibleEdgeCollection());
		}

		return allEdges;
	}

}
