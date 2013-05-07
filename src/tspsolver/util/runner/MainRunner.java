package tspsolver.util.runner;

import java.util.Observable;
import java.util.Observer;

public abstract class MainRunner extends Observable implements Observer {

	private final Runner[] runners;

	public MainRunner(Runner[] runners) {
		this.runners = runners;

		for (final Runner runner : runners) {
			runner.addObserver(this);
		}
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers();
	}

	public boolean canInitialize() {
		boolean canInitialize = false;
		for (final Runner runner : this.runners) {
			if (runner.canInitialize()) {
				canInitialize = true;
				break;
			}
		}
		return canInitialize;
	}

	public boolean initialize() {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.initialize()) {
				successful = true;
			}
		}
		return successful;
	}

	public boolean initialize(int stepDelay) {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.initialize(stepDelay)) {
				successful = true;
			}
		}
		return successful;
	}

	public synchronized boolean canStart() {
		boolean canStart = false;
		for (final Runner runner : this.runners) {
			if (runner.canStart()) {
				canStart = true;
				break;
			}
		}
		return canStart;
	}

	public synchronized boolean start() {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.start()) {
				successful = true;
			}
		}
		return successful;
	}

	public synchronized boolean canStep() {
		boolean canStep = false;
		for (final Runner runner : this.runners) {
			if (runner.canStep()) {
				canStep = true;
				break;
			}
		}
		return canStep;
	}

	public synchronized boolean step() {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.step()) {
				successful = true;
			}
		}
		return successful;
	}

	public synchronized boolean canPause() {
		boolean canPause = false;
		for (final Runner runner : this.runners) {
			if (runner.canPause()) {
				canPause = true;
				break;
			}
		}
		return canPause;
	}

	public synchronized boolean pause() {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.pause()) {
				successful = true;
			}
		}
		return successful;
	}

	public synchronized boolean canStop() {
		boolean canStop = false;
		for (final Runner runner : this.runners) {
			if (runner.canStop()) {
				canStop = true;
				break;
			}
		}
		return canStop;
	}

	public synchronized boolean stop() {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.stop()) {
				successful = true;
			}
		}
		return successful;
	}

	public synchronized boolean canReset() {
		boolean canReset = false;
		for (final Runner runner : this.runners) {
			if (runner.canReset()) {
				canReset = true;
				break;
			}
		}
		return canReset;
	}

	public synchronized boolean reset() {
		boolean successful = false;
		for (final Runner runner : this.runners) {
			if (runner.reset()) {
				successful = true;
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
