package tspsolver.model.algorithm.optimizer;

import java.util.HashSet;
import java.util.Vector;

import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Vertex;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class TwoOptHeuristic extends OptimizerAlgorithm {

	private final Vector<Vertex> verticesInOrder;

	public TwoOptHeuristic() {
		this.verticesInOrder = new Vector<Vertex>();
	}

	@Override
	protected void doInitialize() {
		// Get the vertex in line
		Vertex currentVertex = this.getStartingVertex();

		this.verticesInOrder.add(currentVertex);

		int numberOfEdges = this.getPath().getNumberOfEdges();

		HashSet<Edge> edges = new HashSet<Edge>();
		for (Edge edge : this.getPath().getEdges()) {
			edges.add(edge);
		}

		while (this.verticesInOrder.size() < numberOfEdges) {
			for (Edge edge : edges) {
				if (edge.getFirstVertex() == currentVertex) {

					edges.remove(edge);

					currentVertex = edge.getSecondVertex();

					this.verticesInOrder.add(currentVertex);
					break;
				}
				else if (edge.getSecondVertex() == currentVertex) {

					edges.remove(edge);

					currentVertex = edge.getFirstVertex();

					this.verticesInOrder.add(currentVertex);
					break;
				}
			}

		}

	}

	@Override
	protected void doReset() {
		this.verticesInOrder.clear();
	}

	@Override
	protected boolean doStep() {

		int numberOfVertices = this.getPath().getNumberOfEdges();

		long lightestWeight = this.getPath().getWeight();

		for (int i = 0; i < numberOfVertices; i++) {
			for (int k = i + 1; k < numberOfVertices - 1; k++) {

				Path newPath = this.generateNewPath(i, k);
				if (newPath == null) {
					return false;
				}

				long newWeight = newPath.getWeight();

				if (newWeight < lightestWeight) {

					this.getPathUpdater().clearPath();
					this.getPathUpdater().addPath(newPath);
					this.getPathUpdater().updatePath();
					return true;
				}
			}
		}

		this.finishedSuccessfully();
		return true;
	}

	private Path generateNewPath(int i, int k) {

		Vector<Vertex> newVertexPath = new Vector<Vertex>();

		for (int j = 0; j < i; j++) {
			newVertexPath.add(this.verticesInOrder.get(j));
		}

		for (int j = k; j >= i; j--) {
			newVertexPath.add(this.verticesInOrder.get(j));
		}

		for (int j = k + 1; j < this.verticesInOrder.size(); j++) {
			newVertexPath.add(this.verticesInOrder.get(j));
		}

		return this.convertVerticesToPath(newVertexPath);
	}

	private Path convertVerticesToPath(Vector<Vertex> vertexPath) {

		Path path = new Path();
		PathUpdater pathUpdater = new PathUpdater(path);

		for (int j = 1; j <= this.verticesInOrder.size(); j++) {
			Vertex firstVertex = vertexPath.get(j - 1);
			Vertex secondVertex = (j == this.verticesInOrder.size()) ? vertexPath.get(0) : vertexPath.get(j);

			Edge edge = firstVertex.getEdgeToVertex(secondVertex);
			if (edge == null) {
				// Can't finish the tour
				return null;
			}

			pathUpdater.addEdge(edge);
		}

		pathUpdater.updatePath();

		return pathUpdater.getPath();
	}

}
