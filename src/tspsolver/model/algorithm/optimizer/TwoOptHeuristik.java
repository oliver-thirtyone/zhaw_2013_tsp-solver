package tspsolver.model.algorithm.optimizer;

import tspsolver.model.Scenario;
import tspsolver.model.grid.Edge;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;

public class TwoOptHeuristik extends AOptimizerAlgorithm {

	private int i;
	private int k;

	public TwoOptHeuristik(Scenario scenario) {
		super(scenario);

		this.i = 0;
		this.k = 1;
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

			if (newPath.getWeight() < this.getPathUpdater().getPath().getWeight()) {
				// A shorter path was found.
				this.getPathUpdater().clearPath();
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

		Edge[] edges = path.getEdges();

		Path newPath = new Path();
		PathUpdater newPathUpdater = new PathUpdater(newPath);

		for (int i = 0; i < first; i++) {
			newPathUpdater.addEdge(edges[i]);
		}

		for (int i = second; i >= first; i--) {
			newPathUpdater.addEdge(edges[i]);
		}

		for (int i = second + 1; i < path.getNumberOfEdges(); i++) {
			newPathUpdater.addEdge(edges[i]);
		}

		newPathUpdater.updatePath();
		return newPath;
	}
}
