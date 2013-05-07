package tspsolver.model.algorithm.start;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class BruteForceAlgorithm extends StartAlgorithm {

	private final Path currentLightestPath;
	private final PathUpdater currentLightestPathUpdater;

	private final Path currentNewPath;
	private final PathUpdater currentNewPathUpdater;

	private int nodeCount;
	private Node[] nodes;
	private int[] nodeIndexes;

	private int outerLoopIndex;
	private int innerLoopIndex;

	public BruteForceAlgorithm() {
		this.currentLightestPath = new Path();
		this.currentLightestPathUpdater = new PathUpdater(this.currentLightestPath);

		this.currentNewPath = new Path();
		this.currentNewPathUpdater = new PathUpdater(this.currentNewPath);

		this.reset();
	}

	@Override
	protected void doInitialize() {

		this.nodeCount = this.getGrid().getNumberOfNodes();
		this.nodes = this.getGrid().getNodes();
		this.nodeIndexes = new int[this.nodeCount];

		// Initialize the node indexes
		for (int i = 0; i < this.nodeCount; i++) {
			this.nodeIndexes[i] = i;
		}

		this.outerLoopIndex = 0;
		this.innerLoopIndex = 0;
	}

	@Override
	protected void doReset() {
		this.currentLightestPathUpdater.clearPath();
		this.currentLightestPathUpdater.updatePath();

		this.currentNewPathUpdater.clearPath();
		this.currentNewPathUpdater.updatePath();

		this.nodeCount = 0;
		this.nodes = null;
		this.nodeIndexes = null;

		this.outerLoopIndex = 0;
		this.innerLoopIndex = 0;
	}

	@Override
	protected boolean doStep() {
		final boolean successfulStep = true;

		// Swap the node indexes
		final int temp = this.nodeIndexes[this.outerLoopIndex];
		this.nodeIndexes[this.outerLoopIndex] = this.nodeIndexes[this.innerLoopIndex];
		this.nodeIndexes[this.innerLoopIndex] = temp;

		// Create the new path
		this.currentNewPathUpdater.clearPath();
		for (int i = 1; i <= this.nodeCount; i++) {
			final int firstIndex = this.nodeIndexes[i - 1];
			final int secondIndex = (i == this.nodeCount) ? this.nodeIndexes[0] : this.nodeIndexes[i];

			final Node firstNode = this.nodes[firstIndex];
			final Node secondNode = this.nodes[secondIndex];

			final Edge edge = firstNode.getEdgeToNode(secondNode);
			if (edge == null) {
				// FIXME: this path does not work, what do we do now?
				return false;
			}

			this.currentNewPathUpdater.addEdge(edge);
		}
		this.currentNewPathUpdater.updatePath();

		// Add the lightest and the new path to the current path (for the GUI)
		this.getPathUpdater().clearPath();
		this.getPathUpdater().addPath(this.currentLightestPath);
		this.getPathUpdater().addPath(this.currentNewPath);

		// Check if the new path is lighter
		if (this.currentLightestPath.isEmpty() || this.currentNewPath.getWeight() < this.currentLightestPath.getWeight()) {
			this.currentLightestPathUpdater.clearPath();
			this.currentLightestPathUpdater.addPath(this.currentNewPath);
			this.currentLightestPathUpdater.updatePath();
		}

		// Increase the inner loop index
		this.innerLoopIndex++;

		// Increase the outer loop index if we are done with the inner loop
		if (this.innerLoopIndex >= this.nodeCount) {
			this.outerLoopIndex++;
			this.innerLoopIndex = this.outerLoopIndex + 1; // Skip the combinations that we've already tried

			// Prevent an "index out of bounds"-exception
			if (this.innerLoopIndex >= this.nodeCount) {
				this.innerLoopIndex = this.nodeCount - 1;
			}
		}

		// Finish the algorithm if we are done with the outer loop
		if (this.outerLoopIndex >= this.nodeCount) {
			this.getPathUpdater().clearPath();
			this.getPathUpdater().addPath(this.currentLightestPath);
			this.finishedSuccessfully();
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

}
