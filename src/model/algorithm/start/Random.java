package model.algorithm.start;

import java.util.LinkedList;
import java.util.Set;

import model.grid.Edge;
import model.grid.Node;

public class Random extends StartAlgorithm {

	@Override
	public LinkedList<Edge> run(Set<Node> nodes, Node startingNode) {
		
		LinkedList<Node> nodesToVisit = new LinkedList<Node>(nodes);
		this.currentNode = startingNode;

		while (nodesToVisit.size() > 1) {

			// Remove the current node from the set of nodes to visit
			nodesToVisit.remove(this.currentNode);
			
			Node nextNode = null;

			// Get random a new node from the set of nodes to visit
			nextNode = nodesToVisit.get((int)(Math.random() * (nodesToVisit.size() + 1)));

			// Add the new edge to the path
			this.currentPath.add(nextNode.getEdgeToNode(this.currentNode));

			// Set the new current node
			this.currentNode = nextNode;

			// Notify the observers that we changed
			this.setChanged();
			this.notifyObservers();
		}

		// Link the last node with the starting node
		Edge lastEdge = currentNode.getEdgeToNode(startingNode);
		if (lastEdge != null) {
			this.currentPath.add(lastEdge);
		} else {
			// FIXME: If the last node has no accessible edge to the starting
			// node we fail here
			System.err.println("Jetzt hämmer es Problem...");
		}

		return currentPath;
	}

}
