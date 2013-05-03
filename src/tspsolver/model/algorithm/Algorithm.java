package tspsolver.model.algorithm;

import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;

public abstract class Algorithm {

	private final PathUpdater pathUpdater;

	private boolean validArguments;
	private boolean finishedSuccessful;

	public Algorithm(Path path) {
		this.pathUpdater = new PathUpdater(path);

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

	public PathUpdater getPathUpdater() {
		return this.pathUpdater;
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
		this.getPathUpdater().clearWholePath();

		this.setValidArguments(false);
		this.setFinishedSuccessful(false);
	}

}
