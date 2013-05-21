package tspsolver.model.algorithm;

import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.scenario.path.Path;
import tspsolver.model.scenario.path.PathUpdater;

public abstract class Algorithm {

	private Scenario scenario;
	private PathUpdater pathUpdater;

	private boolean validArguments;
	private boolean finishedSuccessfully;

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public synchronized void initialize(Scenario scenario) {
		// Reset everything before we initialize
		this.reset();

		this.scenario = scenario;
		this.pathUpdater = new PathUpdater(this.getPath());

		this.doInitialize();
		this.validateArguments();
	}

	public synchronized void reset() {
		if (this.getPathUpdater() != null) {
			this.getPathUpdater().clearPath();
			this.getPathUpdater().updatePath();
		}

		this.scenario = null;
		this.pathUpdater = null;

		this.setValidArguments(false);
		this.finishedSuccessfully = false;

		this.doReset();
	}

	public synchronized boolean step() {
		if (!this.hasValidArguments() || this.hasFinishedSuccessfully()) {
			return false;
		}

		return this.doStep();
	}

	protected abstract void doInitialize();

	protected abstract void doReset();

	protected abstract boolean doStep();

	protected abstract void validateArguments();

	public boolean hasValidArguments() {
		return this.validArguments;
	}

	protected void setValidArguments(boolean validArguments) {
		this.validArguments = validArguments;
	}

	public boolean hasFinishedSuccessfully() {
		return this.finishedSuccessfully;
	}

	protected void finishedSuccessfully() {
		this.finishedSuccessfully = true;
		this.getPathUpdater().updatePath();
	}

	public Scenario getScenario() {
		return this.scenario;
	}

	public Grid getGrid() {
		return this.scenario.getGrid();
	}

	public Node getStartingNode() {
		return this.scenario.getStartingNode();
	}

	public Path getPath() {
		return this.scenario.getPath();
	}

	protected PathUpdater getPathUpdater() {
		return this.pathUpdater;
	}
}
