package tspsolver.view.grid;

import tspsolver.model.grid.Node;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;

public class NodeView {

	public static final int CIRCLE_RADIUS = 5;
	public static final String NORMAL_NODE_COLOR = "#000000";
	public static final String STARTING_NODE_COLOR = "#0000ff";

	private final Node node;

	private final Group svgGroup;
	private final Circle svgCircle;

	protected NodeView(Node node, GridView gridView) {
		this.node = node;

		this.svgGroup = (Group) gridView.getSVGDiagram().getElement(GridView.SVG_GROUP_NODES);
		this.svgCircle = new Circle();
	}

	protected void createCircle() {
		try {
			this.svgCircle.addAttribute("cx", AnimationElement.AT_XML, String.valueOf(this.node.getX()));
			this.svgCircle.addAttribute("cy", AnimationElement.AT_XML, String.valueOf(this.node.getY()));
			this.svgCircle.addAttribute("r", AnimationElement.AT_XML, String.valueOf(NodeView.CIRCLE_RADIUS));

			this.svgGroup.loaderAddChild(null, this.svgCircle);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void deleteCircle() {
		try {
			this.svgGroup.removeChild(this.svgCircle);
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}

	protected void updateCircle(String color) {
		try {
			if (this.svgCircle.hasAttribute("fill", AnimationElement.AT_CSS)) {
				this.svgCircle.setAttribute("fill", AnimationElement.AT_CSS, color);
			}
			else {
				this.svgCircle.addAttribute("fill", AnimationElement.AT_CSS, color);
			}
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}
}
