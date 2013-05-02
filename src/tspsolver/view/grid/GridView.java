package tspsolver.view.grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.Node;
import tspsolver.model.grid.Path;
import tspsolver.model.grid.updates.NodeUpdate;
import tspsolver.model.grid.updates.StartingNodeUpdate;
import tspsolver.model.grid.updates.UpdateAction;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.Line;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;

public class GridView extends JPanel implements Observer {

	public static final String DATA_MAP_SWITZERLAND = "map/switzerland_simple.svg";
	public static final String SVG_GROUP_NODES = "tspsolver.nodes";
	public static final String SVG_GROUP_EDGES = "tspsolver.edges";

	private static final long serialVersionUID = -5210001067574218993L;

	private final Grid grid;
	private final Path path;

	private final SVGDiagram svgDiagram;
	private final SVGIcon svgIcon;

	private final Map<Node, NodeView> nodeViews;
	private final Map<Edge, Line> svgLines;

	public GridView(Grid grid, Path path) {
		this.grid = grid;
		this.path = path;

		InputStream svgImage = this.getClass().getClassLoader().getResourceAsStream(GridView.DATA_MAP_SWITZERLAND);
		URI svgURI = null;
		try {
			svgURI = SVGCache.getSVGUniverse().loadSVG(svgImage, GridView.DATA_MAP_SWITZERLAND);
		}
		catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Unable to load svg image : " + GridView.DATA_MAP_SWITZERLAND, "Error loading svg image", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}

		this.svgDiagram = SVGCache.getSVGUniverse().getDiagram(svgURI);

		this.svgIcon = new SVGIcon();
		this.svgIcon.setSvgURI(svgURI);
		this.svgIcon.setAntiAlias(true);

		this.nodeViews = new HashMap<Node, NodeView>();
		this.svgLines = new HashMap<Edge, Line>();

		// Observe the grid and the path
		this.grid.addObserver(this);
		this.path.addObserver(this);

		this.update(this.grid, this.grid);
	}

	protected Grid getGrid() {
		return this.grid;
	}

	protected Path getPath() {
		return this.path;
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
		if (argument instanceof NodeUpdate) {
			NodeUpdate update = (NodeUpdate) argument;
			UpdateAction action = update.getAction();

			Node node = update.getNode();
			NodeView nodeView = null;

			switch (action) {
				case ADD:
					nodeView = new NodeView(node, this);
					nodeView.createCircle();
					this.nodeViews.put(node, nodeView);
					break;
				case REMOVE:
					nodeView = this.nodeViews.get(node);
					nodeView.deleteCircle();
					this.nodeViews.remove(node);
					break;
			}
		}
		else if (argument instanceof StartingNodeUpdate) {
			NodeUpdate update = (StartingNodeUpdate) argument;
			UpdateAction action = update.getAction();

			Node node = update.getNode();
			NodeView nodeView = this.nodeViews.get(node);

			// If this node is not visible, we can not change its color
			if (nodeView == null) {
				break;
			}

			switch (action) {
				case ADD:
					nodeView.modifyCircle(NodeView.STARTING_NODE_COLOR);
					break;
				case REMOVE:
					nodeView.modifyCircle(NodeView.NORMAL_NODE_COLOR);
					break;
			}
		}

		// Remove the current circles
		Node[] currentNodes = this.svgCircles.keySet().toArray(new Node[this.svgCircles.size()]);
		for (Node node : currentNodes) {
			this.destroyCircle(node);
			this.svgCircles.remove(node);
		}

		// Remove the current lines
		Edge[] currentEdges = this.svgLines.keySet().toArray(new Edge[this.svgLines.size()]);
		for (Edge egde : currentEdges) {
			this.destroyLine(egde);
			this.svgLines.remove(egde);
		}

		// Create the new circles
		for (Node node : this.grid.getNodes()) {
			// Skip the nodes that already exist
			if (this.svgCircles.containsKey(node)) {
				continue;
			}

			Circle circle = this.createCircle(node);
			this.svgCircles.put(node, circle);

			// Create the new edges
			for (Edge edge : node.getEdgeCollection()) {
				// Skip the edges that already exist
				if (this.svgLines.containsKey(edge)) {
					continue;
				}

				Line line = this.createLine(edge);
				this.svgLines.put(edge, line);
			}
		}

		// Color the starting node
		Circle startingCircle = this.svgCircles.get(this.grid.getStartingNode());
		if (startingCircle != null) {
			try {
				startingCircle.addAttribute("fill", AnimationElement.AT_CSS, "#0000FF");
			}
			catch (SVGElementException exception) {
				// TODO: uncomment
				// exception.printStackTrace();
			}
		}

		// Color the path
		this.updatePath();

		// Update the diagram
		try {
			this.svgDiagram.updateTime(0.0);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	private void updatePath() {

	}

	private Circle createCircle(Node node) {
		Group group = (Group) this.svgDiagram.getElement(GridView.SVG_GROUP_NODES);
		Circle circle = new Circle();

		try {
			circle.addAttribute("cx", AnimationElement.AT_XML, String.valueOf(node.getX()));
			circle.addAttribute("cy", AnimationElement.AT_XML, String.valueOf(node.getY()));
			circle.addAttribute("r", AnimationElement.AT_XML, "5");

			group.loaderAddChild(null, circle);
		}
		catch (SVGException exception) {
			// TODO: uncomment
			// exception.printStackTrace();
		}

		return circle;
	}

	private Circle destroyCircle(Node node) {
		Group group = (Group) this.svgDiagram.getElement(GridView.SVG_GROUP_EDGES);
		Circle circle = this.svgCircles.get(node);

		try {
			group.removeChild(circle);
		}
		catch (SVGException exception) {
			// TODO: uncomment
			// exception.printStackTrace();
		}

		return circle;
	}

	private Line createLine(Edge edge) {
		Group group = (Group) this.svgDiagram.getElement(GridView.SVG_GROUP_EDGES);
		Line line = new Line();

		try {
			line.addAttribute("x1", AnimationElement.AT_XML, String.valueOf(edge.getFirstNode().getX()));
			line.addAttribute("y1", AnimationElement.AT_XML, String.valueOf(edge.getFirstNode().getY()));
			line.addAttribute("x2", AnimationElement.AT_XML, String.valueOf(edge.getSecondNode().getX()));
			line.addAttribute("y2", AnimationElement.AT_XML, String.valueOf(edge.getSecondNode().getY()));

			if (!edge.isAccessible()) {
				line.addAttribute("stroke-dasharray", AnimationElement.AT_CSS, "2, 4");
			}

			// TODO: display non-accessible edges different

			group.loaderAddChild(null, line);
		}
		catch (SVGException e) {
			// TODO: uncomment
			// exception.printStackTrace();
		}

		return line;
	}

	private Line destroyLine(Edge edge) {
		Group group = (Group) this.svgDiagram.getElement(GridView.SVG_GROUP_NODES);
		Line line = this.svgLines.get(edge);

		try {
			group.removeChild(line);
		}
		catch (SVGException exception) {
			// TODO: uncomment
			// exception.printStackTrace();
		}

		return line;
	}

}
