package tspsolver.model.algorithm;

import tspsolver.model.Scenario;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;

public abstract class Algorithm {

	private final Grid grid;
	private final Node startingNode;

	private final Path path;
	private final PathUpdater pathUpdater;

	private boolean validArguments;
	private boolean finishedSuccessfully;

	public Algorithm(Scenario scenario) {
		this.grid = scenario.getGrid();
		this.startingNode = scenario.getStartingNode();

		this.path = scenario.getPath();
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

	public void reset() {
		this.getPathUpdater().clearPath();

		this.setValidArguments(false);
		this.finishedSuccessfully = false;
	}

	protected abstract boolean doStep();

	protected Grid getGrid() {
		return this.grid;
	}

	protected Node getStartingNode() {
		return startingNode;
	}

	protected Path getPath() {
		return path;
	}

	protected PathUpdater getPathUpdater() {
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

	protected void finishedSuccessfully() {
		this.pathUpdater.updatePath();
		this.finishedSuccessfully = true;
	}

}
