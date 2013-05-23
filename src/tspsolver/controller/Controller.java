package tspsolver.controller;

import tspsolver.model.scenario.Scenario;
import tspsolver.util.runner.MainRunner;

public class Controller extends MainRunner {

	private final Scenario[] scenarios;
	private final AlgorithmRunner[] algorithmRunners;

	private Scenario selectedScenario;

	public Controller(Scenario[] scenarios, AlgorithmRunner[] algorithmRunners) {
		super(algorithmRunners);

		this.scenarios = scenarios;
		this.algorithmRunners = algorithmRunners;

		this.setSelectedScenario(scenarios[0]);
	}

	@Override
	public synchronized boolean initialize() {
		// Check if a scenario is selected
		if (this.getSelectedScenario() == null) {
			return false;
		}

		// Initialize all runners
		return super.initialize();
	}

	@Override
	public synchronized boolean reset() {
		// Select the first scenario by default
		this.setSelectedScenario(this.scenarios[0]);

		// Reset all runners
		return super.reset();
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
		boolean success = true;

		if (!this.canInitialize()) {
			return false;
		}

		// Set the scenario for all algorithm runners
		for (AlgorithmRunner runner : this.getAlgorithmRunners()) {
			Scenario copyOfScenario = scenario.copy();

			boolean setSelectedScenario = runner.setSelectedScenario(copyOfScenario);
			success = success ? setSelectedScenario : false;
		}

		// Set the scenario for the main runner
		if (success) {
			this.selectedScenario = scenario;
		}

		return success;
	}

}
