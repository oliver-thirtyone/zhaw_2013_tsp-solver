package tspsolver.view.grid;

import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.Node;

import com.kitfox.svg.Group;
import com.kitfox.svg.Line;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;

public class EdgeView {

	public static final String LINE_STROKE_EDGE = "#000000";
	public static final String LINE_STROKE_PATH = "#0000ff";
	public static final String LINE_STROKE_PATH_NEW = "#00ff00";
	public static final String LINE_STROKE_PATH_OLD = "#ff0000";

	public static final String LINE_STROKE_WIDTH_EDGE = "1";
	public static final String LINE_STROKE_WIDTH_PATH = "3";

	public static final String LINE_STROKE_DASHARRAY_EDGE = "2, 8";
	public static final String LINE_STROKE_DASHARRAY_PATH = "2, 2";

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
			this.svgLine.addAttribute("stroke-dasharray", AnimationElement.AT_CSS, "2, 8");

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

	protected void updateLine(String lineStroke, String lineWidth, String lineDashArray) {
		try {
			// Set the stroke
			if (this.svgLine.hasAttribute("stroke", AnimationElement.AT_CSS)) {
				this.svgLine.setAttribute("stroke", AnimationElement.AT_CSS, lineStroke);
			}
			else {
				this.svgLine.addAttribute("stroke", AnimationElement.AT_CSS, lineStroke);
			}

			// Set the stroke width
			if (this.svgLine.hasAttribute("stroke-width", AnimationElement.AT_CSS)) {
				this.svgLine.setAttribute("stroke-width", AnimationElement.AT_CSS, lineWidth);
			}
			else {
				this.svgLine.addAttribute("stroke-width", AnimationElement.AT_CSS, lineWidth);
			}

			// Set the stroke dasharray
			if (!lineDashArray.isEmpty()) {
				if (this.svgLine.hasAttribute("stroke-dasharray", AnimationElement.AT_CSS)) {
					this.svgLine.setAttribute("stroke-dasharray", AnimationElement.AT_CSS, lineDashArray);
				}
				else {
					this.svgLine.addAttribute("stroke-dasharray", AnimationElement.AT_CSS, lineDashArray);
				}
			}
			else {
				if (this.svgLine.hasAttribute("stroke-dasharray", AnimationElement.AT_CSS)) {
					this.svgLine.removeAttribute("stroke-dasharray", AnimationElement.AT_CSS);
				}
			}
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}
}
