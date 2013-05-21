package tspsolver.model.validator;

import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.path.Path;

public interface Validator {

	public abstract boolean validateScenario(Scenario scenario);

	public abstract boolean validateGrid(Scenario scenario, Grid grid);

	public abstract boolean validatePath(Scenario scenario, Grid grid, Path path);

}