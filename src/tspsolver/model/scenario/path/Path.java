package tspsolver.model.scenario.path;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.updates.path.PathUpdate;
import tspsolver.model.updates.path.PathUpdateAction;

public class Path extends Observable implements Serializable {

	private static final long serialVersionUID = 8102936507138748772L;

	private final Set<Edge> edges;
	private int weight;

	public Path() {
		this.edges = new HashSet<Edge>();
		this.weight = 0;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;

		result = prime * result + ((this.edges == null) ? 0 : this.edges.hashCode());
		result = prime * result + this.weight;

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

		Path other = (Path) obj;

		if (this.weight != other.weight) {
			return false;
		}

		if (this.edges == null) {
			if (other.edges != null) {
				return false;
			}
		}
		else if (!this.edges.equals(other.edges)) {
			return false;
		}

		return true;
	}

	public int getWeight() {
		return this.weight;
	}

	public Edge[] getEdges() {
		return this.edges.toArray(new Edge[this.edges.size()]);
	}

	public int getNumberOfEdges() {
		return this.edges.size();
	}

	public boolean isEmpty() {
		return this.getNumberOfEdges() < 1;
	}

	public boolean containsEdge(Edge edge) {
		return this.edges.contains(edge);
	}

	protected synchronized void addEdge(Edge edge) {
		if (this.edges.add(edge)) {
			this.weight += edge.getWeight();
		}
	}

	protected synchronized void removeEdge(Edge edge) {
		if (this.edges.remove(edge)) {
			this.weight -= edge.getWeight();
		}
	}

	protected void firePathUpdate(Edge edge, PathUpdateAction action) {
		PathUpdate update = new PathUpdate(edge, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
