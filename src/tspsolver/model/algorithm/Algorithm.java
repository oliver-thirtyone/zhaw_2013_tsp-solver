package tspsolver.model.algorithm;

import tspsolver.model.grid.Node;

public abstract class Algorithm {

	private final Path currentPath;
	private Node currentNode;

	private boolean validArguments;
	private boolean finishedSuccessful;

	public Algorithm() {
		this.currentPath = new Path();
		this.currentNode = null;

		this.validArguments = false;
		this.finishedSuccessful = false;
	}

	public final boolean step() {
		if (!this.hasValidArguments() || this.hasFinishedSuccessful()) {
			return false;
		}

		return this.doStep();
	}

	protected abstract boolean doStep();

	public abstract void validateArguments();

	public Path getCurrentPath() {
		return this.currentPath;
	}

	public Node getCurrentNode() {
		return this.currentNode;
	}

	protected void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public boolean hasValidArguments() {
		return this.validArguments;
	}

	protected void setValidArguments(boolean validArguments) {
		this.validArguments = validArguments;
	}

	public boolean hasFinishedSuccessful() {
		return this.finishedSuccessful;
	}

	protected void setFinishedSuccessful(boolean finishedSuccessful) {
		this.finishedSuccessful = finishedSuccessful;
	}

	protected void reset() {
		this.getCurrentPath().clearEdges();
		this.setCurrentNode(null);

		this.setValidArguments(false);
		this.setFinishedSuccessful(false);
	}

}
