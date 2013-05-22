package tspsolver.util.runner;

import java.util.Observable;
import java.util.Observer;

public abstract class MainRunner extends Observable implements Observer {

	private final Runner[] runners;

	private int stepDelay;

	public MainRunner(Runner[] runners) {
		this.runners = runners;

		for (Runner runner : runners) {
			runner.addObserver(this);
		}

		this.stepDelay = Runner.DEFAULT_STEP_DELAY;
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers();
	}

	public boolean canInitialize() {
		boolean canInitialize = true;
		for (Runner runner : this.runners) {
			if (!runner.canInitialize()) {
				canInitialize = false;
				break;
			}
		}
		return canInitialize;
	}

	public boolean initialize() {
		boolean successful = true;
		for (Runner runner : this.runners) {
			if (!runner.initialize()) {
				successful = false;
			}
		}
		return successful;
	}

	public boolean initialize(int stepDelay) {
		this.stepDelay = stepDelay;

		boolean successful = true;
		for (Runner runner : this.runners) {
			if (!runner.initialize(this.stepDelay)) {
				successful = false;
			}
		}
		return successful;
	}

	public boolean canStart() {
		boolean canStart = false;
		for (Runner runner : this.runners) {
			if (runner.canStart()) {
				canStart = true;
				break;
			}
		}
		return canStart;
	}

	public boolean start() {
		boolean successful = false;
		for (Runner runner : this.runners) {
			if (runner.start()) {
				successful = true;
			}
		}
		return successful;
	}

	public boolean canStep() {
		boolean canStep = false;
		for (Runner runner : this.runners) {
			if (runner.canStep()) {
				canStep = true;
				break;
			}
		}
		return canStep;
	}

	public boolean step() {
		boolean successful = false;
		for (Runner runner : this.runners) {
			if (runner.step()) {
				successful = true;
			}
		}
		return successful;
	}

	public boolean canPause() {
		boolean canPause = false;
		for (Runner runner : this.runners) {
			if (runner.canPause()) {
				canPause = true;
				break;
			}
		}
		return canPause;
	}

	public boolean pause() {
		boolean successful = false;
		for (Runner runner : this.runners) {
			if (runner.pause()) {
				successful = true;
			}
		}
		return successful;
	}

	public boolean canStop() {
		boolean canStop = false;
		for (Runner runner : this.runners) {
			if (runner.canStop()) {
				canStop = true;
				break;
			}
		}
		return canStop;
	}

	public boolean stop() {
		boolean successful = false;
		for (Runner runner : this.runners) {
			if (runner.stop()) {
				successful = true;
			}
		}
		return successful;
	}

	public boolean canReset() {
		boolean canReset = true;
		for (Runner runner : this.runners) {
			if (!runner.canReset()) {
				canReset = false;
				break;
			}
		}
		return canReset;
	}

	public boolean reset() {
		boolean successful = true;
		for (Runner runner : this.runners) {
			if (!runner.reset()) {
				successful = false;
			}
		}
		return successful;
	}

	public Runner[] getRunners() {
		return this.runners;
	}

	public int getStepDelay() {
		return this.stepDelay;
	}

}
