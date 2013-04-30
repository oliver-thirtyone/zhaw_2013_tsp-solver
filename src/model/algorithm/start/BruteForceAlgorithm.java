package model.algorithm.start;

import java.util.Set;

import model.grid.Edge;
import model.grid.Node;
import model.grid.Path;

public class BruteForceAlgorithm extends AStartAlgorithm {

	private final int nodeCount;
	private final Node[] nodes;
	private final int[] nodeIndexes;

	private final Path currentLightestPath;
	private final Path currentNewPath;

	private int outerLoopIndex;
	private int innerLoopIndex;

	public BruteForceAlgorithm(Set<Node> nodes, Node startingNode) {
		super(nodes, startingNode);

		this.nodeCount = this.getNodes().size();
		this.nodes = this.getNodes().toArray(new Node[this.nodeCount]);
		this.nodeIndexes = new int[this.nodeCount];

		this.currentLightestPath = new Path();
		this.currentNewPath = new Path();

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
		this.currentNewPath.clearEdges();
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

			this.currentNewPath.addEdge(edge);
		}

		// Add the lightest and the new path to the current path (for the GUI)
		this.getCurrentPath().clearEdges();
		this.getCurrentPath().addPath(this.currentLightestPath);
		this.getCurrentPath().addPath(this.currentNewPath);

		// Check if the new path is lighter
		if (this.currentLightestPath.getWeight() == 0.0 || this.currentNewPath.getWeight() < this.currentLightestPath.getWeight()) {
			this.currentLightestPath.clearEdges();
			this.currentLightestPath.addPath(this.currentNewPath);
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
			this.getCurrentPath().clearEdges();
			this.getCurrentPath().addPath(this.currentLightestPath);
			this.setFinishedSuccessful(true);
		}

		return successfulStep;
	}

	@Override
	protected void reset() {
		super.reset();

		this.currentLightestPath.clearEdges();
		this.currentNewPath.clearEdges();

		this.outerLoopIndex = 0;
		this.innerLoopIndex = 0;

		// Initialize the node indexes
		for (int i = 0; i < this.nodeCount; i++) {
			this.nodeIndexes[i] = i;
		}
	}

}
