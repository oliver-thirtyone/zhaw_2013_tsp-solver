package tspsolver.model.scenario.grid;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import tspsolver.model.updates.grid.VertexUpdate;
import tspsolver.model.updates.grid.VertexUpdateAction;

public class Grid extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = 8471461234146693504L;

	public static final boolean LINK_ADDED_NODE = true;

	private final Map<String, Vertex> vertices;

	public Grid() {
		this.vertices = new HashMap<String, Vertex>();
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;

		result = prime * result + ((this.vertices == null) ? 0 : this.vertices.hashCode());

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
		if (this.vertices == null) {
			if (other.vertices != null) {
				return false;
			}
		}
		else if (!this.vertices.equals(other.vertices)) {
			return false;
		}

		return true;
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	public Vertex[] getVertices() {
		return this.vertices.values().toArray(new Vertex[this.vertices.size()]);
	}

	public int getNumberOfVertices() {
		return this.vertices.size();
	}

	protected Vertex getVertex(String name) {
		return this.vertices.get(name);
	}

	protected boolean containsVertex(Vertex vertex) {
		return this.vertices.containsKey(vertex.getName());
	}

	protected void addVertex(Vertex vertex) {
		this.addVertex(vertex, Grid.LINK_ADDED_NODE);
	}

	protected synchronized void addVertex(Vertex vertex, boolean link) {
		if (!this.containsVertex(vertex)) {
			if (link) {
				for (Vertex n : this.getVertices()) {
					n.addEdgeToVertex(vertex);
				}
			}
			vertex.addObserver(this);

			this.vertices.put(vertex.getName(), vertex);
			this.fireVertexUpdate(vertex, VertexUpdateAction.ADD_NODE);
		}
	}

	protected synchronized void removeVertex(Vertex vertex) {
		if (this.containsVertex(vertex)) {
			for (Vertex n : this.getVertices()) {
				n.removeEdgeToVertex(vertex);
			}
			vertex.deleteObserver(this);

			this.vertices.remove(vertex);
			this.fireVertexUpdate(vertex, VertexUpdateAction.REMOVE_NODE);
		}
	}

	protected synchronized void clear() {
		for (Vertex vertex : this.getVertices()) {
			this.removeVertex(vertex);
		}
	}

	private void fireVertexUpdate(Vertex vertex, VertexUpdateAction action) {
		VertexUpdate update = new VertexUpdate(vertex, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
