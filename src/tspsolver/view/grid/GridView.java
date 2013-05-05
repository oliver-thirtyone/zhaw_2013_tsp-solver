package tspsolver.view.grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tspsolver.model.Scenario;
import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Node;
import tspsolver.model.updates.EdgeUpdate;
import tspsolver.model.updates.NodeUpdate;
import tspsolver.model.updates.PathUpdate;
import tspsolver.model.updates.StartingNodeUpdate;
import tspsolver.model.updates.UpdateAction;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.app.beans.SVGIcon;

public class GridView extends JPanel implements Observer {

	public static final String DATA_MAP_SWITZERLAND = "data/map/switzerland_simple.svg";
	public static final String SVG_GROUP_NODES = "tspsolver.nodes";
	public static final String SVG_GROUP_EDGES = "tspsolver.edges";

	private static final long serialVersionUID = -5210001067574218993L;

	private final SVGDiagram svgDiagram;
	private final SVGIcon svgIcon;

	private final Map<Node, NodeView> nodeViews;
	private final Map<Edge, EdgeView> edgeViews;

	public GridView(Scenario scenario) {
		URI svgURI = null;
		try {
			InputStream svgImage = new FileInputStream(GridView.DATA_MAP_SWITZERLAND);
			svgURI = SVGCache.getSVGUniverse().loadSVG(svgImage, GridView.DATA_MAP_SWITZERLAND);
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Unable to load svg image: " + GridView.DATA_MAP_SWITZERLAND, "Error loading svg image", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}

		this.svgDiagram = SVGCache.getSVGUniverse().getDiagram(svgURI);

		this.svgIcon = new SVGIcon();
		this.svgIcon.setSvgURI(svgURI);
		this.svgIcon.setAntiAlias(true);

		this.nodeViews = new HashMap<Node, NodeView>();
		this.edgeViews = new HashMap<Edge, EdgeView>();

		// Observe the scenario
		scenario.addObserver(this);
	}

	protected SVGDiagram getSVGDiagram() {
		return this.svgDiagram;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		this.svgIcon.setPreferredSize(new Dimension(600, 385));
		this.svgIcon.setScaleToFit(true);
		this.svgIcon.paintIcon(this, graphics, 0, 0);
	}

	@Override
	public void update(Observable observable, Object argument) {
		// Starting node updates
		if (argument instanceof StartingNodeUpdate) {
			NodeUpdate update = (StartingNodeUpdate) argument;
			UpdateAction action = update.getAction();

			Node node = update.getNode();
			NodeView nodeView = this.nodeViews.get(node);

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
		else if (argument instanceof NodeUpdate) {
			NodeUpdate update = (NodeUpdate) argument;
			UpdateAction action = update.getAction();

			Node node = update.getNode();
			NodeView nodeView = null;

			switch (action) {
			case ADD_NODE:
				if (!this.nodeViews.containsKey(node)) {
					nodeView = new NodeView(node, this);
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
		else if (argument instanceof PathUpdate) {
			PathUpdate update = (PathUpdate) argument;
			UpdateAction action = update.getAction();

			Edge edge = update.getEdge();
			EdgeView edgeView = this.edgeViews.get(edge);

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
		else if (argument instanceof EdgeUpdate) {
			EdgeUpdate update = (EdgeUpdate) argument;
			UpdateAction action = update.getAction();

			Edge edge = update.getEdge();
			EdgeView edgeView = null;

			switch (action) {
			case ADD_EDGE:
				if (!this.edgeViews.containsKey(edge)) {
					edgeView = new EdgeView(edge, this);
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
		} catch (SVGException exception) {
			exception.printStackTrace();
		}
	}
}
