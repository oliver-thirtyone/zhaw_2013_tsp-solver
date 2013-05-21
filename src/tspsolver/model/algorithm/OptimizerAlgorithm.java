package tspsolver.model.algorithm;

import tspsolver.model.scenario.Scenario;

public abstract class OptimizerAlgorithm extends Algorithm {

	@Override
	public synchronized final void validateArguments() {
		boolean validArguments = true;

		Scenario scenario = this.getScenario();
		if (scenario != null) {
			if (!scenario.isPathValid()) {
				validArguments = false;
			}
		}
		else {
			validArguments = false;
		}

		this.setValidArguments(validArguments);
	}
}
