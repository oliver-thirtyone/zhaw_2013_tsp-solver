package tspsolver.model.algorithm.start;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.grid.comparators.EdgeWeightComparator;
import tspsolver.model.path.Path;

public class MinimumSpanningTreeHeuristik extends AStartAlgorithm {

	// FIXME: PELOSO NACHRICHT:
	// Zirkel-Bildung zu schlecht berechnet. z.B: Wenn bei einem Quadrat die obere und die untere Kante verbunden wurden haben wir ein Problem...

	// FIXME: Refactor this class !!!
	public MinimumSpanningTreeHeuristik(Path path, Grid grid) {
		super(path, grid);
	}

	@Override
	public boolean doStep() {
		Set<Node> nodes = this.getGrid().getNodes();
		Node startingNode = this.getGrid().getStartingNode();

		this.calcSpanningTree(nodes);

		Set<Edge> minSpanningTree = new HashSet<Edge>(this.getPathUpdater().getPath().getEdges());

		Set<Edge> path = new HashSet<Edge>();

		Node lastNode = this.doEulerianTrail(minSpanningTree, startingNode, path);

		// Connect the last node from the eulerian path with the start node to
		// close the circle
		path.add(startingNode.getEdgeToNode(lastNode));

		this.setFinishedSuccessful(true);
		return true;
	}

	private void calcSpanningTree(Set<Node> nodes) {

		// Get all edges from the nodes
		Set<Edge> allEdges = this.convertToEdgeSet(nodes);

		// Sort them by length
		List<Edge> sortedEdges = new ArrayList<Edge>(allEdges);

		// Sort all edges by weight
		Collections.sort(sortedEdges, new EdgeWeightComparator());

		// Take so long short edges while all nodes are visited
		HashSet<Node> spanningTreeNodes = new HashSet<Node>();
		int edgeCount = nodes.size() - 1;

		for (Edge currentEdge : sortedEdges) {

			// Skip edges where no new edges contains, it will build a circle
			if (spanningTreeNodes.contains(currentEdge.getFirstNode()) == false) {

				this.getPathUpdater().addEdge(currentEdge);

				spanningTreeNodes.add(currentEdge.getFirstNode());

				if (spanningTreeNodes.contains(currentEdge.getSecondNode()) == false) {

					spanningTreeNodes.add(currentEdge.getSecondNode());
				}
			}
			else if (spanningTreeNodes.contains(currentEdge.getSecondNode()) == false) {

				this.getPathUpdater().addEdge(currentEdge);

				spanningTreeNodes.add(currentEdge.getSecondNode());
			}

			this.getPathUpdater().updatePath();
			// TODO: step finished here

			// Break if all nodes are connected
			if (edgeCount <= this.getPathUpdater().getPath().getNumberOfEdges()) {
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
				this.getPathUpdater().clearWholePath();
				this.getPathUpdater().addEdges(path);
				this.getPathUpdater().addEdges(subMinSpanningTree);

				this.getPathUpdater().updatePath();
				// TODO: step finished here

				// Go deeper and set the result as new current node, so we can
				// handle the branches.
				currentNode = this.doEulerianTrail(subMinSpanningTree, currentNode, path);

			}
			else if (edge.getSecondNode() == startingNode) {

				// Connect the current node with the node from the new edge
				path.add(currentNode.getEdgeToNode(edge.getFirstNode()));

				// Remove the edge from the spanning tree, because this edge is
				// used
				Set<Edge> subMinSpanningTree = new HashSet<Edge>(subMinSpanningTree2);
				subMinSpanningTree.remove(edge);

				// Prepare the currentPath for the gui
				this.getPathUpdater().clearWholePath();
				this.getPathUpdater().addEdges(path);
				this.getPathUpdater().addEdges(subMinSpanningTree);

				this.getPathUpdater().updatePath();
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
			allEdges.addAll(node.getEdges());
		}

		return allEdges;
	}

}
