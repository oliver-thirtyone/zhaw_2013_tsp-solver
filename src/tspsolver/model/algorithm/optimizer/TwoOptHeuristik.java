package tspsolver.model.algorithm.optimizer;

import java.util.List;
import java.util.Vector;

import tspsolver.model.Scenario;
import tspsolver.model.grid.Edge;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;

public class TwoOptHeuristik extends AOptimizerAlgorithm {

	private final List<Edge> orderlyPath;

	private int i;
	private int k;

	public TwoOptHeuristik(Scenario scenario) {
		super(scenario);

		this.orderlyPath = new Vector<Edge>();
		this.reset();
	}

	@Override
	protected boolean doStep() {

		// Initialize
		if (i == 0 && k == 1 && orderlyPath.isEmpty()) {
			for (Edge edge : this.getPath().getEdges()) {
				this.orderlyPath.add(edge);
			}
		}

		if (i < this.getPath().getNumberOfEdges()) {
			// Create the new path
			List<Edge> newOrderlyPath = this.createNewOrderlyPath();
			Path newPath = this.createNewPath(newOrderlyPath);

			// Check if the new path is lighter
			if (newPath.getWeight() < this.getPath().getWeight()) {
				this.getPathUpdater().clearPath();
				this.getPathUpdater().addPath(newPath);
				this.getPathUpdater().updatePath();

				this.orderlyPath.clear();
				this.orderlyPath.addAll(newOrderlyPath);
			}

			if (this.k < this.getPath().getNumberOfEdges() - 1) {
				this.k++;
			} else {
				this.i++;
				this.k = this.i + 1;
			}
		} else {
			this.finishedSuccessfully();
		}

		return true;
	}

	// 2optSwap(route, i, k) {
	// 1. take route[0] to route[i-1] and add them in order to new_route
	// 2. take route[i] to route[k] and add them in reverse order to new_route
	// 3. take route[k+1] to end and add them in order to new_route
	// return new_route;
	// }
	private List<Edge> createNewOrderlyPath() {
		List<Edge> newOrderlyPath = new Vector<Edge>();

		for (int x = 0; x < this.i; x++) {
			newOrderlyPath.add(this.orderlyPath.get(x));
		}

		for (int x = this.k; x >= this.i; x--) {
			newOrderlyPath.add(this.orderlyPath.get(x));
		}

		for (int x = this.k + 1; x < this.getPath().getNumberOfEdges(); x++) {
			newOrderlyPath.add(this.orderlyPath.get(x));
		}

		return newOrderlyPath;
	}

	private Path createNewPath(List<Edge> edges) {
		Path newPath = new Path();
		PathUpdater newPathUpdater = new PathUpdater(newPath);

		newPathUpdater.addEdges(edges);
		newPathUpdater.updatePath();

		return newPath;
	}

	@Override
	public void reset() {
		super.reset();

		this.orderlyPath.clear();
		this.i = 0;
		this.k = 1;
	}
}
