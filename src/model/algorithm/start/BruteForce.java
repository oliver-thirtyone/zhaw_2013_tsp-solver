package model.algorithm.start;

import java.util.LinkedList;
import java.util.Set;

import model.grid.Edge;
import model.grid.Node;

public class BruteForce extends StartAlgorithm {

	@Override
	public LinkedList<Edge> run(Set<Node> nodes, Node startingNode) {

		final LinkedList<Node> initNodePath = new LinkedList<Node>(nodes);

		LinkedList<Edge> shortestPath = new LinkedList<Edge>();

		shortestPath = convertToEdgePath(initNodePath);

		Double shortestPathLength = calcPathLength(shortestPath);

		final int size = initNodePath.size();
		// Take every element in the list and change it with all other element
		for (int i = 0; i < size; i++) {
			// Skipping the combinations which have already been tried
			for (int ii = i + 1; ii < size; ii++) {

				// TODO: Optimize: Do not create a new object for every next
				// Calculate next path combination
				LinkedList<Node> nextNodePath = changeElements(
						(LinkedList<Node>) initNodePath.clone(), i, ii);

				LinkedList<Edge> nextPath = convertToEdgePath(nextNodePath);

				Double nextPathLength = calcPathLength(nextPath);

				// Prepare the currentPath for the gui
				this.currentPath.clear();
				this.currentPath.addAll(shortestPath);
				this.currentPath.addAll(nextPath);

				// Notify the observers that we changed
				this.setChanged();
				this.notifyObservers();

				// Check if the next path shorter
				if (nextPathLength < shortestPathLength) {

					shortestPath = nextPath;
					shortestPathLength = nextPathLength;

				}

			}
		}

		return shortestPath;
	}

	private LinkedList<Node> changeElements(LinkedList<Node> list, int first,
			int second) {

		Node tempNode = list.get(first);

		list.set(first, list.get(second));

		list.set(second, tempNode);

		return list;

	}

	private LinkedList<Edge> convertToEdgePath(LinkedList<Node> path) {

		LinkedList<Edge> edgePath = new LinkedList<Edge>();

		Node currentNode = path.getFirst();

		for (Node node : path) {
			edgePath.add(currentNode.getEdgeToNode(node));
			currentNode = node;
		}

		return edgePath;
	}

	private Double calcPathLength(LinkedList<Edge> path) {

		Double length = 0.0;

		for (Edge edge : path) {
			length += edge.getLength();
		}

		return length;
	}

}
