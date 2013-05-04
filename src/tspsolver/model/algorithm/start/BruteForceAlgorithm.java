package tspsolver.model.algorithm.start;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;

public class BruteForceAlgorithm extends AStartAlgorithm {

	private final int nodeCount;
	private final Node[] nodes;
	private final int[] nodeIndexes;

	private final Path currentLightestPath;
	private final PathUpdater currentLightestPathUpdater;

	private final Path currentNewPath;
	private final PathUpdater currentNewPathUpdater;

	private int outerLoopIndex;
	private int innerLoopIndex;

	public BruteForceAlgorithm(Path path, Grid grid) {
		super(path, grid);

		this.nodeCount = this.getGrid().getNodes().size();
		this.nodes = this.getGrid().getNodes().toArray(new Node[this.nodeCount]);
		this.nodeIndexes = new int[this.nodeCount];

		this.currentLightestPath = new Path();
		this.currentLightestPathUpdater = new PathUpdater(this.currentLightestPath);

		this.currentNewPath = new Path();
		this.currentNewPathUpdater = new PathUpdater(this.currentNewPath);

		this.reset();
	}

	@Override
	protected boolean doStep() {
		boolean successfulStep = true;

		// Swap the node indexes
		int temp = this.nodeIndexes[this.outerLoopIndex];
		this.nodeIndexes[this.outerLoopIndex] = this.nodeIndexes[this.innerLoopIndex];
		this.nodeIndexes[this.innerLoopIndex] = temp;

		// Create the new path
		this.currentNewPathUpdater.clearWholePath();
		for (int i = 1; i <= this.nodeCount; i++) {
			int firstIndex = this.nodeIndexes[i - 1];
			int secondIndex = (i == this.nodeCount) ? this.nodeIndexes[0] : this.nodeIndexes[i];

			Node firstNode = this.nodes[firstIndex];
			Node secondNode = this.nodes[secondIndex];

			Edge edge = firstNode.getEdgeToNode(secondNode);
			if (edge == null) {
				// FIXME: this path does not work, what do we do now?
				return false;
			}

			this.currentNewPathUpdater.addEdge(edge);
		}
		this.currentNewPathUpdater.updatePath();

		// Add the lightest and the new path to the current path (for the GUI)
		this.getPathUpdater().clearWholePath();
		this.getPathUpdater().addPath(this.currentLightestPath);
		this.getPathUpdater().addPath(this.currentNewPath);

		// Check if the new path is lighter
		if (this.currentLightestPath.getWeight() == 0.0 || this.currentNewPath.getWeight() < this.currentLightestPath.getWeight()) {
			this.currentLightestPathUpdater.clearWholePath();
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
			this.getPathUpdater().clearWholePath();
			this.getPathUpdater().addPath(this.currentLightestPath);
			this.setFinishedSuccessful(true);
		}

		this.getPathUpdater().updatePath();
		return successfulStep;
	}

	@Override
	public void reset() {
		super.reset();

		this.currentLightestPathUpdater.clearWholePath();
		this.currentLightestPathUpdater.updatePath();

		this.currentNewPathUpdater.clearWholePath();
		this.currentNewPathUpdater.clearWholePath();

		this.outerLoopIndex = 0;
		this.innerLoopIndex = 0;

		// Initialize the node indexes
		for (int i = 0; i < this.nodeCount; i++) {
			this.nodeIndexes[i] = i;
		}
	}

}
