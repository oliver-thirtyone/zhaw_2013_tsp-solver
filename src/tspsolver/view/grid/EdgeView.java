package tspsolver.view.grid;

import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Node;

import com.kitfox.svg.Group;
import com.kitfox.svg.Line;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;

public class EdgeView {

	public static final int CIRCLE_RADIUS = 5;
	public static final String NORMAL_EDGE_COLOR = "#000000";
	public static final String CURRENT_PATH_COLOR = "#0000ff";
	public static final String NEW_PATH_COLOR = "#00ff00";
	public static final String OLD_PATH_COLOR = "#ff0000";

	private final Edge edge;

	private final Group svgGroup;
	private final Line svgLine;

	protected EdgeView(Edge edge, GridView gridView) {
		this.edge = edge;

		this.svgGroup = (Group) gridView.getSVGDiagram().getElement(GridView.SVG_GROUP_EDGES);
		this.svgLine = new Line();
	}

	protected void createLine() {
		try {
			Node firstNode = this.edge.getFirstNode();
			Node secondNode = this.edge.getSecondNode();

			this.svgLine.addAttribute("x1", AnimationElement.AT_XML, String.valueOf(firstNode.getX()));
			this.svgLine.addAttribute("y1", AnimationElement.AT_XML, String.valueOf(firstNode.getY()));
			this.svgLine.addAttribute("x2", AnimationElement.AT_XML, String.valueOf(secondNode.getX()));
			this.svgLine.addAttribute("y2", AnimationElement.AT_XML, String.valueOf(secondNode.getY()));

			this.svgGroup.loaderAddChild(null, this.svgLine);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void deleteLine() {
		try {
			this.svgGroup.removeChild(this.svgLine);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void colorLine(String color) {
		try {
			if (this.svgLine.hasAttribute("stroke", AnimationElement.AT_CSS)) {
				this.svgLine.setAttribute("stroke", AnimationElement.AT_CSS, color);
			}
			else {
				this.svgLine.addAttribute("stroke", AnimationElement.AT_CSS, color);
			}
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}
}
