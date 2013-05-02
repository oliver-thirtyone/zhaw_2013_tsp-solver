package tspsolver.model.algorithm;

import tspsolver.model.grid.Path;

public abstract class Algorithm {

	private final Path path;

	private boolean validArguments;
	private boolean finishedSuccessful;

	public Algorithm(Path path) {
		this.path = path;

		this.validArguments = false;
		this.finishedSuccessful = false;
	}

	public abstract void validateArguments();

	public final boolean step() {
		if (!this.hasValidArguments() || this.hasFinishedSuccessful()) {
			return false;
		}

		return this.doStep();
	}

	protected abstract boolean doStep();

	public Path getPath() {
		return this.path;
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
		this.getPath().clearEdges();

		this.setValidArguments(false);
		this.setFinishedSuccessful(false);
	}

}
