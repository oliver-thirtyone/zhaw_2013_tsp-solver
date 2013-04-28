package model.algorithm.start;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import model.grid.Edge;
import model.grid.Node;

public class MinimumSpanningTreeHeuristik extends StartAlgorithm {

	@Override
	public LinkedList<Edge> run(Set<Node> nodes, Node startingNode) {

		this.calcSpanningTree(nodes);

		LinkedList<Edge> minSpanningTree = (LinkedList<Edge>) this.currentPath
				.clone();

		LinkedList<Edge> path = new LinkedList<Edge>();

		Node lastNode = doEulerianTrail(minSpanningTree, startingNode, path);

		// Connect the last node from the eulerian path with the start node to close the circle
		path.add(startingNode.getEdgeToNode(lastNode));

		return path;
	}

	private void calcSpanningTree(Set<Node> nodes) {

		// Get all eages from the nodes
		Set<Edge> allEdges = convertToEdgeSet(nodes);

		// Sort them by length
		PriorityQueue<Edge> sortedEages = new PriorityQueue<Edge>(allEdges);

		// Take so long short eages while all nodes are visited
		HashSet<Node> spanningTreeNodes = new HashSet<Node>();
		int edgeCount = nodes.size() - 1;

		for (Edge currentEdge : sortedEages) {

			// Skip eages where no new eages contains, it will build a circle
			if (spanningTreeNodes.contains(currentEdge.getFirstNode()) == false) {

				this.currentPath.add(currentEdge);

				spanningTreeNodes.add(currentEdge.getFirstNode());

				if (spanningTreeNodes.contains(currentEdge.getSecondNode()) == false) {

					spanningTreeNodes.add(currentEdge.getSecondNode());
				}
			} else if (spanningTreeNodes.contains(currentEdge.getSecondNode()) == false) {

				this.currentPath.add(currentEdge);

				spanningTreeNodes.add(currentEdge.getSecondNode());
			}

			// Notify the observers that we changed
			this.setChanged();
			this.notifyObservers();

			// Break if all nodes are connected
			if (edgeCount <= this.currentPath.size()) {
				break;
			}
		}

	}

	private Node doEulerianTrail(LinkedList<Edge> minSpanningTree,
			Node startingNode, LinkedList<Edge> path) {

		Node currentNode = startingNode;

		for (Edge edge : minSpanningTree) {
			
			// Find all edge from the node in the spanning tree
			if (edge.getFirstNode() == startingNode) {

				// Remove the edge from the spanning tree, because this edge is used
				minSpanningTree.remove(edge);

				// Connect the current node with the node from the new edge
				path.add(currentNode.getEdgeToNode(edge.getSecondNode()));

				// Prepare the currentPath for the gui
				this.currentPath.clear();
				this.currentPath.addAll(path);
				this.currentPath.addAll(minSpanningTree);

				// Notify the observers that we changed
				this.setChanged();
				this.notifyObservers();

				// Go deeper and set the result as new current node, so we can handle the branches.
				currentNode = doEulerianTrail(minSpanningTree, currentNode,
						path);

			} else if (edge.getSecondNode() == startingNode) {

				// Remove the edge from the spanning tree, because this edge is used
				minSpanningTree.remove(edge);

				// Connect the current node with the node from the new edge
				path.add(currentNode.getEdgeToNode(edge.getFirstNode()));

				// Prepare the currentPath for the gui
				this.currentPath.clear();
				this.currentPath.addAll(path);
				this.currentPath.addAll(minSpanningTree);

				// Notify the observers that we changed
				this.setChanged();
				this.notifyObservers();
				
				// Go deeper and set the result as new current node, so we can handle the branches.
				currentNode = doEulerianTrail(minSpanningTree, currentNode,
						path);
			}
		}

		// Return the current node it's always a leaf of the spanning tree.  
		return currentNode;
	}

	private Set<Edge> convertToEdgeSet(Set<Node> nodes) {

		HashSet<Edge> allEdges = new HashSet<Edge>();

		for (Node node : nodes) {
			allEdges.addAll(node.getAccessibleEdges());
		}

		return allEdges;
	}

}
