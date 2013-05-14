package tspsolver.controller.scenario;

import java.io.InputStream;

import tspsolver.model.scenario.Scenario;

public interface ScenarioLoader {

	public void loadScenario(Scenario scenario, InputStream inputStream) throws IllegalArgumentException;

}
