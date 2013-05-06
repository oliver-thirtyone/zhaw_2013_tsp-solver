package tspsolver.model.scenario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;
import tspsolver.model.updates.scenario.StartingNodeUpdate;
import tspsolver.model.updates.scenario.StartingNodeUpdateAction;

public class Scenario extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = -7758506719837963384L;

	private final String name;

	private final Grid grid;
	private final Path path;

	private Node startingNode;

	public Scenario(String name) {
		this.name = name;

		this.grid = new Grid();
		this.path = new Path();

		this.startingNode = null;

		this.grid.addObserver(this);
		this.path.addObserver(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((startingNode == null) ? 0 : startingNode.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Scenario other = (Scenario) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;

		if (grid == null) {
			if (other.grid != null)
				return false;
		}
		else if (!grid.equals(other.grid))
			return false;

		if (path == null) {
			if (other.path != null)
				return false;
		}
		else if (!path.equals(other.path))
			return false;

		if (startingNode == null) {
			if (other.startingNode != null)
				return false;
		}
		else if (!startingNode.equals(other.startingNode))
			return false;

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
		Scenario scenarioCopy = new Scenario(this.getName());

		// Copy the gird
		Grid gridCopy = scenarioCopy.getGrid();

		// Copy the nodes
		Map<Node, Node> nodeCopies = new HashMap<Node, Node>();
		for (Node node : grid.getNodes()) {
			// Create copy of each node
			Node nodeCopy = GridFactory.createNode(node.getX(), node.getY());

			// Add the copied node to the copied grid
			GridFactory.addNode(gridCopy, nodeCopy, false);

			// Add the copied node to the reference map
			nodeCopies.put(node, nodeCopy);
		}

		// Copy the edges
		Map<Edge, Edge> edgeCopies = new HashMap<Edge, Edge>();
		for (Node node : grid.getNodes()) {
			for (Edge edge : node.getEdges()) {
				Node firstNodeCopy = nodeCopies.get(edge.getFirstNode());
				Node secondNodeCopy = nodeCopies.get(edge.getSecondNode());

				if (!GridFactory.hasEdge(firstNodeCopy, secondNodeCopy)) {
					// Add the copied edge to the nodes
					Edge edgeCopy = GridFactory.addEdge(firstNodeCopy, secondNodeCopy, edge.getWeight());

					// Add the copied node to the reference map
					edgeCopies.put(edge, edgeCopy);
				}
			}
		}

		// Copy the path
		Path pathCopy = scenarioCopy.getPath();
		PathUpdater pathCopyUpdater = new PathUpdater(pathCopy);
		for (Edge edge : path.getEdges()) {
			pathCopyUpdater.addEdge(edgeCopies.get(edge));
		}
		pathCopyUpdater.updatePath();

		// Copy the starting node
		scenarioCopy.setStartingNode(nodeCopies.get(this.getStartingNode()));

		return scenarioCopy;
	}

	public String getName() {
		return this.name;
	}

	public Grid getGrid() {
		return grid;
	}

	public Path getPath() {
		return path;
	}

	public Node getStartingNode() {
		return this.startingNode;
	}

	public void setStartingNode(Node startingNode) {
		if (this.startingNode != null) {
			this.fireStartingNodeUpdate(this.startingNode, StartingNodeUpdateAction.REMOVE_STARTING_NODE);
		}

		this.startingNode = startingNode;

		if (this.startingNode != null) {
			this.fireStartingNodeUpdate(this.startingNode, StartingNodeUpdateAction.ADD_STARTING_NODE);
		}
	}

	private void fireStartingNodeUpdate(Node node, StartingNodeUpdateAction action) {
		StartingNodeUpdate update = new StartingNodeUpdate(node, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
