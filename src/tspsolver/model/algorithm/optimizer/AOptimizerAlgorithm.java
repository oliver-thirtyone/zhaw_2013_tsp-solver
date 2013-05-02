package tspsolver.model.algorithm.optimizer;

import tspsolver.model.algorithm.Algorithm;
import tspsolver.model.grid.Path;

public abstract class AOptimizerAlgorithm extends Algorithm {

	public AOptimizerAlgorithm(Path path) {
		super(path);
	}

	@Override
	public final void validateArguments() {
		boolean validArguments = true;

		// TODO: further validations...

		this.setValidArguments(validArguments);
	}

}
