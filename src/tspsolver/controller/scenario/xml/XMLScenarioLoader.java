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

import tspsolver.controller.scenario.ScenarioLoader;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Grid;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;

public class XMLScenarioLoader implements ScenarioLoader {

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
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private boolean isXMLValid(Document document) throws SAXException, IOException {
		DOMSource domSource = new DOMSource(document);
		this.validator.validate(domSource);
		return true;
	}

	@Override
	public void loadScenario(Scenario scenario, InputStream inputStream) throws IllegalArgumentException {
		try {
			Document document = this.documentBuilder.parse(inputStream);
			document.getDocumentElement().normalize();

			Map<String, Vertex> vertices = new HashMap<String, Vertex>();

			if (this.isXMLValid(document)) {
				NodeList rootXMLNodes = document.getChildNodes();
				for (int x = 0; x < rootXMLNodes.getLength(); x++) {
					Node rootXMLVertex = rootXMLNodes.item(x);
					if (rootXMLVertex.getNodeName().equals(XMLScenario.ELEMENT_SCENARIO.toString())) {
						this.parseScenario(scenario, rootXMLVertex);

						NodeList xmlNodes = rootXMLVertex.getChildNodes();
						for (int y = 0; y < xmlNodes.getLength(); y++) {
							Node xmlNode = xmlNodes.item(y);
							if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_NODE.toString())) {
								this.parseVertex(scenario.getGrid(), vertices, xmlNode);
							}
							else if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_ADD_EDGE.toString())) {
								this.parseAddEdge(scenario.getGrid(), vertices, xmlNode);
							}
							else if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_REMOVE_EDGE.toString())) {
								this.parseRemoveEdge(scenario.getGrid(), vertices, xmlNode);
							}
							else if (xmlNode.getNodeName().equals(XMLScenario.ELEMENT_STARTINGNODE.toString())) {
								this.parseStartingVertex(scenario, vertices, xmlNode);
							}
						}
					}
				}
			}
		}
		catch (SAXException exception) {
			throw new IllegalArgumentException(exception);
		}
		catch (IOException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	private void parseScenario(Scenario scenario, Node xmlNode) {
		Element scenarioElement = (Element) xmlNode;
		String name = scenarioElement.getAttribute(XMLScenario.ELEMENT_SCENARIO_ATTRIBUTE_NAME.toString());

		scenario.setName(name);
	}

	private void parseVertex(Grid grid, Map<String, Vertex> vertices, Node xmlNode) {
		Element nodeElement = (Element) xmlNode;

		String name = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_NAME.toString());
		String x = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_X.toString());
		String y = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_Y.toString());
		String link = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_LINK.toString());

		// Check if this vertex already exists
		if (vertices.containsKey(name)) {
			throw new IllegalArgumentException("Vertex already exists: " + name);
		}

		// Create the vertex
		tspsolver.model.scenario.grid.Vertex vertex = GridFactory.createVertex(name, Integer.parseInt(x), Integer.parseInt(y));

		// Add the vertex to the grid
		if (!link.isEmpty()) {
			GridFactory.addVertex(grid, vertex, Boolean.parseBoolean(link));
		}
		else {
			GridFactory.addVertex(grid, vertex);
		}

		// Add the vertex to a temporary map for future references
		vertices.put(name, vertex);
	}

	private void parseAddEdge(Grid grid, Map<String, Vertex> vertices, Node xmlNode) {
		Element addEdgeElement = (Element) xmlNode;

		String firstVertexName = addEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_FIRSTNODE.toString());
		String secondVertexName = addEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_SECONDNODE.toString());
		String weight = addEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_WEIGHT.toString());

		// Check if the referred vertices do exist
		if (!vertices.containsKey(firstVertexName)) {
			throw new IllegalArgumentException("Referred vertex does not exist: " + firstVertexName);
		}
		if (!vertices.containsKey(secondVertexName)) {
			throw new IllegalArgumentException("Referred vertex does not exist: " + secondVertexName);
		}

		// Get the vertices
		tspsolver.model.scenario.grid.Vertex firstVertex = vertices.get(firstVertexName);
		tspsolver.model.scenario.grid.Vertex secondVertex = vertices.get(secondVertexName);

		// Check if this edge already exists
		if (firstVertex.hasEdgeToVertex(secondVertex)) {
			throw new IllegalArgumentException("Edge already exists: " + firstVertexName + " -> " + secondVertexName);
		}

		// Add the vertex to the grid
		if (!weight.isEmpty()) {
			GridFactory.addEdge(firstVertex, secondVertex, Double.parseDouble(weight));
		}
		else {
			GridFactory.addEdge(firstVertex, secondVertex);
		}
	}

	private void parseRemoveEdge(Grid grid, Map<String, Vertex> vertices, Node xmlNode) {
		Element removeEdgeElement = (Element) xmlNode;

		String firstVertexName = removeEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_FIRSTNODE.toString());
		String secondVertexName = removeEdgeElement.getAttribute(XMLScenario.ELEMENT_EDGE_ATTRIBUTE_SECONDNODE.toString());

		// Check if the referred vertices do exist
		if (!vertices.containsKey(firstVertexName)) {
			throw new IllegalArgumentException("Referred vertex does not exist: " + firstVertexName);
		}
		if (!vertices.containsKey(secondVertexName)) {
			throw new IllegalArgumentException("Referred vertex does not exist: " + secondVertexName);
		}

		// Get the vertices
		tspsolver.model.scenario.grid.Vertex firstVertex = vertices.get(firstVertexName);
		tspsolver.model.scenario.grid.Vertex secondVertex = vertices.get(secondVertexName);

		// Check if this edge exists
		if (!firstVertex.hasEdgeToVertex(secondVertex)) {
			throw new IllegalArgumentException("Edge does not exist: " + firstVertexName + " -> " + secondVertexName);
		}

		// Remove the edge from the grid
		GridFactory.removeEdge(firstVertex, secondVertex);
	}

	private void parseStartingVertex(Scenario scenario, Map<String, Vertex> vertices, Node xmlNode) {
		Element nodeElement = (Element) xmlNode;

		String name = nodeElement.getAttribute(XMLScenario.ELEMENT_NODE_ATTRIBUTE_NAME.toString());

		// Check if this vertex exists
		if (!vertices.containsKey(name)) {
			throw new IllegalArgumentException("Referred starting vertex does not exist: " + name);
		}

		// Get the vertex
		tspsolver.model.scenario.grid.Vertex vertex = vertices.get(name);

		// Set the starting vertex for this scenario
		scenario.setStartingVertex(vertex);
	}

}
