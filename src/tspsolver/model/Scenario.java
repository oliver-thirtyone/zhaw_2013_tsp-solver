package tspsolver.model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.updates.StartingNodeUpdateAction;
import tspsolver.model.updates.StartingNodeUpdate;

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
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	@Override
	public String toString() {
		return this.getName();
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
