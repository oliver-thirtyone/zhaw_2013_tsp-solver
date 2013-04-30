package model.grid;

import java.util.HashSet;
import java.util.Set;

public class Node {

	// TODO: Wenn ein Punkt nur über eine Kante erreichbar erreichbar ist schlägt jeder Algorithmus fehl => Wir müssen alle Punkte zuerst validieren bevor wir starten

	private final int x;
	private final int y;

	private final Set<Edge> edges;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;

		this.edges = new HashSet<Edge>();
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

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Set<Edge> getEdges() {
		return this.edges;
	}

	public Set<Edge> getAccessibleEdges() {
		Set<Edge> accessibleEdges = new HashSet<Edge>();

		for (Edge edge : this.edges) {
			if (edge.isAccessible()) {
				accessibleEdges.add(edge);
			}
		}

		return accessibleEdges;
	}

	public Edge getEdgeToNode(Node node) {
		return this.getEdgeToNode(node, Boolean.TRUE);
	}

	public Edge getEdgeToNode(Node node, Boolean accessible) {
		Edge edgeToNode = null;

		for (Edge edge : this.getEdges()) {
			if (edge.getFirstNode() == node || edge.getSecondNode() == node) {
				if (!accessible || edge.isAccessible()) {
					edgeToNode = edge;
					break;
				}
			}
		}

		return edgeToNode;
	}

	protected void addEdge(Edge edge) {
		this.edges.add(edge);
	}

	protected void removeEdge(Edge edge) {
		this.edges.remove(edge);
	}

	protected void clearEdges(Edge edge) {
		this.edges.remove(edge);
	}

}
