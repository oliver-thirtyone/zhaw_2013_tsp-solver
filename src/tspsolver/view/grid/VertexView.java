package tspsolver.view.grid;

import tspsolver.model.scenario.grid.Vertex;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;

public class VertexView {

	public static final String CIRCLE_RADIUS = "8";

	public static final String CIRCLE_FILL_NODE = "#000000";
	public static final String CIRCLE_FILL_STARTINGNODE = "#0000ff";

	private final Vertex vertex;

	private final Group svgGroup;
	private final Circle svgCircle;

	protected VertexView(Vertex vertex, SVGDiagram svgDiagram) {
		this.vertex = vertex;

		this.svgGroup = (Group) svgDiagram.getElement(GridView.SVG_GROUP_NODES);
		this.svgCircle = new Circle();
	}

	protected void createCircle() {
		try {
			this.svgCircle.addAttribute("cx", AnimationElement.AT_XML, String.valueOf(this.vertex.getX()));
			this.svgCircle.addAttribute("cy", AnimationElement.AT_XML, String.valueOf(this.vertex.getY()));
			this.svgCircle.addAttribute("r", AnimationElement.AT_XML, VertexView.CIRCLE_RADIUS);

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

	protected void updateCircle(String circleFill) {
		try {
			if (this.svgCircle.hasAttribute("fill", AnimationElement.AT_CSS)) {
				this.svgCircle.setAttribute("fill", AnimationElement.AT_CSS, circleFill);
			}
			else {
				this.svgCircle.addAttribute("fill", AnimationElement.AT_CSS, circleFill);
			}
		}
		catch (SVGException exception) {
			exception.printStackTrace();
		}
	}
}
