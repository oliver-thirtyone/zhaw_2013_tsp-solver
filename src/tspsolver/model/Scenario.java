package tspsolver.model;

import java.util.Observable;
import java.util.Observer;

import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.updates.NodeUpdate;
import tspsolver.model.updates.StartingNodeUpdate;
import tspsolver.model.updates.UpdateAction;

public class Scenario extends Observable implements Observer {

	private final Path path;
	private final Grid grid;

	private Node startingNode;

	public Scenario() {
		this.path = new Path();
		this.grid = new Grid();

		this.startingNode = null;

		this.grid.addObserver(this);
		this.path.addObserver(this);
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	public Path getPath() {
		return path;
	}

	public Grid getGrid() {
		return grid;
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
