package tspsolver.controller;

import java.util.Observable;

import tspsolver.model.algorithm.Algorithm;

public class Runner extends Observable implements Runnable {

	public final static Integer PAUSE_SLEEP_MILLISECONDS = 300;

	private final Algorithm algorithm;

	private Thread thread;
	private RunnerState state;
	private int stepDelay;
	private long stepCounter;

	public Runner(Algorithm algorithm) {
		this.algorithm = algorithm;

		this.thread = null;
		this.state = RunnerState.NOT_READY;
		this.stepDelay = 0;
		this.stepCounter = 0;
	}

	@Override
	public void run() {
		while (this.isRunning()) {

			// Sleep and continue if we are paused
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
			if (this.getStepDelay() != 0) {
				try {
					Thread.sleep(this.getStepDelay());
				}
				catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}

			// Take the next step in the algorithm
			boolean successfulStep = this.algorithm.step();
			this.stepCounter++;

			// Stop if there was an error or we are done
			if (!successfulStep || this.algorithm.hasFinishedSuccessful()) {
				this.setState(RunnerState.STOPPED);
			}

			// Switch to pause if we run in STEP-Mode
			if (this.getState() == RunnerState.STEPPING) {
				this.setState(RunnerState.PAUSED);
			}

			this.setChanged();
			this.notifyObservers();
		}
	}

	public synchronized boolean canReset() {
		switch (this.getState()) {
			case READY:
			case STOPPED:
				return true;
			default:
				break;
		}

		return false;
	}

	public synchronized boolean reset() {
		boolean successful = false;

		if (this.canReset()) {
			this.algorithm.reset();

			this.thread = null;
			this.stepDelay = 0;
			this.stepCounter = 0;

			this.setState(RunnerState.NOT_READY);
			successful = true;
		}

		return successful;
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

	public synchronized boolean initialize(int instructionDelay) {
		boolean successful = false;

		if (this.canInitialize()) {
			this.algorithm.validateArguments();
			if (this.algorithm.hasValidArguments()) {
				this.thread = new Thread(this, this.algorithm.getClass().getName());

				this.stepDelay = instructionDelay;
				this.setState(RunnerState.READY);
				successful = true;
			}
		}

		return successful;
	}

	public synchronized boolean canStart() {
		switch (this.getState()) {
			case READY:
			case PAUSED:
				return true;
			default:
				break;
		}

		return false;
	}

	public synchronized boolean start() {
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

	public synchronized boolean canStep() {
		switch (this.getState()) {
			case READY:
			case PAUSED:
				return true;
			default:
				break;
		}

		return false;
	}

	public synchronized boolean step() {
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

	public synchronized boolean canPause() {
		switch (this.getState()) {
			case RUNNING:
			case STEPPING:
				return true;
			default:
				break;
		}

		return false;
	}

	public synchronized boolean pause() {
		boolean successful = false;

		if (this.canPause()) {
			this.setState(RunnerState.PAUSED);
			successful = true;
		}

		return successful;
	}

	public synchronized boolean canStop() {
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

	public synchronized boolean stop() {
		boolean successful = false;

		if (this.canStop()) {
			this.setState(RunnerState.STOPPED);
			successful = true;
		}

		return successful;
	}

	public synchronized RunnerState getState() {
		return this.state;
	}

	private synchronized void setState(RunnerState mode) {
		if (this.state == mode) {
			return;
		}

		this.state = mode;
		this.setChanged();
		this.notifyObservers(this.state);
	}

	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public int getStepDelay() {
		return this.stepDelay;
	}

	public long getStepCounter() {
		return this.stepCounter;
	}

	private boolean isRunning() {
		return this.getState() == RunnerState.RUNNING || this.getState() == RunnerState.STEPPING || this.getState() != RunnerState.PAUSED;
	}
}
