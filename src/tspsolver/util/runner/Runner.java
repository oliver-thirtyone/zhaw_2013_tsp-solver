package tspsolver.util.runner;

import java.util.Observable;

public abstract class Runner extends Observable implements Runnable {

	public final static int DEFAULT_STEP_DELAY = 0;
	public final static int PAUSE_SLEEP_MILLISECONDS = 300;

	private Thread thread;
	private RunnerState state;

	private long timeStarted;
	private long timeStopped;

	private int stepDelay;
	private long stepCounter;

	public Runner() {
		this.thread = null;
		this.state = RunnerState.NOT_READY;

		this.timeStarted = 0;
		this.timeStopped = 0;

		this.stepDelay = 0;
		this.stepCounter = 0;
	}

	protected abstract boolean doInitialize();

	protected abstract boolean doReset();

	protected abstract boolean doStep();

	@Override
	public final void run() {
		this.timeStarted = System.currentTimeMillis();

		while (this.isRunning()) {

			// Sleep and continue if we are PAUSED
			if (this.getState() == RunnerState.PAUSED) {
				try {
					Thread.sleep(Runner.PAUSE_SLEEP_MILLISECONDS);
				}
				catch (InterruptedException exception) {
					exception.printStackTrace();
				}
				continue;
			}

			// Sleep between the steps to create a delay
			// TODO: only when state = running
			if (this.getStepDelay() > 0) {
				try {
					Thread.sleep(this.getStepDelay());
				}
				catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}

			// Take the next step
			if (this.doStep()) {
				this.stepCounter++;
			}

			// Switch to PAUSE if we are STEPPING
			if (this.getState() == RunnerState.STEPPING) {
				this.setState(RunnerState.PAUSED);
			}
			else {
				// Notify the observers
				this.setChanged();
				this.notifyObservers();
			}
		}

		this.timeStopped = System.currentTimeMillis();
	}

	public synchronized boolean canInitialize() {
		switch (this.getState()) {
			case NOT_READY:
				return true;
			default:
				break;
		}

		return false;
	}

	public synchronized boolean initialize() {
		return this.initialize(0);
	}

	public synchronized boolean initialize(int stepDelay) {
		boolean successful = false;

		if (this.canInitialize()) {
			if (this.doInitialize()) {
				this.thread = new Thread(this);

				this.stepDelay = stepDelay;
				this.setState(RunnerState.READY);
				successful = true;
			}
		}

		return successful;
	}

	public final synchronized boolean canReset() {
		switch (this.getState()) {
			case READY:
			case STOPPED:
				return true;
			default:
				break;
		}

		return false;
	}

	public final synchronized boolean reset() {
		boolean successful = false;

		if (this.canReset()) {
			if (this.doReset()) {
				this.thread = null;

				this.timeStarted = 0;
				this.timeStopped = 0;

				this.stepDelay = 0;
				this.stepCounter = 0;

				this.setState(RunnerState.NOT_READY);
				successful = true;
			}
		}

		return successful;
	}

	public final synchronized boolean canStart() {
		switch (this.getState()) {
			case READY:
			case PAUSED:
				return true;
			default:
				break;
		}

		return false;
	}

	public final synchronized boolean start() {
		boolean successful = false;

		if (this.canStart()) {
			if (this.getState() == RunnerState.READY && !this.thread.isAlive()) {
				this.thread.start();
			}
			this.setState(RunnerState.RUNNING);
			successful = true;
		}

		return successful;
	}

	public final synchronized boolean canStep() {
		switch (this.getState()) {
			case READY:
			case PAUSED:
				return true;
			default:
				break;
		}

		return false;
	}

	public final synchronized boolean step() {
		boolean successful = false;

		if (this.canStep()) {
			if (this.getState() == RunnerState.READY && !this.thread.isAlive()) {
				this.thread.start();
			}
			this.setState(RunnerState.STEPPING);
			successful = true;
		}

		return successful;
	}

	public final synchronized boolean canPause() {
		switch (this.getState()) {
			case RUNNING:
			case STEPPING:
			case STOPPED:
				return true;
			default:
				break;
		}

		return false;
	}

	public final synchronized boolean pause() {
		boolean successful = false;

		if (this.canPause()) {
			this.setState(RunnerState.PAUSED);
			successful = true;
		}

		return successful;
	}

	public final synchronized boolean canStop() {
		switch (this.getState()) {
			case RUNNING:
			case STEPPING:
			case PAUSED:
				return true;
			default:
				break;
		}

		return false;
	}

	public final synchronized boolean stop() {
		boolean successful = false;

		if (this.canStop()) {
			this.setState(RunnerState.STOPPED);
			successful = true;
		}

		return successful;
	}

	public final synchronized RunnerState getState() {
		return this.state;
	}

	protected final synchronized void setState(RunnerState state) {
		if (this.state == state) {
			return;
		}

		this.state = state;
		this.setChanged();
		this.notifyObservers(this.state);
	}

	public final synchronized long getTimeElapsed() {
		long timeCurrent = System.currentTimeMillis();

		long timeStarted = this.timeStarted != 0 ? this.timeStarted : timeCurrent;
		long timeStopped = this.timeStopped != 0 ? this.timeStopped : timeCurrent;

		return timeStopped - timeStarted;
	}

	public final synchronized int getStepDelay() {
		return this.stepDelay;
	}

	public final synchronized long getStepCounter() {
		return this.stepCounter;
	}

	public final synchronized boolean isRunning() {
		RunnerState state = this.getState();
		return state == RunnerState.RUNNING || state == RunnerState.STEPPING || state == RunnerState.PAUSED;
	}
}
