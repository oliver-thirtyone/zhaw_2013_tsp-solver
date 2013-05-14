package tspsolver.model.algorithm;

import tspsolver.model.scenario.Scenario;

public abstract class StartAlgorithm extends Algorithm {

	@Override
	public final synchronized void validateArguments() {
		boolean validArguments = true;

		Scenario scenario = this.getScenario();
		if (scenario != null) {
			if (!scenario.isScenarioValid()) {
				validArguments = false;
			}
			if (!scenario.isGridValid()) {
				validArguments = false;
			}
		}
		else {
			validArguments = false;
		}

		this.setValidArguments(validArguments);
	}

}
