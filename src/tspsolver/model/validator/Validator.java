package tspsolver.model.validator;

import tspsolver.model.scenario.Scenario;

public interface Validator {

	public abstract boolean validateScenario(Scenario scenario);

	public abstract boolean validateGrid(Scenario scenario);

	public abstract boolean validatePath(Scenario scenario);

}