package model.grid;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;

public class Node extends Observable {

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

		result = prime * result + x;
		result = prime * result + y;

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

		Node other = (Node) obj;

		if (x != other.x)
			return false;

		if (y != other.y)
			return false;

		return true;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Map<Node, Edge> getEdges() {
		return this.edges;
	}

	public Collection<Edge> getEdgeCollection() {
		return this.edges.values();
	}

	public Collection<Edge> getAccessibleEdgeCollection() {
		Collection<Edge> accessibleEdges = new HashSet<Edge>();

		for (Edge edge : this.getEdgeCollection()) {
			if (edge.isAccessible()) {
				accessibleEdges.add(edge);
			}
		}

		return accessibleEdges;
	}

	public int getNumberOfEdges() {
		return this.edges.size();
	}

	public boolean hasEdgeToNode(Node node) {
		return this.hasEdgeToNode(node, Edge.DEFAULT_ACCESS);
	}

	public boolean hasEdgeToNode(Node node, boolean accessible) {
		Edge edge = this.getEdgeToNode(node, accessible);
		return edge != null;
	}

	public Edge getEdgeToNode(Node node) {
		return this.getEdgeToNode(node, Edge.DEFAULT_ACCESS);
	}

	public Edge getEdgeToNode(Node node, boolean accessible) {
		Edge edge = this.getEdges().get(node);

		// Return null if the edge must be accessible but isn't
		if (edge != null && accessible && !edge.isAccessible()) {
			edge = null;
		}

		return edge;
	}

	protected Edge addEdgeToNode(Node node) {
		boolean accessible = Edge.DEFAULT_ACCESS;
		return this.addEdgeToNode(node, accessible);
	}

	protected Edge addEdgeToNode(Node node, double weight) {
		boolean accessible = Edge.DEFAULT_ACCESS;
		return this.addEdgeToNode(node, weight, accessible);
	}

	protected Edge addEdgeToNode(Node node, boolean accessible) {
		double weight = Edge.calcLinearDistance(this, node);
		return this.addEdgeToNode(node, weight, accessible);
	}

	protected synchronized Edge addEdgeToNode(Node node, double weight, boolean accessible) {
		Edge edge = new Edge(this, node, weight, accessible);

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
		for (Node node : this.getEdges().keySet()) {
			this.removeEdgeToNode(node);
		}
	}

	private void addEdge(Node node, Edge edge) {
		this.edges.put(node, edge);

		this.setChanged();
		this.notifyObservers(this);
	}

	private void removeEdge(Node node) {
		this.edges.remove(node);

		this.setChanged();
		this.notifyObservers(this);
	}

}
