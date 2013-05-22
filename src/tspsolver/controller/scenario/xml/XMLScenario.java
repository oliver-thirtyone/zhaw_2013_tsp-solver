package tspsolver.controller.scenario.xml;

public enum XMLScenario {

	// @formatter:off
	ELEMENT_SCENARIO("scenario"),
	ELEMENT_SCENARIO_ATTRIBUTE_NAME("name"),

	ELEMENT_VERTEX("vertex"),
	ELEMENT_STARTINGVERTEX("starting-vertex"),
	ELEMENT_VERTEX_ATTRIBUTE_NAME("name"),
	ELEMENT_VERTEX_ATTRIBUTE_X("x"),
	ELEMENT_VERTEX_ATTRIBUTE_Y("y"),
	ELEMENT_VERTEX_ATTRIBUTE_LINK("link"),

	ELEMENT_ADD_EDGE("add-edge"),
	ELEMENT_REMOVE_EDGE("remove-edge"),
	ELEMENT_EDGE_ATTRIBUTE_FIRSTVERTEX("first-vertex"),
	ELEMENT_EDGE_ATTRIBUTE_SECONDVERTEX("second-vertex"),
	ELEMENT_EDGE_ATTRIBUTE_WEIGHT("weight");
	// @formatter:on

	public static final String XML_SCHEMA = "data/scenario/schema/scenario.xsd";

	private String value;

	private XMLScenario(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

}
