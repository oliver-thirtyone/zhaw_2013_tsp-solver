package tspsolver.model.scenario.grid;

public abstract class GridFactory {

	public static Vertex createVertex(String name, int x, int y) {
		return new Vertex(name, x, y);
	}

	public static Vertex getVertex(Grid grid, String name) {
		return grid.getVertex(name);
	}

	public static boolean containsVertex(Grid grid, Vertex vertex) {
		return grid.containsVertex(vertex);
	}

	public static void addVertex(Grid grid, Vertex vertex) {
		grid.addVertex(vertex);
	}

	public static void addVertex(Grid grid, Vertex vertex, boolean link) {
		grid.addVertex(vertex, link);
	}

	public static boolean hasEdge(Vertex firstVertex, Vertex secondVertex) {
		return firstVertex.hasEdgeToVertex(secondVertex);
	}

	public static Edge getEdge(Vertex firstVertex, Vertex secondVertex) {
		return firstVertex.getEdgeToVertex(secondVertex);
	}

	public static Edge addEdge(Vertex firstVertex, Vertex secondVertex) {
		return firstVertex.addEdgeToVertex(secondVertex);
	}

	public static Edge addEdge(Vertex firstVertex, Vertex secondVertex, double weight) {
		return firstVertex.addEdgeToVertex(secondVertex, weight);
	}

	public static void removeEdge(Vertex firstVertex, Vertex secondVertex) {
		firstVertex.removeEdgeToVertex(secondVertex);
	}

}
