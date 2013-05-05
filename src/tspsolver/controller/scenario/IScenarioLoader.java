package tspsolver.controller.scenario;

import java.io.InputStream;

import tspsolver.model.Scenario;

public interface IScenarioLoader {

	public Scenario loadScenario(InputStream inputStream) throws IllegalArgumentException;

}
