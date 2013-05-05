package tspsolver.model.algorithm;

import tspsolver.model.Scenario;
import tspsolver.model.path.PathUpdater;

public abstract class Algorithm {

	private final PathUpdater pathUpdater;

	private boolean validArguments;
	private boolean finishedSuccessfully;

	public Algorithm(Scenario scenario) {
		this.pathUpdater = new PathUpdater(scenario.getPath());

		this.validArguments = false;
		this.finishedSuccessfully = false;
	}

	public abstract void validateArguments();

	public final boolean step() {
		if (!this.hasValidArguments() || this.hasFinishedSuccessfully()) {
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

	public boolean hasFinishedSuccessfully() {
		return this.finishedSuccessfully;
	}

	protected void setFinishedSuccessful(boolean finishedSuccessfully) {
		this.finishedSuccessfully = finishedSuccessfully;
	}

	public void reset() {
		this.getPathUpdater().clearPath();

		this.setValidArguments(false);
		this.setFinishedSuccessful(false);
	}

}
