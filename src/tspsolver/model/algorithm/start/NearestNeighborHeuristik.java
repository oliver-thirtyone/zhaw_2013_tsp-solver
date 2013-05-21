package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Vertex;

public class NearestNeighborHeuristik extends StartAlgorithm {

	private final Set<Vertex> verticesToVisit;

	private Vertex currentVertex;

	public NearestNeighborHeuristik() {
		this.verticesToVisit = new HashSet<Vertex>();

		this.reset();
	}

	@Override
	protected void doInitialize() {
		for (Vertex vertex : this.getGrid().getVertices()) {
			this.verticesToVisit.add(vertex);
		}
		this.setCurrentVertex(this.getStartingVertex());
	}

	@Override
	protected void doReset() {
		this.verticesToVisit.clear();
		this.setCurrentVertex(null);
	}

	@Override
	protected boolean doStep() {
		boolean successfulStep = true;

		// Remove the current vertex from the set of vertices to visit
		this.verticesToVisit.remove(this.getCurrentVertex());

		if (this.verticesToVisit.size() > 0) {
			Edge shortestEdge = null;

			// Get all available edges from the current vertex
			Edge[] edges = this.getCurrentVertex().getEdges();

			// Get the shortest edge to a vertex that we still have to visit
			for (Edge edge : edges) {

				// Does this edge lead to a vertex that we still have to visit?
				boolean validEdge = false;
				for (Vertex vertexToVisit : this.verticesToVisit) {
					if (edge.getFirstVertex() == vertexToVisit || edge.getSecondVertex() == vertexToVisit) {
						validEdge = true;
						break;
					}
				}

				// If this edge does not lead to a vertex we want to visit we will continue with the next one
				if (!validEdge) {
					continue;
				}

				// Check if this edge is the shortest
				if (shortestEdge == null || shortestEdge.getWeight() > edge.getWeight()) {
					shortestEdge = edge;
				}

			}

			if (shortestEdge != null) {
				// Add the new edge to the path
				this.getPathUpdater().addEdge(shortestEdge);

				// Set the new current vertex
				if (shortestEdge.getFirstVertex() == this.getCurrentVertex()) {
					this.setCurrentVertex(shortestEdge.getSecondVertex());
				}
				else {
					this.setCurrentVertex(shortestEdge.getFirstVertex());
				}
			}
			else {
				// No edge found, we stop here...
				successfulStep = false;
			}
		}
		else {
			Edge lastEdge = this.getCurrentVertex().getEdgeToVertex(this.getStartingVertex());

			// Link the last vertex with the starting vertex
			if (lastEdge != null) {
				this.getPathUpdater().addEdge(lastEdge);
				this.setCurrentVertex(this.getStartingVertex());

				this.finishedSuccessfully();
			}
			else {
				// There is no edge to the starting vertex
				// The algorithm fails
				successfulStep = false;
			}
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	public Vertex getCurrentVertex() {
		return this.currentVertex;
	}

	protected void setCurrentVertex(Vertex currentVertex) {
		this.currentVertex = currentVertex;
	}
}
