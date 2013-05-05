package tspsolver.controller.runner;

import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.algorithm.optimizer.AOptimizerAlgorithm;
import tspsolver.model.algorithm.start.AStartAlgorithm;

public class AlgorithmRunner extends ARunner implements Runnable {

	private AStartAlgorithm startAlgorithm;
	private AOptimizerAlgorithm optimizerAlgorithm;

	private Algorithm runningAlgorithm;

	public AlgorithmRunner() {
		this.startAlgorithm = null;
		this.optimizerAlgorithm = null;

		this.runningAlgorithm = null;
	}

	@Override
	protected synchronized boolean doStep() {
		// Take the next step in the algorithm
		boolean successfulStep = this.runningAlgorithm.step();

		// Stop if there was an error
		if (!successfulStep) {
			this.setState(RunnerState.STOPPED);
		} else if (this.runningAlgorithm.hasFinishedSuccessfully()) {
			// Switch to the optimizer algorithm if the start algorithm finished successfully
			if (this.runningAlgorithm == this.startAlgorithm && this.optimizerAlgorithm != null) {
				this.setRunningAlgorithm(optimizerAlgorithm);
				return this.validateRunningAlgorithm();
			} else {
				this.setState(RunnerState.STOPPED);
			}
		}

		// Switch to pause if we run in STEP-Mode
		if (this.getState() == RunnerState.STEPPING) {
			this.setState(RunnerState.PAUSED);
		}

		return true;
	}

	@Override
	protected synchronized boolean doReset() {
		this.setStartAlgorithm(null);
		this.setOptimizerAlgorithm(null);
		this.setRunningAlgorithm(null);

		return true;
	}

	@Override
	protected synchronized boolean doInitialize() {
		if (this.startAlgorithm == null) {
			return false;
		}

		this.setRunningAlgorithm(startAlgorithm);
		return this.validateRunningAlgorithm();
	}

	public Algorithm getStartAlgorithm() {
		return this.startAlgorithm;
	}

	public boolean setStartAlgorithm(AStartAlgorithm startAlgorithm) {
		if (!this.canInitialize()) {
			return false;
		}

		this.startAlgorithm = startAlgorithm;
		return true;
	}

	public Algorithm getOptimizerAlgorithm() {
		return this.optimizerAlgorithm;
	}

	public boolean setOptimizerAlgorithm(AOptimizerAlgorithm optimizerAlgorithm) {
		if (!this.canInitialize()) {
			return false;
		}

		this.optimizerAlgorithm = optimizerAlgorithm;
		return true;
	}

	private void setRunningAlgorithm(Algorithm algorithm) {
		this.runningAlgorithm = algorithm;

		this.setChanged();
		this.notifyObservers();
	}

	private boolean validateRunningAlgorithm() {
		this.runningAlgorithm.validateArguments();
		return this.runningAlgorithm.hasValidArguments();
	}

}
