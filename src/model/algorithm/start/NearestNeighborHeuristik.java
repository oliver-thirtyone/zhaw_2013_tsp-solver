package model.algorithm.start;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import model.grid.Edge;
import model.grid.Node;

public class NearestNeighborHeuristik extends StartAlgorithm {

	@Override
	public LinkedList<Edge> run(Set<Node> nodes, Node startingNode) {
		Set<Node> nodesToVisit = new HashSet<Node>(nodes);
		this.currentNode = startingNode;

		while (nodesToVisit.size() > 1) {
			Edge shortestEdge = null;

			// Remove the current node from the set of nodes to visit
			nodesToVisit.remove(this.currentNode);

			// Get all available edges from the current node
			Set<Edge> edges = this.currentNode.getAccessibleEdges();

			// Get the shortest edge to a path that we still have to visit
			for (Edge edge : edges) {

				// Does this edge lead to a node that we still have to visit?
				Boolean validEdge = Boolean.FALSE;
				for (Node nodeToVisit : nodesToVisit) {
					if (edge.getFirstNode() == nodeToVisit || edge.getSecondNode() == nodeToVisit) {
						validEdge = Boolean.TRUE;
						break;
					}
				}

				// If this edge does not lead to a node we want to visit we will continue with the next one
				if (!validEdge) {
					continue;
				}

				// Check if this edge is the shortest
				if (shortestEdge == null || shortestEdge.getLength() > edge.getLength()) {
					shortestEdge = edge;
				}

			}

			// Add the new edge to the path
			this.currentPath.add(shortestEdge);

			// Set the new current node
			this.currentNode = shortestEdge.getFirstNode() == this.currentNode ? shortestEdge.getSecondNode() : shortestEdge.getFirstNode();

			// Notify the observers that we changed
			this.setChanged();
			this.notifyObservers();
		}

		// Link the last node with the starting node
		Edge lastEdge = currentNode.getEdgeToNode(startingNode);
		if (lastEdge != null) {
			this.currentPath.add(lastEdge);
		}
		else {
			// FIXME: If the last node has no accessible edge to the starting node we fail here
			System.err.println("Jetzt hämmer es Problem...");
		}		

		return currentPath;
	}

}
