package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.comparators.grid.EdgeWeightTreeSetComparator;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Vertex;

public class MinimumSpanningTreeHeuristic extends StartAlgorithm {

	private enum Phase {
		CREATE_SPANNING_TREE, DO_EULERIAN_TRAIL
	}

	private final Vector<Edge> spanningTreeEdges;
	private final Set<Vertex> spanningTreeVertices;
	private TreeSet<Edge> spanningTreePossibleEdges;
	private final Stack<Vertex> brancheVertices;

	private Phase phase;
	private Vertex currentVertex;

	public MinimumSpanningTreeHeuristic() {
		this.spanningTreeEdges = new Vector<Edge>();
		this.spanningTreeVertices = new HashSet<Vertex>();

		this.spanningTreePossibleEdges = new TreeSet<Edge>(new EdgeWeightTreeSetComparator());
		this.brancheVertices = new Stack<Vertex>();

		this.reset();
	}

	@Override
	protected void doInitialize() {
		this.spanningTreeVertices.add(this.getStartingVertex());

		// Sort all possible edges by weight
		for (Edge edge : this.getStartingVertex().getEdges()) {
			this.spanningTreePossibleEdges.add(edge);
		}

		this.phase = Phase.CREATE_SPANNING_TREE;
	}

	@Override
	protected void doReset() {
		this.spanningTreeVertices.clear();
		this.spanningTreeEdges.clear();

		this.spanningTreePossibleEdges.clear();
		this.brancheVertices.clear();

		this.phase = null;
		this.currentVertex = null;
	}

	@Override
	public boolean doStep() {
		boolean success = false;
		switch (this.phase) {
			case CREATE_SPANNING_TREE:
				success = this.doStepCreateSpanningTree();
				break;
			case DO_EULERIAN_TRAIL:
				success = this.doStepEulerianTrail();
				break;
			default:
				break;
		}
		return success;
	}

	private boolean doStepCreateSpanningTree() {
		boolean successfulStep = false;

		TreeSet<Edge> newSpanningTreePossibleEdges = new TreeSet<Edge>(new EdgeWeightTreeSetComparator());
		newSpanningTreePossibleEdges.addAll(this.spanningTreePossibleEdges);

		// Take the lowest one, that not build a circle and add it to the tree.
		for (Edge edge : this.spanningTreePossibleEdges) {
			if (this.spanningTreeVertices.contains(edge.getFirstVertex()) == false) {

				// Add the edge to the spanning tree.
				this.spanningTreeEdges.add(edge);
				this.getPathUpdater().addEdge(edge);

				if (this.spanningTreeEdges.size() < this.getGrid().getNumberOfVertices() - 1) {

					// Prepare for next step.
					this.spanningTreeVertices.add(edge.getFirstVertex());

					// Add all new possible edges
					for (Edge edgeToAdd : edge.getFirstVertex().getEdges()) {
						newSpanningTreePossibleEdges.add(edgeToAdd);
					}

					// The edge is used now.
					newSpanningTreePossibleEdges.remove(edge);

					this.spanningTreePossibleEdges = newSpanningTreePossibleEdges;
				}
				else {
					// finish create spanning tree
					this.initEulerianTrail();
				}

				successfulStep = true;
				break;
			}
			else if (this.spanningTreeVertices.contains(edge.getSecondVertex()) == false) {

				// Add the edge to the spanning tree.
				this.spanningTreeEdges.add(edge);
				this.getPathUpdater().addEdge(edge);

				if (this.spanningTreeEdges.size() < this.getGrid().getNumberOfVertices() - 1) {

					// Prepare for next step.
					this.spanningTreeVertices.add(edge.getSecondVertex());

					// Add all new possible edges
					for (Edge edgeToAdd : edge.getSecondVertex().getEdges()) {
						newSpanningTreePossibleEdges.add(edgeToAdd);
					}

					// The edge is used now.
					newSpanningTreePossibleEdges.remove(edge);

					this.spanningTreePossibleEdges = newSpanningTreePossibleEdges;
				}
				else {
					// finish create spanning tree
					this.initEulerianTrail();
				}

				successfulStep = true;
				break;

			}
			else {
				// This edge will build a circle, remove it to improve
				// performance
				newSpanningTreePossibleEdges.remove(edge);
			}
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	private void initEulerianTrail() {
		this.phase = Phase.DO_EULERIAN_TRAIL;
		this.currentVertex = this.getStartingVertex();
		this.brancheVertices.push(this.currentVertex);
	}

	private boolean doStepEulerianTrail() {

		Vertex brancheVertex = this.brancheVertices.pop();

		int i = 1;
		while (this.spanningTreeEdges.isEmpty() == false) {
			Edge edge = this.spanningTreeEdges.elementAt(this.spanningTreeEdges.size() - i);

			// Find the next edge in the spanning tree that is connected to the
			// current branch vertex.
			if (edge.getFirstVertex() == brancheVertex) {

				Edge newEdge = this.currentVertex.getEdgeToVertex(edge.getSecondVertex());
				if (newEdge == null) {
					// Can't finish the tour
					return false;
				}

				// Remove the edge from the spanning tree, because this edge is
				// used
				this.spanningTreeEdges.remove(edge);

				this.getPathUpdater().removeEdge(edge);

				// Add the new edge to the path.
				this.getPathUpdater().addEdge(newEdge);

				// Set the other vertex as new current vertex.
				this.currentVertex = edge.getSecondVertex();

				this.brancheVertices.add(brancheVertex);
				this.brancheVertices.add(this.currentVertex);

				this.getPathUpdater().updatePath();
				return true;

			}
			else if (edge.getSecondVertex() == brancheVertex) {

				Edge newEdge = this.currentVertex.getEdgeToVertex(edge.getFirstVertex());
				if (newEdge == null) {
					// Can't finish the tour
					return false;
				}

				// Remove the edge from the spanning tree, because this edge is
				// used
				this.spanningTreeEdges.remove(edge);

				this.getPathUpdater().removeEdge(edge);

				// Add the new edge to the path.
				this.getPathUpdater().addEdge(newEdge);

				// Set the other vertex as new current vertex.
				this.currentVertex = edge.getFirstVertex();

				this.brancheVertices.add(brancheVertex);
				this.brancheVertices.add(this.currentVertex);

				this.getPathUpdater().updatePath();
				return true;

			}
			else if (i >= this.spanningTreeEdges.size()) {
				// A leaf from the spanning tree
				return true;
			}
			else {
				i++;
			}
		}

		// Finishing the eulerian trail:
		// Connect the last vertex from the eulerian path with the start vertex
		// to
		// close the circle
		Edge newEdge = this.getStartingVertex().getEdgeToVertex(this.currentVertex);
		if (newEdge == null) {
			// Can't finish the tour
			return false;
		}

		this.getPathUpdater().addEdge(newEdge);

		this.getPathUpdater().updatePath();
		this.finishedSuccessfully();
		return true;
	}
}
