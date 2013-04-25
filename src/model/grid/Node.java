package model.grid;

import java.util.HashSet;
import java.util.Set;

public class Node {

	// TODO: Wenn ein Punkt nur über eine Kante erreichbar erreichbar ist schlägt jeder Algorithmus fehl => Wir müssen alle Punkte zuerst validieren bevor wir starten
	
	
	private final Integer x;
	private final Integer y;

	private final Set<Edge> edges;

	public Node(Integer x, Integer y) {
		this.x = x;
		this.y = y;

		this.edges = new HashSet<Edge>();
	}

	public Integer getX() {
		return this.x;
	}

	public Integer getY() {
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
		return getEdgeToNode(node, Boolean.TRUE);
	}

	public Edge getEdgeToNode(Node node, Boolean accessible) {
		Edge edgeToNode = null;

		for (Edge edge : this.edges) {
			if (edge.getFirstNode() == node || edge.getSecondNode() == node) {
				if (!accessible || edge.isAccessible()) {
					edgeToNode = edge;
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
