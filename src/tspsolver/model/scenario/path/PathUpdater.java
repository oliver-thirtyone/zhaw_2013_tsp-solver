package tspsolver.model.scenario.path;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.updates.path.PathUpdateAction;

public class PathUpdater {

	private final Path path;

	private final List<Edge> edgesToAdd;
	private final List<Edge> edgesToRemove;

	private final List<Edge> addedEdges;
	private final List<Edge> removedEdges;

	public PathUpdater(Path path) {
		this.path = path;

		this.edgesToAdd = new LinkedList<Edge>();
		this.edgesToRemove = new LinkedList<Edge>();

		this.addedEdges = new LinkedList<Edge>();
		this.removedEdges = new LinkedList<Edge>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((this.addedEdges == null) ? 0 : this.addedEdges.hashCode());
		result = prime * result + ((this.edgesToAdd == null) ? 0 : this.edgesToAdd.hashCode());
		result = prime * result + ((this.edgesToRemove == null) ? 0 : this.edgesToRemove.hashCode());
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
		result = prime * result + ((this.removedEdges == null) ? 0 : this.removedEdges.hashCode());

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

		PathUpdater other = (PathUpdater) obj;
		if (this.addedEdges == null) {
			if (other.addedEdges != null) {
				return false;
			}
		}
		else if (!this.addedEdges.equals(other.addedEdges)) {
			return false;
		}

		if (this.edgesToAdd == null) {
			if (other.edgesToAdd != null) {
				return false;
			}
		}
		else if (!this.edgesToAdd.equals(other.edgesToAdd)) {
			return false;
		}

		if (this.edgesToRemove == null) {
			if (other.edgesToRemove != null) {
				return false;
			}
		}
		else if (!this.edgesToRemove.equals(other.edgesToRemove)) {
			return false;
		}

		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		}
		else if (!this.path.equals(other.path)) {
			return false;
		}

		if (this.removedEdges == null) {
			if (other.removedEdges != null) {
				return false;
			}
		}
		else if (!this.removedEdges.equals(other.removedEdges)) {
			return false;
		}

		return true;
	}

	public synchronized void updatePath() {
		// Mark the added edges as a path element
		for (final Edge edge : this.addedEdges) {
			this.path.firePathUpdate(edge, PathUpdateAction.PATH_ELEMENT);
		}
		this.addedEdges.clear();

		// Mark the removed edges as a normal edge
		for (final Edge edge : this.removedEdges) {
			this.path.firePathUpdate(edge, PathUpdateAction.NON_PATH_ELEMENT);
		}
		this.removedEdges.clear();

		// Add the new edges and mark them as a new path element
		for (final Edge edge : this.edgesToAdd) {
			this.path.addEdge(edge);
			this.path.firePathUpdate(edge, PathUpdateAction.NEW_PATH_ELEMENT);
		}
		this.addedEdges.addAll(this.edgesToAdd);
		this.edgesToAdd.clear();

		// Remove the old edges and mark them as a old path element
		for (final Edge edge : this.edgesToRemove) {
			this.path.removeEdge(edge);
			this.path.firePathUpdate(edge, PathUpdateAction.OLD_PATH_ELEMENT);
		}
		this.removedEdges.addAll(this.edgesToRemove);
		this.edgesToRemove.clear();
	}

	public synchronized void addEdge(Edge edge) {
		if (this.edgesToRemove.contains(edge)) {
			this.edgesToRemove.remove(edge);
		}
		else if (!this.edgesToAdd.contains(edge) && !this.path.containsEdge(edge)) {
			this.edgesToAdd.add(edge);
		}
	}

	public synchronized void removeEdge(Edge edge) {
		if (this.edgesToAdd.contains(edge)) {
			this.edgesToAdd.remove(edge);
		}
		else if (!this.edgesToRemove.contains(edge) && this.path.containsEdge(edge)) {
			this.edgesToRemove.add(edge);
		}
	}

	public synchronized void addEdges(Edge[] edges) {
		for (final Edge edge : edges) {
			this.addEdge(edge);
		}
	}

	public synchronized void removeEdges(Edge[] edges) {
		for (final Edge edge : edges) {
			this.removeEdge(edge);
		}
	}

	public synchronized void addEdges(Collection<Edge> edges) {
		for (final Edge edge : edges) {
			this.addEdge(edge);
		}
	}

	public synchronized void removeEdges(Collection<Edge> edges) {
		for (final Edge edge : edges) {
			this.removeEdge(edge);
		}
	}

	public void addPath(Path path) {
		this.addEdges(path.getEdges());
	}

	public void removePath(Path path) {
		this.removeEdges(path.getEdges());
	}

	public void clearPath() {
		this.removePath(this.path);
	}

	public final Path getPath() {
		return this.path;
	}
}
