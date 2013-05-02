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

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.Line;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;

public class GridView extends JPanel implements Observer {

	public static final String DATA_MAP_SWITZERLAND = "map/switzerland_simple.svg";
	public static final String SVG_GROUP_NODES = "tspsolver.nodes";
	public static final String SVG_GROUP_EDGES = "tspsolver.edges";

	private static final long serialVersionUID = -5210001067574218993L;

	private final Grid grid;

	private final SVGDiagram svgDiagram;
	private final SVGIcon svgIcon;

	private final Map<Node, Circle> svgCircles;
	private final Map<Edge, Line> svgLines;

	public GridView(Grid grid) {
		this.grid = grid;
		this.grid.addObserver(this);

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

		this.svgCircles = new HashMap<Node, Circle>();
		this.svgLines = new HashMap<Edge, Line>();
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		this.svgIcon.setPreferredSize(new Dimension(600, 385));
		this.svgIcon.setScaleToFit(true);
		this.svgIcon.paintIcon(this, graphics, 0, 0);

		this.update(this.grid, this.grid);
	}

	@Override
	public void update(Observable observable, Object argument) {
		// Remove the current circles
		Node[] currentNodes = this.svgCircles.keySet().toArray(new Node[this.svgCircles.size()]);
		for (Node node : currentNodes) {
			this.destroyCircle(node);
			this.svgCircles.remove(node);
		}

		// Create the new circles
		for (Node node : this.grid.getNodes()) {
			// Skip the nodes that already exist
			if (this.svgCircles.containsKey(node)) {
				continue;
			}

			Circle circle = this.createCircle(node);
			this.svgCircles.put(node, circle);
		}

		// Update the diagram
		try {
			this.svgDiagram.updateTime(0.0);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
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
		catch (SVGException e) {
			e.printStackTrace();
		}

		return circle;
	}

	private Circle destroyCircle(Node node) {
		Group group = (Group) this.svgDiagram.getElement(GridView.SVG_GROUP_NODES);
		Circle circle = this.svgCircles.get(node);

		try {
			group.removeChild(circle);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}

		return circle;
	}

}
