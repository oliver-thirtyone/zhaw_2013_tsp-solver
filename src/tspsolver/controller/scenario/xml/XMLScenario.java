package tspsolver.controller.scenario.xml;

public enum XMLScenario {

	// @formatter:off
	ELEMENT_SCENARIO("scenario"),
	ELEMENT_SCENARIO_ATTRIBUTE_NAME("name"),

	ELEMENT_NODE("vertex"),
	ELEMENT_STARTINGNODE("starting-vertex"),
	ELEMENT_NODE_ATTRIBUTE_NAME("name"),
	ELEMENT_NODE_ATTRIBUTE_X("x"),
	ELEMENT_NODE_ATTRIBUTE_Y("y"),
	ELEMENT_NODE_ATTRIBUTE_LINK("link"),

	ELEMENT_ADD_EDGE("add-edge"),
	ELEMENT_REMOVE_EDGE("remove-edge"),
	ELEMENT_EDGE_ATTRIBUTE_FIRSTNODE("first-vertex"),
	ELEMENT_EDGE_ATTRIBUTE_SECONDNODE("second-vertex"),
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
