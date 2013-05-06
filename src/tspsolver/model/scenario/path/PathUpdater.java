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

	public synchronized void updatePath() {
		// Mark the added edges as a path element
		for (Edge edge : this.addedEdges) {
			this.path.firePathUpdate(edge, PathUpdateAction.PATH_ELEMENT);
		}
		this.addedEdges.clear();

		// Mark the removed edges as a normal edge
		for (Edge edge : this.removedEdges) {
			this.path.firePathUpdate(edge, PathUpdateAction.NON_PATH_ELEMENT);
		}
		this.removedEdges.clear();

		// Add the new edges and mark them as a new path element
		for (Edge edge : this.edgesToAdd) {
			this.path.addEdge(edge);
			this.path.firePathUpdate(edge, PathUpdateAction.NEW_PATH_ELEMENT);
		}
		this.addedEdges.addAll(this.edgesToAdd);
		this.edgesToAdd.clear();

		// Remove the old edges and mark them as a old path element
		for (Edge edge : this.edgesToRemove) {
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
		for (Edge edge : edges) {
			this.addEdge(edge);
		}
	}

	public synchronized void removeEdges(Edge[] edges) {
		for (Edge edge : edges) {
			this.removeEdge(edge);
		}
	}

	public synchronized void addEdges(Collection<Edge> edges) {
		for (Edge edge : edges) {
			this.addEdge(edge);
		}
	}

	public synchronized void removeEdges(Collection<Edge> edges) {
		for (Edge edge : edges) {
			this.removeEdge(edge);
		}
	}

	public synchronized void addPath(Path path) {
		this.addEdges(path.getEdges());
	}

	public synchronized void removePath(Path path) {
		this.removeEdges(path.getEdges());
	}

	public synchronized void clearPath() {
		this.removePath(this.path);
	}

	public final Path getPath() {
		return this.path;
	}
}
