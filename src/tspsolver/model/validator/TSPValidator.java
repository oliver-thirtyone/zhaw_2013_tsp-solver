package tspsolver.model.validator;

import java.util.ArrayList;
import java.util.List;

import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.scenario.path.Path;

public class TSPValidator implements Validator {

	public static final int MINIMUM_VERTEX_COUNT = 3;
	public static final int MINIMUM_EDGE_COUNT = 2;
	public static final int REQUIRED_PATH_OCCURENCES = 2;

	@Override
	public boolean validateScenario(Scenario scenario) {
		boolean validScenario = true;

		Vertex startingVertex = scenario.getStartingVertex();
		Grid grid = scenario.getGrid();

		if (startingVertex == null) {
			System.err.println("The starting vertex cannot be null");
			validScenario = false;
		}

		if (!GridFactory.containsVertex(grid, startingVertex)) {
			System.err.println("The starting vertex must be in the vertex set");
			validScenario = false;
		}

		return validScenario;
	}

	@Override
	public boolean validateGrid(Scenario scenario, Grid grid) {
		boolean validGrid = true;

		if (grid.getNumberOfVertices() < TSPValidator.MINIMUM_VERTEX_COUNT) {
			System.err.println("We need at least " + TSPValidator.MINIMUM_VERTEX_COUNT + " vertices in the vertex set");
			validGrid = false;
		}

		for (Vertex vertex : scenario.getGrid().getVertices()) {
			if (vertex.getNumberOfEdges() < TSPValidator.MINIMUM_EDGE_COUNT) {
				System.err.println("Each vertex needs at least " + TSPValidator.MINIMUM_EDGE_COUNT + " edges");
				validGrid = false;
				break;
			}
		}

		return validGrid;
	}

	@Override
	public boolean validatePath(Scenario scenario, Grid grid, Path path) {
		boolean validPath = true;

		Vertex startingVertex = scenario.getStartingVertex();

		List<Vertex> vertices2Visit = new ArrayList<Vertex>();
		for (Vertex vertex : grid.getVertices()) {
			vertices2Visit.add(vertex);
		}

		Vertex currentVertex = null;
		Vertex nextVertex = startingVertex;

		// Check if we visit each vertex
		while (validPath && nextVertex != null) {
			currentVertex = nextVertex;
			nextVertex = null;

			// Remove the current vertex from the vertices to visit
			vertices2Visit.remove(currentVertex);

			// Check if there are still vertices to visit
			if (vertices2Visit.size() < 1) {
				break;
			}

			int pathOccurencesCount = 0;
			for (Edge edge : currentVertex.getEdges()) {

				// Check if an edge of this vertex belongs to the path
				if (path.containsEdge(edge)) {
					pathOccurencesCount++;

					if (nextVertex == null) {
						Vertex fistVertex = edge.getFirstVertex();
						Vertex secondVertex = edge.getSecondVertex();

						// Set the next vertex
						if (vertices2Visit.contains(fistVertex) && !vertices2Visit.contains(secondVertex)) {
							nextVertex = fistVertex;
						}
						else if (vertices2Visit.contains(secondVertex) && !vertices2Visit.contains(fistVertex)) {
							nextVertex = secondVertex;
						}
					}
				}
			}

			// Check if each vertex has two edges
			if (pathOccurencesCount != TSPValidator.REQUIRED_PATH_OCCURENCES) {
				validPath = false;
			}
		}

		// Check if we have visited all vertices
		if (vertices2Visit.size() != 0 || currentVertex == null) {
			validPath = false;
		}
		// Check if we can connect the last vertex to the starting vertex
		else if (!currentVertex.hasEdgeToVertex(startingVertex) || !path.containsEdge(currentVertex.getEdgeToVertex(startingVertex))) {
			validPath = false;
		}

		return validPath;
	}
}
