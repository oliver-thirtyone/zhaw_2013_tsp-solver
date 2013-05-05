package tspsolver.controller.scenario.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tspsolver.controller.scenario.IScenarioLoader;
import tspsolver.model.Scenario;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.GridFactory;

public class XMLScenarioLoader implements IScenarioLoader {

	private DocumentBuilder documentBuilder;
	private Validator validator;

	public XMLScenarioLoader() {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			this.documentBuilder = documentBuilderFactory.newDocumentBuilder();

			InputStream schema = new FileInputStream(XMLScenario.XML_SCHEMA);
			Source source = new StreamSource(schema);

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			this.validator = schemaFactory.newSchema(source).newValidator();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private boolean isXMLValid(Document document) throws SAXException, IOException {
		DOMSource domSource = new DOMSource(document);
		this.validator.validate(domSource);
		return true;
	}

	@Override
	public Scenario loadScenario(InputStream inputStream) throws IllegalArgumentException {
		Scenario scenario = null;
		try {
			Document document = this.documentBuilder.parse(inputStream);
			document.getDocumentElement().normalize();

			Map<String, tspsolver.model.grid.Node> nodes = new HashMap<String, tspsolver.model.grid.Node>();

			if (this.isXMLValid(document)) {
				NodeList rootXMLNodes = document.getChildNodes();
				for (int x = 0; x < rootXMLNodes.getLength(); x++) {
					Node rootXMLNode = rootXMLNodes.item(x);
					if (rootXMLNode.getNodeName().equals(XMLScenario.ELEMENT_SCENARIO.toString())) {
						scenario = this.parseScenario(rootXMLNode);

						NodeList xmlNodes = rootXMLNode.getChildNodes();
						for (int y = 0; y < xmlNodes.getLength(); y++) {
							Node xmlNode = xmlNodes.item(y);
							if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_NODE.toString()))
								this.parseNode(scenario.getGrid(), nodes, xmlNode);
							else if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_ADD_EDGE.toString()))
								this.parseAddEdge(scenario.getGrid(), nodes, xmlNode);
							else if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_REMOVE_EDGE.toString()))
								this.parseRemoveEdge(scenario.getGrid(), nodes, xmlNode);
							else if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_STARTINGNODE.toString()))
								this.parseStartingNode(scenario, nodes, xmlNode);
						}
					}
				}
			}
		} catch (SAXException exception) {
			throw new IllegalArgumentException(exception);
		} catch (IOException exception) {
			throw new IllegalArgumentException(exception);
		}

		return scenario;
	}

	private Scenario parseScenario(Node xmlNode) {
		Element scenarioElement = (Element) xmlNode;
		String name = scenarioElement.getAttribute(XMLScenario.ELEMENT_SCENARIO_ATTRIBUTE_NAME.toString());
		return new Scenario(name);
	}

	private void parseNode(Grid grid, Map<String, tspsolver.model.grid.Node> nodes, Node xmlNode) {
		Element nodeElement = (Element) xmlNode;

		String name = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_NAME.toString());
		String x = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_X.toString());
		String y = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_Y.toString());
		String link = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_LINK.toString());

		// Check if this node already exists
		if (nodes.containsKey(name)) {
			throw new IllegalArgumentException("Node already exists: " + name);
		}

		// Create the node
		tspsolver.model.grid.Node node = GridFactory.createNode(Integer.parseInt(x), Integer.parseInt(y));

		// Add the node to the grid
		if (!link.isEmpty()) {
			grid.addNode(node, Boolean.parseBoolean(link));
		} else {
			grid.addNode(node);
		}

		// Add the node to a temporary map for future references
		nodes.put(name, node);
	}

	private void parseAddEdge(Grid grid, Map<String, tspsolver.model.grid.Node> nodes, Node xmlNode) {
		Element addEdgeElement = (Element) xmlNode;

		String firstNodeName = addEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_FIRSTNODE.toString());
		String secondNodeName = addEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_SECONDNODE.toString());
		String weight = addEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_WEIGHT.toString());

		// Check if the referred nodes do exist
		if (!nodes.containsKey(firstNodeName)) {
			throw new IllegalArgumentException("Referred node does not exist: " + firstNodeName);
		}
		if (!nodes.containsKey(secondNodeName)) {
			throw new IllegalArgumentException("Referred node does not exist: " + secondNodeName);
		}

		// Get the nodes
		tspsolver.model.grid.Node firstNode = nodes.get(firstNodeName);
		tspsolver.model.grid.Node secondNode = nodes.get(secondNodeName);

		// Check if this edge already exists
		if (firstNode.hasEdgeToNode(secondNode)) {
			throw new IllegalArgumentException("Edge already exists: " + firstNodeName + " -> " + secondNodeName);
		}

		// Add the node to the grid
		if (!weight.isEmpty()) {
			GridFactory.addEdge(firstNode, secondNode, Double.parseDouble(weight));
		} else {
			GridFactory.addEdge(firstNode, secondNode);
		}
	}

	private void parseRemoveEdge(Grid grid, Map<String, tspsolver.model.grid.Node> nodes, Node xmlNode) {
		Element removeEdgeElement = (Element) xmlNode;

		String firstNodeName = removeEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_FIRSTNODE.toString());
		String secondNodeName = removeEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_SECONDNODE.toString());

		// Check if the referred nodes do exist
		if (!nodes.containsKey(firstNodeName)) {
			throw new IllegalArgumentException("Referred node does not exist: " + firstNodeName);
		}
		if (!nodes.containsKey(secondNodeName)) {
			throw new IllegalArgumentException("Referred node does not exist: " + secondNodeName);
		}

		// Get the nodes
		tspsolver.model.grid.Node firstNode = nodes.get(firstNodeName);
		tspsolver.model.grid.Node secondNode = nodes.get(secondNodeName);

		// Check if this edge exists
		if (!firstNode.hasEdgeToNode(secondNode)) {
			throw new IllegalArgumentException("Edge does not exist: " + firstNodeName + " -> " + secondNodeName);
		}

		// Remove the edge from the grid
		GridFactory.removeEdge(firstNode, secondNode);
	}

	private void parseStartingNode(Scenario scenario, Map<String, tspsolver.model.grid.Node> nodes, Node xmlNode) {
		Element nodeElement = (Element) xmlNode;

		String name = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_NAME.toString());

		// Check if this node exists
		if (!nodes.containsKey(name)) {
			throw new IllegalArgumentException("Referred starting node does not exist: " + name);
		}

		// Get the node
		tspsolver.model.grid.Node node = nodes.get(name);

		// Set the starting node for this scenario
		scenario.setStartingNode(node);
	}

}
