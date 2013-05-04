package tspsolver.model.algorithm.optimizer;

import tspsolver.model.Scenario;
import tspsolver.model.algorithm.Algorithm;

public abstract class AOptimizerAlgorithm extends Algorithm {

	public AOptimizerAlgorithm(Scenario scenario) {
		super(scenario);
	}

	@Override
	public final void validateArguments() {
		boolean validArguments = true;

		// TODO: validate the path: Entspricht er den Vorgaben eines TSP-Pfades

		this.setValidArguments(validArguments);
	}

}
