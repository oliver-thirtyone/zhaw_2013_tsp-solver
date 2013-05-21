package tspsolver.model.validator;

import java.util.ArrayList;
import java.util.List;

import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;

public class TSPValidator implements Validator {

	public static final int MINIMUM_NODE_COUNT = 3;
	public static final int MINIMUM_EDGE_COUNT = 2;
	public static final int REQUIRED_PATH_OCCURENCES = 2;

	@Override
	public boolean validateScenario(Scenario scenario) {
		boolean validScenario = true;

		Node startingNode = scenario.getStartingNode();
		Grid grid = scenario.getGrid();

		if (startingNode == null) {
			System.err.println("The starting node cannot be null");
			validScenario = false;
		}

		if (!GridFactory.containsNode(grid, startingNode)) {
			System.err.println("The starting node must be in the node set");
			validScenario = false;
		}

		return validScenario;
	}

	@Override
	public boolean validateGrid(Scenario scenario, Grid grid) {
		boolean validGrid = true;

		if (grid.getNumberOfNodes() < TSPValidator.MINIMUM_NODE_COUNT) {
			System.err.println("We need at least " + TSPValidator.MINIMUM_NODE_COUNT + " nodes in the node set");
			validGrid = false;
		}

		for (Node node : scenario.getGrid().getNodes()) {
			if (node.getNumberOfEdges() < TSPValidator.MINIMUM_EDGE_COUNT) {
				System.err.println("Each node needs at least " + TSPValidator.MINIMUM_EDGE_COUNT + " edges");
				validGrid = false;
				break;
			}
		}

		return validGrid;
	}

	@Override
	public boolean validatePath(Scenario scenario, Grid grid, Path path) {
		boolean validPath = true;

		Node startingNode = scenario.getStartingNode();

		List<Node> nodes2Visit = new ArrayList<Node>();
		for (Node node : grid.getNodes()) {
			nodes2Visit.add(node);
		}

		Node currentNode = null;
		Node nextNode = startingNode;

		// Check if we visit each node
		while (validPath && nextNode != null) {
			currentNode = nextNode;
			nextNode = null;

			// Remove the current node from the nodes to visit
			nodes2Visit.remove(currentNode);

			// Check if there are still nodes to visit
			if (nodes2Visit.size() < 1) {
				break;
			}

			int pathOccurencesCount = 0;
			for (Edge edge : currentNode.getEdges()) {

				// Check if an edge of this node belongs to the path
				if (path.containsEdge(edge)) {
					pathOccurencesCount++;

					if (nextNode == null) {
						Node fistNode = edge.getFirstNode();
						Node secondNode = edge.getSecondNode();

						// Set the next node
						if (nodes2Visit.contains(fistNode) && !nodes2Visit.contains(secondNode)) {
							nextNode = fistNode;
						}
						else if (nodes2Visit.contains(secondNode) && !nodes2Visit.contains(fistNode)) {
							nextNode = secondNode;
						}
					}
				}
			}

			// Check if each node has two edges
			if (pathOccurencesCount != TSPValidator.REQUIRED_PATH_OCCURENCES) {
				validPath = false;
			}
		}

		// Check if we have visited all nodes
		if (nodes2Visit.size() != 0 || currentNode == null) {
			validPath = false;
		}
		// Check if we can connect the last node to the starting node
		else if (!currentNode.hasEdgeToNode(startingNode) || !path.containsEdge(currentNode.getEdgeToNode(startingNode))) {
			validPath = false;
		}

		return validPath;
	}
}
