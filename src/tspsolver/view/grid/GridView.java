package tspsolver.view.grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import tspsolver.model.scenario.Scenario;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;
import tspsolver.model.updates.ElementUpdate;
import tspsolver.model.updates.grid.EdgeUpdate;
import tspsolver.model.updates.grid.EdgeUpdateAction;
import tspsolver.model.updates.grid.NodeUpdate;
import tspsolver.model.updates.grid.NodeUpdateAction;
import tspsolver.model.updates.path.PathUpdate;
import tspsolver.model.updates.path.PathUpdateAction;
import tspsolver.model.updates.scenario.StartingNodeUpdate;
import tspsolver.model.updates.scenario.StartingNodeUpdateAction;
import tspsolver.util.copy.FileCopy;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.app.beans.SVGIcon;

public class GridView extends JPanel implements Observer {

	public static final int MAP_SWITZERLAND_WIDTH = 500;
	public static final int MAP_SWITZERLAND_HEIGHT = 320;

	public static final String DATA_MAP_SWITZERLAND = "data/map/switzerland_simple.svg";
	public static final String SVG_GROUP_NODES = "tspsolver.nodes";
	public static final String SVG_GROUP_EDGES = "tspsolver.edges";

	private static final long serialVersionUID = -5210001067574218993L;

	private final SVGDiagram svgDiagram;
	private final SVGIcon svgIcon;

	private final Map<Node, NodeView> nodeViews;
	private final Map<Edge, EdgeView> edgeViews;

	private Scenario scenario;

	public GridView() {
		URI svgURI = null;
		try {
			File mapFile = new File(GridView.DATA_MAP_SWITZERLAND);
			File tempFile = File.createTempFile("tspsolver_map_switzerland", ".svg");
			FileCopy.copy(mapFile, tempFile);

			InputStream svgImage = new FileInputStream(tempFile);
			svgURI = SVGCache.getSVGUniverse().loadSVG(svgImage, tempFile.getName());
		}
		catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Unable to load svg image: " + GridView.DATA_MAP_SWITZERLAND, "Error loading svg image", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}

		// Create the diagram
		this.svgDiagram = SVGCache.getSVGUniverse().getDiagram(svgURI);

		// Create the map
		this.svgIcon = new SVGIcon();
		this.svgIcon.setSvgURI(svgURI);
		this.svgIcon.setAntiAlias(true);
		this.svgIcon.setPreferredSize(new Dimension(GridView.MAP_SWITZERLAND_WIDTH, GridView.MAP_SWITZERLAND_HEIGHT));
		this.svgIcon.setScaleToFit(true);

		// Create the views
		this.nodeViews = new HashMap<Node, NodeView>();
		this.edgeViews = new HashMap<Edge, EdgeView>();

		// Create the titled border
		final TitledBorder titledBorder = BorderFactory.createTitledBorder("");
		titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		this.setBorder(titledBorder);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		this.svgIcon.paintIcon(this, graphics, 0, 0);
	}

	@Override
	public void update(Observable observable, final Object argument) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ElementUpdate<?, ?> elementUpdate = (ElementUpdate<?, ?>) argument;
				GridView.this.doUpdate(elementUpdate);
			}
		});
	}

	public void updateScenario(final Scenario scenario) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GridView.this.doUpdateScenario(scenario);
			}
		});
	}

	private synchronized void doUpdate(ElementUpdate<?, ?> elementUpdate) {
		// Starting node updates
		if (elementUpdate instanceof StartingNodeUpdate) {
			final StartingNodeUpdate update = (StartingNodeUpdate) elementUpdate;
			final StartingNodeUpdateAction action = update.getAction();

			final Node node = update.getElement();
			final NodeView nodeView = this.nodeViews.get(node);

			if (nodeView != null) {
				switch (action) {
					case ADD_STARTING_NODE:
						nodeView.updateCircle(NodeView.STARTING_NODE_COLOR);
						break;
					case REMOVE_STARTING_NODE:
						nodeView.updateCircle(NodeView.NORMAL_NODE_COLOR);
						break;
					default:
						break;
				}
			}
		}

		// Node updates
		else if (elementUpdate instanceof NodeUpdate) {
			final NodeUpdate update = (NodeUpdate) elementUpdate;
			final NodeUpdateAction action = update.getAction();

			final Node node = update.getElement();
			NodeView nodeView = null;

			switch (action) {
				case ADD_NODE:
					if (!this.nodeViews.containsKey(node)) {
						nodeView = new NodeView(node, this.svgDiagram);
						nodeView.createCircle();
						this.nodeViews.put(node, nodeView);
					}
					break;
				case REMOVE_NODE:
					if (this.nodeViews.containsKey(node)) {
						nodeView = this.nodeViews.get(node);
						nodeView.deleteCircle();
						this.nodeViews.remove(node);
					}
					break;
				default:
					break;
			}
		}

		// Path updates
		else if (elementUpdate instanceof PathUpdate) {
			final PathUpdate update = (PathUpdate) elementUpdate;
			final PathUpdateAction action = update.getAction();

			final Edge edge = update.getElement();
			final EdgeView edgeView = this.edgeViews.get(edge);

			if (edgeView != null) {
				switch (action) {
					case PATH_ELEMENT:
						edgeView.updateLine(EdgeView.PATH_COLOR, false);
						break;
					case NEW_PATH_ELEMENT:
						edgeView.updateLine(EdgeView.NEW_PATH_COLOR, false);
						break;
					case OLD_PATH_ELEMENT:
						edgeView.updateLine(EdgeView.OLD_PATH_COLOR, true);
						break;
					case NON_PATH_ELEMENT:
						edgeView.updateLine(EdgeView.EDGE_COLOR, true);
						break;
					default:
						break;
				}
			}
		}

		// Edge updates
		else if (elementUpdate instanceof EdgeUpdate) {
			final EdgeUpdate update = (EdgeUpdate) elementUpdate;
			final EdgeUpdateAction action = update.getAction();

			final Edge edge = update.getElement();
			EdgeView edgeView = null;

			switch (action) {
				case ADD_EDGE:
					if (!this.edgeViews.containsKey(edge)) {
						edgeView = new EdgeView(edge, this.svgDiagram);
						edgeView.createLine();
						this.edgeViews.put(edge, edgeView);
					}
					break;
				case REMOVE_EDGE:
					if (this.edgeViews.containsKey(edge)) {
						edgeView = this.edgeViews.get(edge);
						edgeView.deleteLine();
						this.edgeViews.remove(edge);
					}
					break;
				default:
					break;
			}
		}

		// Update the diagram
		try {
			this.svgDiagram.updateTime(0.0);
			this.repaint();
		}
		catch (final SVGException exception) {
			exception.printStackTrace();
		}
	}

	private synchronized void doUpdateScenario(Scenario scenario) {
		// Clear the current grid
		if (this.scenario != null) {
			this.clearGrid();
			this.scenario.deleteObserver(this);
		}

		// Update the scenario
		this.scenario = scenario;

		// Paint the new grid
		if (this.scenario != null) {
			this.scenario.addObserver(this);
			this.paintGrid();
		}

		// Update the diagram
		try {
			this.svgDiagram.updateTime(0.0);
			this.repaint();
		}
		catch (final SVGException exception) {
			exception.printStackTrace();
		}
	}

	private void paintGrid() {
		for (final Node node : this.scenario.getGrid().getNodes()) {
			for (final Edge edge : node.getEdges()) {
				this.scenario.update(this.scenario, new EdgeUpdate(edge, EdgeUpdateAction.ADD_EDGE));
			}

			this.scenario.update(this.scenario, new NodeUpdate(node, NodeUpdateAction.ADD_NODE));
		}

		final Node startingNode = this.scenario.getStartingNode();
		if (startingNode != null) {
			this.scenario.update(this.scenario, new StartingNodeUpdate(startingNode, StartingNodeUpdateAction.ADD_STARTING_NODE));
		}

		for (final Edge edge : this.scenario.getPath().getEdges()) {
			this.scenario.update(this.scenario, new PathUpdate(edge, PathUpdateAction.PATH_ELEMENT));
		}
	}

	private void clearGrid() {
		for (final Edge edge : this.scenario.getPath().getEdges()) {
			this.scenario.update(this.scenario, new PathUpdate(edge, PathUpdateAction.NON_PATH_ELEMENT));
		}

		final Node startingNode = this.scenario.getStartingNode();
		if (startingNode != null) {
			this.scenario.update(this.scenario, new StartingNodeUpdate(startingNode, StartingNodeUpdateAction.REMOVE_STARTING_NODE));
		}

		for (final Node node : this.scenario.getGrid().getNodes()) {
			this.scenario.update(this.scenario, new NodeUpdate(node, NodeUpdateAction.REMOVE_NODE));

			for (final Edge edge : node.getEdges()) {
				this.scenario.update(this.scenario, new EdgeUpdate(edge, EdgeUpdateAction.REMOVE_EDGE));
			}
		}
	}
}
