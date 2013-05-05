package tspsolver.controller.runner;

import tspsolver.model.Scenario;

public class MainRunner extends ARunner {

	private final Scenario[] scenarios;
	private final AlgorithmRunner[] algorithmRunners;

	private Scenario selectedScenario;

	public MainRunner(Scenario[] scenarios, AlgorithmRunner[] algorithmRunners) {
		this.scenarios = scenarios;
		this.algorithmRunners = algorithmRunners;

		this.selectedScenario = null;
	}

	@Override
	protected boolean doStep() {
		boolean success = true;

		for (ARunner runner : this.getAlgorithmRunners()) {
			if (runner.canStep()) {
				boolean step = runner.step();
				success = success ? step : false;
			}
		}

		return success;
	}

	@Override
	protected boolean doReset() {
		boolean success = true;

		for (ARunner runner : this.getAlgorithmRunners()) {
			boolean reset = runner.reset();
			success = success ? reset : false;
		}

		return success;
	}

	@Override
	protected boolean doInitialize() {
		boolean success = true;

		// TODO: set the selected scenario for the algorithms

		for (ARunner runner : this.getAlgorithmRunners()) {
			if (runner.canStep()) {
				boolean initialize = runner.initialize(0);
				success = success ? initialize : false;
			}
		}

		return success;
	}

	public Scenario[] getScenarios() {
		return this.scenarios;
	}

	public AlgorithmRunner[] getAlgorithmRunners() {
		return this.algorithmRunners;
	}

	public Scenario getSelectedScenario() {
		return this.selectedScenario;
	}

	public boolean setSelectedScenario(Scenario scenario) {
		if (!this.canInitialize()) {
			return false;
		}

		this.selectedScenario = scenario;
		return true;
	}

}
