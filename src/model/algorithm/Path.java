package model.algorithm;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import model.grid.Edge;

public class Path extends Observable {

	private final Set<Edge> edges;
	private double weight;

	public Path() {
		this.edges = new HashSet<Edge>();
		this.weight = 0.0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((this.edges == null) ? 0 : this.edges.hashCode());

		long temp = Double.doubleToLongBits(this.weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));

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

		if (this.edges == null) {
			if (other.edges != null) {
				return false;
			}
		} else if (!this.edges.equals(other.edges)) {
			return false;
		}

		if (Double.doubleToLongBits(this.weight) != Double.doubleToLongBits(other.weight)) {
			return false;
		}

		return true;
	}

	public Set<Edge> getEdges() {
		return this.edges;
	}

	public boolean containsEdge(Edge edge) {
		return this.edges.contains(edge);
	}

	public synchronized void addPath(Path path) {
		this.addEdges(path.getEdges());
	}

	public synchronized void addEdges(Set<Edge> edges) {
		for (Edge edge : edges) {
			this.addEdge(edge);
		}
	}

	public synchronized void addEdge(Edge edge) {
		if (this.edges.add(edge)) {
			this.weight += edge.getWeight();

			this.setChanged();
			this.notifyObservers(this);
		}
	}

	public synchronized void removePath(Path path) {
		this.removeEdges(path.getEdges());
	}

	public synchronized void removeEdges(Set<Edge> edges) {
		for (Edge edge : edges) {
			this.removeEdge(edge);
		}
	}

	public synchronized void removeEdge(Edge edge) {
		if (this.edges.remove(edge)) {
			this.weight -= edge.getWeight();

			this.setChanged();
			this.notifyObservers(this);
		}
	}

	public synchronized void clearEdges() {
		this.edges.clear();
		this.weight = 0.0;

		this.setChanged();
		this.notifyObservers(this);
	}

	public double getWeight() {
		return this.weight;
	}

	public int getNumberOfEdges() {
		return this.edges.size();
	}
}
