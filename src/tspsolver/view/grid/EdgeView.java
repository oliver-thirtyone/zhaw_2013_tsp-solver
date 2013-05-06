package tspsolver.view.grid;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;

import com.kitfox.svg.Group;
import com.kitfox.svg.Line;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;

public class EdgeView {

	public static final int CIRCLE_RADIUS = 5;
	public static final String EDGE_COLOR = "#000000";
	public static final String PATH_COLOR = "#0000ff";
	public static final String NEW_PATH_COLOR = "#00ff00";
	public static final String OLD_PATH_COLOR = "#ff0000";

	private final Edge edge;

	private final Group svgGroup;
	private final Line svgLine;

	protected EdgeView(Edge edge, SVGDiagram svgDiagram) {
		this.edge = edge;

		this.svgGroup = (Group) svgDiagram.getElement(GridView.SVG_GROUP_EDGES);
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
			this.svgLine.addAttribute("stroke-dasharray", AnimationElement.AT_CSS, "2, 4");

			this.svgGroup.loaderAddChild(null, this.svgLine);
		} catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void deleteLine() {
		try {
			this.svgGroup.removeChild(this.svgLine);
		} catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void updateLine(String color, boolean dashedLine) {
		try {
			// Set the color
			if (this.svgLine.hasAttribute("stroke", AnimationElement.AT_CSS)) {
				this.svgLine.setAttribute("stroke", AnimationElement.AT_CSS, color);
			} else {
				this.svgLine.addAttribute("stroke", AnimationElement.AT_CSS, color);
			}

			// Set dashed line or not
			if (dashedLine) {
				if (this.svgLine.hasAttribute("stroke-dasharray", AnimationElement.AT_CSS)) {
					this.svgLine.setAttribute("stroke-dasharray", AnimationElement.AT_CSS, "2, 4");
				} else {
					this.svgLine.addAttribute("stroke-dasharray", AnimationElement.AT_CSS, "2, 4");
				}
			} else {
				if (this.svgLine.hasAttribute("stroke-dasharray", AnimationElement.AT_CSS)) {
					this.svgLine.removeAttribute("stroke-dasharray", AnimationElement.AT_CSS);
				}
			}
		} catch (SVGException exception) {
			exception.printStackTrace();
		}
	}
}
