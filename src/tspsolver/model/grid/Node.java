package tspsolver.model.grid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.updates.EdgeUpdate;
import tspsolver.model.updates.UpdateAction;

public class Node extends Observable implements Observer {

	private final int x;
	private final int y;

	private final Map<Node, Edge> edges;

	protected Node(int x, int y) {
		this.x = x;
		this.y = y;

		this.edges = new HashMap<Node, Edge>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + this.x;
		result = prime * result + this.y;

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

		Node other = (Node) obj;

		if (this.x != other.x) {
			return false;
		}

		if (this.y != other.y) {
			return false;
		}

		return true;
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Edge[] getEdges() {
		Collection<Edge> edges = this.edges.values();
		return edges.toArray(new Edge[edges.size()]);
	}

	public int getNumberOfEdges() {
		return this.edges.size();
	}

	public boolean hasEdgeToNode(Node node) {
		return this.edges.containsKey(node);
	}

	public Edge getEdgeToNode(Node node) {
		return this.edges.get(node);
	}

	protected Edge addEdgeToNode(Node node) {
		double weight = Edge.calcLinearDistance(this, node);
		return this.addEdgeToNode(node, weight);
	}

	protected synchronized Edge addEdgeToNode(Node node, double weight) {
		Edge edge = new Edge(this, node, weight);

		// Add the edge to this node
		if (!this.hasEdgeToNode(node)) {
			this.addEdge(node, edge);
		}

		// Add the edge to the oder node
		if (!node.hasEdgeToNode(this)) {
			node.addEdge(this, edge);
		}

		return edge;
	}

	protected synchronized void removeEdgeToNode(Node node) {
		// Remove the edge from this node
		if (!this.hasEdgeToNode(node)) {
			this.removeEdge(node);
		}

		// Remove the edge from the oder node
		if (!node.hasEdgeToNode(this)) {
			node.removeEdge(this);
		}
	}

	protected synchronized void clearEdges() {
		for (Node node : this.edges.keySet()) {
			this.removeEdgeToNode(node);
		}
	}

	private void addEdge(Node toNode, Edge edge) {
		edge.addObserver(this);

		this.edges.put(toNode, edge);
		this.fireEdgeUpdate(edge, UpdateAction.ADD_EDGE);
	}

	private void removeEdge(Node toNode) {
		Edge edge = this.edges.get(toNode);
		edge.deleteObserver(this);

		this.edges.remove(toNode);
		this.fireEdgeUpdate(edge, UpdateAction.REMOVE_EDGE);
	}

	private void fireEdgeUpdate(Edge edge, UpdateAction action) {
		EdgeUpdate update = new EdgeUpdate(edge, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
