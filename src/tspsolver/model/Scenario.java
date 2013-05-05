package tspsolver.model;

import java.util.Observable;
import java.util.Observer;

import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;
import tspsolver.model.updates.NodeUpdate;
import tspsolver.model.updates.StartingNodeUpdate;
import tspsolver.model.updates.UpdateAction;

public class Scenario extends Observable implements Observer {

	private final String name;

	private final Grid grid;
	private final Path path;
	private final PathUpdater pathUpdater;

	private Node startingNode;

	public Scenario(String name) {
		this.name = name;

		this.grid = new Grid();
		this.path = new Path();
		this.pathUpdater = new PathUpdater(path);

		this.startingNode = null;

		this.grid.addObserver(this);
		this.path.addObserver(this);
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

	public synchronized void clear() {
		this.setStartingNode(null);

		this.getPathUpdater().clearPath();
		this.getPathUpdater().updatePath();

		this.getGrid().clear();
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

	public PathUpdater getPathUpdater() {
		return pathUpdater;
	}

	public Node getStartingNode() {
		return this.startingNode;
	}

	public void setStartingNode(Node startingNode) {
		if (this.startingNode != null) {
			this.fireStartingNodeUpdate(this.startingNode, UpdateAction.REMOVE_STARTING_NODE);
		}

		this.startingNode = startingNode;

		if (this.startingNode != null) {
			this.fireStartingNodeUpdate(this.startingNode, UpdateAction.ADD_STARTING_NODE);
		}
	}

	private void fireStartingNodeUpdate(Node node, UpdateAction action) {
		NodeUpdate update = new StartingNodeUpdate(node, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
