package tspsolver.model.scenario.grid;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.updates.grid.EdgeUpdate;
import tspsolver.model.updates.grid.EdgeUpdateAction;

public class Node extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = -5593471052000934706L;

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

		final Node other = (Node) obj;

		if (this.x != other.x) {
			return false;
		}

		if (this.y != other.y) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append("(");
		builder.append(this.getX());
		builder.append(", ");
		builder.append(this.getY());
		builder.append(")");

		return builder.toString();
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
		final Collection<Edge> edges = this.edges.values();
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
		final double weight = Edge.calcLinearDistance(this, node);
		return this.addEdgeToNode(node, weight);
	}

	protected synchronized Edge addEdgeToNode(Node node, double weight) {
		final Edge edge = new Edge(this, node, weight);

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
		if (this.hasEdgeToNode(node)) {
			this.removeEdge(node);
		}

		// Remove the edge from the other node
		if (node.hasEdgeToNode(this)) {
			node.removeEdge(this);
		}
	}

	protected synchronized void clearEdges() {
		for (final Node node : this.edges.keySet()) {
			this.removeEdgeToNode(node);
		}
	}

	private void addEdge(Node toNode, Edge edge) {
		edge.addObserver(this);

		this.edges.put(toNode, edge);
		this.fireEdgeUpdate(edge, EdgeUpdateAction.ADD_EDGE);
	}

	private void removeEdge(Node toNode) {
		final Edge edge = this.edges.get(toNode);
		edge.deleteObserver(this);

		this.edges.remove(toNode);
		this.fireEdgeUpdate(edge, EdgeUpdateAction.REMOVE_EDGE);
	}

	private void fireEdgeUpdate(Edge edge, EdgeUpdateAction action) {
		final EdgeUpdate update = new EdgeUpdate(edge, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
