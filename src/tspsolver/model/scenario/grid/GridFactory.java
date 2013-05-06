package tspsolver.model.scenario.grid;

public abstract class GridFactory {

	public static Node createNode(int x, int y) {
		return new Node(x, y);
	}

	public static void addNode(Grid grid, Node node) {
		grid.addNode(node);
	}

	public static void addNode(Grid grid, Node node, boolean link) {
		grid.addNode(node, link);
	}

	public static boolean hasEdge(Node firstNode, Node secondNode) {
		return firstNode.hasEdgeToNode(secondNode);
	}

	public static Edge getEdge(Node firstNode, Node secondNode) {
		return firstNode.getEdgeToNode(secondNode);
	}

	public static Edge addEdge(Node firstNode, Node secondNode) {
		return firstNode.addEdgeToNode(secondNode);
	}

	public static Edge addEdge(Node firstNode, Node secondNode, double weight) {
		return firstNode.addEdgeToNode(secondNode, weight);
	}

	public static void removeEdge(Node firstNode, Node secondNode) {
		firstNode.removeEdgeToNode(secondNode);
	}

}
