package tspsolver.model.algorithm.start;

import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public class BruteForceAlgorithm extends StartAlgorithm {

	private final Path lightestPath;
	private final PathUpdater lightestPathUpdater;

	private int nodeCount;
	private Node[] nodes;
	private int[] nodeIndexes;

	private int outerLoopIndex;
	private int innerLoopIndex;

	public BruteForceAlgorithm() {
		this.lightestPath = new Path();
		this.lightestPathUpdater = new PathUpdater(this.lightestPath);

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
		this.lightestPathUpdater.clearPath();
		this.lightestPathUpdater.updatePath();

		this.nodeCount = 0;
		this.nodes = null;
		this.nodeIndexes = null;

		this.outerLoopIndex = 0;
		this.innerLoopIndex = 0;
	}

	@Override
	protected boolean doStep() {
		boolean successfullStep = true;
		boolean validNewPath = true;

		// Swap the node indexes
		final int temp = this.nodeIndexes[this.outerLoopIndex];
		this.nodeIndexes[this.outerLoopIndex] = this.nodeIndexes[this.innerLoopIndex];
		this.nodeIndexes[this.innerLoopIndex] = temp;

		// Create the new path
		Path newPath = new Path();
		PathUpdater newPathUpdater = new PathUpdater(newPath);

		newPathUpdater.clearPath();
		for (int i = 1; i <= this.nodeCount; i++) {
			final int firstIndex = this.nodeIndexes[i - 1];
			final int secondIndex = (i == this.nodeCount) ? this.nodeIndexes[0] : this.nodeIndexes[i];

			final Node firstNode = this.nodes[firstIndex];
			final Node secondNode = this.nodes[secondIndex];

			if (firstNode.hasEdgeToNode(secondNode)) {
				Edge edge = firstNode.getEdgeToNode(secondNode);
				newPathUpdater.addEdge(edge);
			}
			else {
				validNewPath = false;
				break;
			}
		}
		newPathUpdater.updatePath();

		// Check if the new path is lighter
		if (validNewPath && (this.lightestPath.isEmpty() || newPath.getWeight() < this.lightestPath.getWeight())) {
			// Set the new lightest path
			this.lightestPathUpdater.clearPath();
			this.lightestPathUpdater.addPath(newPath);
			this.lightestPathUpdater.updatePath();

			this.getPathUpdater().clearPath();
			this.getPathUpdater().addPath(this.lightestPath);
		}

		// Increase the inner loop index
		this.innerLoopIndex++;

		// Increase the outer loop index if we are done with the inner loop
		if (this.innerLoopIndex >= this.nodeCount) {
			this.outerLoopIndex++;
			this.innerLoopIndex = 0;
		}

		// Finish the algorithm if we are done with the outer loop
		if (this.outerLoopIndex >= this.nodeCount) {
			// If we have found a path, we were successful
			if (!this.getPath().isEmpty()) {
				this.finishedSuccessfully();
			}
			else {
				successfullStep = false;
			}
		}

		this.getPathUpdater().updatePath();
		return successfullStep;
	}
}
