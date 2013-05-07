package tspsolver.controller;

import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.scenario.Scenario;
import tspsolver.util.runner.Runner;
import tspsolver.util.runner.RunnerState;

public class AlgorithmRunner extends Runner implements Runnable {

	private final StartAlgorithm[] startAlgorithms;
	private final OptimizerAlgorithm[] optimizerAlgorithms;

	private Scenario selectedScenario;
	private StartAlgorithm selectedstartAlgorithm;
	private OptimizerAlgorithm selectedOptimizerAlgorithm;

	private Algorithm runningAlgorithm;

	public AlgorithmRunner(StartAlgorithm[] startAlgorithms, OptimizerAlgorithm[] optimizerAlgorithms) {
		this.startAlgorithms = startAlgorithms;
		this.optimizerAlgorithms = optimizerAlgorithms;

		this.setSelectedScenario(null);
		this.setSelectedStartAlgorithm(startAlgorithms[0]);
		this.setSelectedOptimizerAlgorithm(null);

		this.setRunningAlgorithm(null);
	}

	@Override
	protected synchronized boolean doInitialize() {
		// Check if a scenario and a start algorithm is selected
		if (this.getSelectedScenario() == null || this.getSelectedStartAlgorithm() == null) {
			return false;
		}

		this.getSelectedStartAlgorithm().initialize(this.getSelectedScenario());
		this.setRunningAlgorithm(this.selectedstartAlgorithm);

		return true;
	}

	@Override
	protected synchronized boolean doReset() {
		if (this.getSelectedStartAlgorithm() != null) {
			this.getSelectedStartAlgorithm().reset();
		}
		if (this.getSelectedOptimizerAlgorithm() != null) {
			this.getSelectedOptimizerAlgorithm().reset();
		}

		this.setSelectedScenario(null);
		this.setSelectedStartAlgorithm(this.startAlgorithms[0]);
		this.setSelectedOptimizerAlgorithm(null);

		this.setRunningAlgorithm(null);
		return true;
	}

	@Override
	protected synchronized boolean doStep() {
		// Take the next step in the algorithm
		final boolean successfulStep = this.getRunningAlgorithm().step();

		// Stop if there was an error
		if (!successfulStep) {
			this.setState(RunnerState.STOPPED);
		}

		// Check if the running algorithm finished successfully
		else if (this.getRunningAlgorithm().hasFinishedSuccessfully()) {
			// Switch to the optimizer-algorithm if the start-algorithm finished successfully
			if (this.getRunningAlgorithm() == this.getSelectedStartAlgorithm() && this.getSelectedOptimizerAlgorithm() != null) {
				this.getSelectedOptimizerAlgorithm().initialize(this.getSelectedScenario());
				this.setRunningAlgorithm(this.getSelectedOptimizerAlgorithm());
			}
			// Stop if no optimizer-algorithm has been selected or the optimizer-algorithm finished successfully
			else {
				this.setState(RunnerState.STOPPED);
			}
		}

		return successfulStep;
	}

	public StartAlgorithm[] getStartAlgorithms() {
		return this.startAlgorithms;
	}

	public OptimizerAlgorithm[] getOptimizerAlgorithms() {
		return this.optimizerAlgorithms;
	}

	public Scenario getSelectedScenario() {
		return this.selectedScenario;
	}

	public boolean setSelectedScenario(Scenario scenario) {
		if (!this.canInitialize()) {
			return false;
		}
		this.selectedScenario = scenario;

		this.setChanged();
		this.notifyObservers(scenario);
		return true;
	}

	public Algorithm getSelectedStartAlgorithm() {
		return this.selectedstartAlgorithm;
	}

	public boolean setSelectedStartAlgorithm(StartAlgorithm startAlgorithm) {
		if (!this.canInitialize()) {
			return false;
		}
		this.selectedstartAlgorithm = startAlgorithm;

		this.setChanged();
		this.notifyObservers(startAlgorithm);
		return true;
	}

	public Algorithm getSelectedOptimizerAlgorithm() {
		return this.selectedOptimizerAlgorithm;
	}

	public boolean setSelectedOptimizerAlgorithm(OptimizerAlgorithm optimizerAlgorithm) {
		if (!this.canInitialize()) {
			return false;
		}
		this.selectedOptimizerAlgorithm = optimizerAlgorithm;

		this.setChanged();
		this.notifyObservers(optimizerAlgorithm);
		return true;
	}

	public Algorithm getRunningAlgorithm() {
		return this.runningAlgorithm;
	}

	private void setRunningAlgorithm(Algorithm algorithm) {
		this.runningAlgorithm = algorithm;

		this.setChanged();
		this.notifyObservers(algorithm);
	}

}
