package tspsolver.util.runner;

import java.util.Observable;
import java.util.Observer;

public abstract class MainRunner extends Observable implements Observer {

	private final Runner[] runners;

	public MainRunner(Runner[] runners) {
		this.runners = runners;

		for (Runner runner : runners) {
			runner.addObserver(this);
		}
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers();
	}

	public boolean canInitialize() {
		boolean canInitialize = true;
		for (Runner runner : runners) {
			if (!runner.canInitialize()) {
				canInitialize = false;
				break;
			}
		}
		return canInitialize;
	}

	public boolean initialize() {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.initialize()) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public boolean initialize(int stepDelay) {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.initialize(stepDelay)) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public synchronized boolean canReset() {
		boolean canReset = true;
		for (Runner runner : runners) {
			if (!runner.canReset()) {
				canReset = false;
				break;
			}
		}
		return canReset;
	}

	public synchronized boolean reset() {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.reset()) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public synchronized boolean canStart() {
		boolean canStart = true;
		for (Runner runner : runners) {
			if (!runner.canStart()) {
				canStart = false;
				break;
			}
		}
		return canStart;
	}

	public synchronized boolean start() {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.start()) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public synchronized boolean canStep() {
		boolean canStep = true;
		for (Runner runner : runners) {
			if (!runner.canStep()) {
				canStep = false;
				break;
			}
		}
		return canStep;
	}

	public synchronized boolean step() {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.step()) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public synchronized boolean canPause() {
		boolean canPause = true;
		for (Runner runner : runners) {
			if (!runner.canPause()) {
				canPause = false;
				break;
			}
		}
		return canPause;
	}

	public synchronized boolean pause() {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.pause()) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public synchronized boolean canStop() {
		boolean canStop = true;
		for (Runner runner : runners) {
			if (!runner.canStop()) {
				canStop = false;
				break;
			}
		}
		return canStop;
	}

	public synchronized boolean stop() {
		boolean successful = true;
		for (Runner runner : runners) {
			if (!runner.stop()) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public synchronized Runner[] getRunners() {
		return this.runners;
	}

	public synchronized int getStepDelay() {
		int stepDelay = Runner.DEFAULT_STEP_DELAY;

		if (this.runners[0] != null) {
			stepDelay = this.runners[0].getStepDelay();
		}
		return stepDelay;
	}

}
