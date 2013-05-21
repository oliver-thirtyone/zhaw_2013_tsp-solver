package tspsolver.model.scenario.grid;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.updates.grid.NodeUpdate;
import tspsolver.model.updates.grid.NodeUpdateAction;

public class Grid extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = 8471461234146693504L;

	public static final boolean LINK_ADDED_NODE = true;

	private final Map<String, Node> nodes;

	public Grid() {
		this.nodes = new HashMap<String, Node>();
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;

		result = prime * result + ((this.nodes == null) ? 0 : this.nodes.hashCode());

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

		Grid other = (Grid) obj;
		if (this.nodes == null) {
			if (other.nodes != null) {
				return false;
			}
		}
		else if (!this.nodes.equals(other.nodes)) {
			return false;
		}

		return true;
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	public Node[] getNodes() {
		return this.nodes.values().toArray(new Node[this.nodes.size()]);
	}

	public int getNumberOfNodes() {
		return this.nodes.size();
	}

	protected Node getNode(String name) {
		return this.nodes.get(name);
	}

	protected boolean containsNode(Node node) {
		return this.nodes.containsKey(node.getName());
	}

	protected void addNode(Node node) {
		this.addNode(node, Grid.LINK_ADDED_NODE);
	}

	protected synchronized void addNode(Node node, boolean link) {
		if (!this.containsNode(node)) {
			if (link) {
				for (Node n : this.getNodes()) {
					n.addEdgeToNode(node);
				}
			}
			node.addObserver(this);

			this.nodes.put(node.getName(), node);
			this.fireNodeUpdate(node, NodeUpdateAction.ADD_NODE);
		}
	}

	protected synchronized void removeNode(Node node) {
		if (this.containsNode(node)) {
			for (Node n : this.getNodes()) {
				n.removeEdgeToNode(node);
			}
			node.deleteObserver(this);

			this.nodes.remove(node);
			this.fireNodeUpdate(node, NodeUpdateAction.REMOVE_NODE);
		}
	}

	protected synchronized void clear() {
		for (Node node : this.getNodes()) {
			this.removeNode(node);
		}
	}

	private void fireNodeUpdate(Node node, NodeUpdateAction action) {
		NodeUpdate update = new NodeUpdate(node, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
