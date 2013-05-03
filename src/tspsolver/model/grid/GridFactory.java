package tspsolver.model.grid;

public abstract class GridFactory {

	public static Node createNode(int x, int y) {
		return new Node(x, y);
	}

	public static void hasEdge(Node firstNode, Node secondNode) {
		firstNode.hasEdgeToNode(secondNode);
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
