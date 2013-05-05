package tspsolver.model.algorithm.optimizer;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import tspsolver.model.Scenario;
import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;

public class TwoOpt extends AOptimizerAlgorithm {

	private int i;
	private int k;

	public TwoOpt(Scenario scenario) {
		super(scenario);

		this.i = 0;
		this.k = 1;

		// TODO: Das ist nicht für jeden Start-Algorithmus nötig sondern nur für
		// die bei denen nicht gewährleistet ist das die Reihenfolge der Kanten
		// keinen Pfad bilden.
		bringEdgesInOrder(scenario.getStartingNode());
	}

	private void bringEdgesInOrder(Node startNode) {

		Node currentNode = startNode;

		Set<Edge> pathNotInOrder = this.getPathUpdater().getPath().getEdges();

		Set<Edge> pathInOrder = new HashSet<Edge>();

		while (pathNotInOrder.size() > 0) {
			for (Edge edge : pathNotInOrder) {
				if (currentNode.getEdges().contains(edge)) {

					pathInOrder.add(edge);

					if (edge.getFirstNode() == currentNode) {
						currentNode = edge.getSecondNode();
					} else if (edge.getSecondNode() == currentNode) {
						currentNode = edge.getFirstNode();
					}

					pathNotInOrder.remove(edge);
					break;
				}
			}
		}

		this.getPathUpdater().clearWholePath();
		this.getPathUpdater().addEdges(pathInOrder);
	}

	@Override
	protected boolean doStep() {

		if (i < this.getPathUpdater().getPath().getNumberOfEdges()) {

			Path newPath = swap(this.getPathUpdater().getPath(), i, k);

			if (k < this.getPathUpdater().getPath().getNumberOfEdges() - 1) {
				k++;
			} else {
				i++;
				k = i + 1;
			}

			if (newPath.getWeight() < this.getPathUpdater().getPath()
					.getWeight()) {
				// A shorter path was found.
				this.getPathUpdater().clearWholePath();
				this.getPathUpdater().addPath(newPath);
				return true;
			} else {
				// Nothing happen to the path, so recall the step.
				return doStep();
			}

		} else {
			this.setFinishedSuccessful(true);
			return true;
		}
	}

	// 2optSwap(route, i, k) {
	// 1. take route[0] to route[i-1] and add them in order to new_route
	// 2. take route[i] to route[k] and add them in reverse order to new_route
	// 3. take route[k+1] to end and add them in order to new_route
	// return new_route;
	// }
	private Path swap(Path path, int first, int second) {

		Vector<Edge> vectorPath = new Vector<Edge>(path.getEdges());
		Path newPath = new Path();

		for (int i = 0; i < first; i++) {
			newPath.getEdges().add(vectorPath.get(i));
		}

		for (int i = second; i >= first; i--) {
			newPath.getEdges().add(vectorPath.get(i));
		}

		for (int i = second + 1; i < vectorPath.size(); i--) {
			newPath.getEdges().add(vectorPath.get(i));
		}

		return newPath;
	}

}
