package tspsolver.model.scenario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;
import tspsolver.model.updates.scenario.StartingVertexUpdate;
import tspsolver.model.updates.scenario.StartingVertexUpdateAction;
import tspsolver.model.validator.Validator;

public class Scenario extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = -7758506719837963384L;

	private final Validator validator;

	private final Grid grid;
	private final Path path;

	private String name;
	private Vertex startingVertex;

	public Scenario(Validator validator) {
		this.validator = validator;

		this.grid = new Grid();
		this.path = new Path();

		this.name = "";
		this.startingVertex = null;

		this.grid.addObserver(this);
		this.path.addObserver(this);
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;

		result = prime * result + ((this.validator == null) ? 0 : this.validator.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.grid == null) ? 0 : this.grid.hashCode());
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
		result = prime * result + ((this.startingVertex == null) ? 0 : this.startingVertex.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		Scenario other = (Scenario) obj;
		if (this.validator == null) {
			if (other.validator != null) {
				return false;
			}
		}
		else if (!this.validator.equals(other.validator)) {
			return false;
		}

		if (this.grid == null) {
			if (other.grid != null) {
				return false;
			}
		}
		else if (!this.grid.equals(other.grid)) {
			return false;
		}

		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		}
		else if (!this.path.equals(other.path)) {
			return false;
		}

		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!this.name.equals(other.name)) {
			return false;
		}

		if (this.startingVertex == null) {
			if (other.startingVertex != null) {
				return false;
			}
		}
		else if (!this.startingVertex.equals(other.startingVertex)) {
			return false;
		}

		return true;
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public Scenario copy() {
		// Create the scenario copy
		Scenario scenarioCopy = new Scenario(this.validator);

		// Copy the gird
		Grid gridCopy = scenarioCopy.getGrid();

		// Copy the vertices
		Map<Vertex, Vertex> vertexCopies = new HashMap<Vertex, Vertex>();
		for (Vertex vertex : this.grid.getVertices()) {
			// Create a copy of each vertex
			Vertex vertexCopy = GridFactory.createVertex(vertex.getName(), vertex.getX(), vertex.getY());

			// Add the copied vertex to the copied grid
			GridFactory.addVertex(gridCopy, vertexCopy, false);

			// Add the copied vertex to the reference map
			vertexCopies.put(vertex, vertexCopy);
		}

		// Copy the edges
		Map<Edge, Edge> edgeCopies = new HashMap<Edge, Edge>();
		for (Vertex vertex : this.grid.getVertices()) {
			for (Edge edge : vertex.getEdges()) {
				// Get the copied vertices
				Vertex firstVertexCopy = vertexCopies.get(edge.getFirstVertex());
				Vertex secondVertexCopy = vertexCopies.get(edge.getSecondVertex());

				if (!GridFactory.hasEdge(firstVertexCopy, secondVertexCopy)) {
					// Add the copied edge to the vertices
					Edge edgeCopy = GridFactory.addEdge(firstVertexCopy, secondVertexCopy, edge.getWeight());

					// Add the copied vertex to the reference map
					edgeCopies.put(edge, edgeCopy);
				}
			}
		}

		// Copy the path
		Path pathCopy = scenarioCopy.getPath();
		PathUpdater pathCopyUpdater = new PathUpdater(pathCopy);
		for (Edge edge : this.path.getEdges()) {
			pathCopyUpdater.addEdge(edgeCopies.get(edge));
		}
		pathCopyUpdater.updatePath();

		// Copy the name
		scenarioCopy.setName(this.getName());

		// Copy the starting vertex
		scenarioCopy.setStartingVertex(vertexCopies.get(this.getStartingVertex()));

		return scenarioCopy;
	}

	public boolean isScenarioValid() {
		return this.validator.validateScenario(this);
	}

	public boolean isGridValid() {
		return this.validator.validateGrid(this, this.grid);
	}

	public boolean isPathValid() {
		return this.validator.validatePath(this, this.grid, this.path);
	}

	public Validator getValidator() {
		return this.validator;
	}

	public Grid getGrid() {
		return this.grid;
	}

	public Path getPath() {
		return this.path;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vertex getStartingVertex() {
		return this.startingVertex;
	}

	public void setStartingVertex(Vertex startingVertex) {
		if (this.startingVertex != null) {
			this.fireStartingVertexUpdate(this.startingVertex, StartingVertexUpdateAction.REMOVE_STARTING_NODE);
		}

		this.startingVertex = startingVertex;

		if (this.startingVertex != null) {
			this.fireStartingVertexUpdate(this.startingVertex, StartingVertexUpdateAction.ADD_STARTING_NODE);
		}
	}

	private void fireStartingVertexUpdate(Vertex vertex, StartingVertexUpdateAction action) {
		StartingVertexUpdate update = new StartingVertexUpdate(vertex, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
