package tspsolver.model.grid;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class Grid extends Observable {

	public static final boolean LINK_ADDED_NODE = true;

	private final Set<Node> nodes;
	private Node startingNode;

	public Grid() {
		this.nodes = new HashSet<Node>();
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public boolean containsNode(Node node) {
		return nodes.contains(node);
	}

	public synchronized Node addNode(Node node) {
		return this.addNode(node, Grid.LINK_ADDED_NODE);
	}

	public synchronized Node addNode(Node node, boolean link) {
		if (!this.containsNode(node)) {
			if (link) {
				for (Node n : this.getNodes()) {
					n.addEdgeToNode(node);
				}
			}

			this.nodes.add(node);
			this.setChanged();
			this.notifyObservers();
		}

		return node;
	}

	public synchronized void removeNode(Node node) {
		if (this.containsNode(node)) {
			for (Node n : this.getNodes()) {
				n.removeEdgeToNode(node);
			}

			this.nodes.remove(node);
			this.setChanged();
			this.notifyObservers();
		}
	}

	public synchronized void clearNodes() {
		for (Node node : this.getNodes()) {
			this.removeNode(node);
		}
	}

	public Node getStartingNode() {
		return startingNode;
	}

	public void setStartingNode(Node startingNode) {
		this.startingNode = startingNode;

		this.setChanged();
		this.notifyObservers();
	}

}
