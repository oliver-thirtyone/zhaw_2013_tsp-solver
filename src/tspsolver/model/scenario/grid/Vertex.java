package tspsolver.model.scenario.grid;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.updates.grid.EdgeUpdate;
import tspsolver.model.updates.grid.EdgeUpdateAction;

public class Vertex extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = -5593471052000934706L;

	private final String name;
	private final int x;
	private final int y;

	private final Map<Vertex, Edge> edges;

	protected Vertex(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;

		this.edges = new HashMap<Vertex, Edge>();
	}

	@Override
	public int hashCode() {
		int prime = 31;
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

		Vertex other = (Vertex) obj;

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
		StringBuilder builder = new StringBuilder();

		builder.append(this.getName());
		builder.append(": ");
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

	public String getName() {
		return this.name;
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

	public boolean hasEdgeToVertex(Vertex vertex) {
		return this.edges.containsKey(vertex);
	}

	public Edge getEdgeToVertex(Vertex vertex) {
		return this.edges.get(vertex);
	}

	protected Edge addEdgeToVertex(Vertex vertex) {
		double weight = Edge.calcLinearDistance(this, vertex);
		return this.addEdgeToVertex(vertex, weight);
	}

	protected synchronized Edge addEdgeToVertex(Vertex vertex, double weight) {
		Edge edge = new Edge(this, vertex, weight);

		// Add the edge to this vertex
		if (!this.hasEdgeToVertex(vertex)) {
			this.addEdge(vertex, edge);
		}

		// Add the edge to the oder vertex
		if (!vertex.hasEdgeToVertex(this)) {
			vertex.addEdge(this, edge);
		}

		return edge;
	}

	protected synchronized void removeEdgeToVertex(Vertex vertex) {
		// Remove the edge from this vertex
		if (this.hasEdgeToVertex(vertex)) {
			this.removeEdge(vertex);
		}

		// Remove the edge from the other vertex
		if (vertex.hasEdgeToVertex(this)) {
			vertex.removeEdge(this);
		}
	}

	protected synchronized void clearEdges() {
		for (Vertex vertex : this.edges.keySet()) {
			this.removeEdgeToVertex(vertex);
		}
	}

	private void addEdge(Vertex toVertex, Edge edge) {
		edge.addObserver(this);

		this.edges.put(toVertex, edge);
		this.fireEdgeUpdate(edge, EdgeUpdateAction.ADD_EDGE);
	}

	private void removeEdge(Vertex toVertex) {
		Edge edge = this.edges.get(toVertex);
		edge.deleteObserver(this);

		this.edges.remove(toVertex);
		this.fireEdgeUpdate(edge, EdgeUpdateAction.REMOVE_EDGE);
	}

	private void fireEdgeUpdate(Edge edge, EdgeUpdateAction action) {
		EdgeUpdate update = new EdgeUpdate(edge, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
