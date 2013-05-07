package online_examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.ShapeElement;
import com.kitfox.svg.Text;
import com.kitfox.svg.Tspan;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;

class DynamicIconPanel extends JPanel {
	public static final long serialVersionUID = 0;

	final SVGIcon icon;
	URI uri;

	LinkedList extraElements = new LinkedList();

	public DynamicIconPanel() {
		final StringReader reader = new StringReader(this.makeDynamicSVG());
		this.uri = SVGCache.getSVGUniverse().loadSVG(reader, "myImage");
		this.icon = new SVGIcon();
		this.icon.setAntiAlias(true);
		this.icon.setSvgURI(this.uri);

		this.setPreferredSize(new Dimension(400, 400));
	}

	@Override
	public void paintComponent(Graphics g) {
		final int width = this.getWidth();
		final int height = this.getHeight();

		g.setColor(this.getBackground());
		g.fillRect(0, 0, width, height);

		this.icon.paintIcon(this, g, 0, 0);
	}

	private String makeDynamicSVG() {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);

		pw.println("<svg width=\"400\" height=\"400\" style=\"fill:none;stroke-width:16\">");
		pw.println("    <circle id=\"bigCircle\" cx=\"200\" cy=\"200\" r=\"200\" style=\"stroke:blue\"/>");
		pw.println("    <g id=\"extraElementGroup\" style=\"stroke-width:4;fill:green\"/>");
		pw.println("    <text id=\"userTextParent\" x=\"0\" y=\"40\" style=\"font-size:40;stroke:none;fill:red\">");
		pw.println("        <tspan id=\"userText\">Hello!</tspan>");
		pw.println("    </text>");
		pw.println("</svg>");

		pw.close();
		return sw.toString();
	}

	public void setCircleForeground(Color color) {
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Circle circle = (Circle) diagram.getElement("bigCircle");

		final String colorStrn = Integer.toHexString(color.getRGB() & 0xffffff);
		try {
			if (!circle.hasAttribute("stroke", AnimationElement.AT_CSS)) {
				circle.addAttribute("stroke", AnimationElement.AT_CSS, "#" + colorStrn);
			}
			else {
				circle.setAttribute("stroke", AnimationElement.AT_CSS, "#" + colorStrn);
			}
		}
		catch (final SVGElementException e) {
			e.printStackTrace();
		}
	}

	public void setCircleBackground(Color color) {
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Circle circle = (Circle) diagram.getElement("bigCircle");

		final String colorStrn = Integer.toHexString(color.getRGB() & 0xffffff);
		try {
			if (!circle.hasAttribute("fill", AnimationElement.AT_CSS)) {
				circle.addAttribute("fill", AnimationElement.AT_CSS, "#" + colorStrn);
			}
			else {
				circle.setAttribute("fill", AnimationElement.AT_CSS, "#" + colorStrn);
			}
		}
		catch (final SVGElementException e) {
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Tspan tspan = (Tspan) diagram.getElement("userText");

		tspan.setText(text);

		final Text textEle = (Text) diagram.getElement("userTextParent");

		try {
			textEle.rebuild();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void addCircle() {
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Group group = (Group) diagram.getElement("extraElementGroup");

		final Circle circle = new Circle();
		try {
			final int cx = (int) (Math.random() * 400);
			final int cy = (int) (Math.random() * 400);

			circle.addAttribute("cx", AnimationElement.AT_XML, "" + cx);
			circle.addAttribute("cy", AnimationElement.AT_XML, "" + cy);
			circle.addAttribute("r", AnimationElement.AT_XML, "10");

			group.loaderAddChild(null, circle);

			// Update animation state or group and it's decendants so that it reflects new animation values.
			// We could also call diagram.update(0.0) or SVGCache.getSVGUniverse().update(). Note that calling
			// circle.update(0.0) won't display anything since even though it will update the circle's state,
			// it won't update the parent group's state.
			group.updateTime(0.0);

			// Keep track of circles so we can remove them later
			this.extraElements.add(circle);
		}
		catch (final SVGException e) {
			e.printStackTrace();
		}

	}

	public void addText() {
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Group group = (Group) diagram.getElement("extraElementGroup");

		final Text text = new Text();
		try {
			final int x = (int) (Math.random() * 300) + 50;
			final int y = (int) (Math.random() * 300) + 50;

			text.addAttribute("x", AnimationElement.AT_XML, "" + x);
			text.addAttribute("y", AnimationElement.AT_XML, "" + y);
			text.addAttribute("font-size", AnimationElement.AT_CSS, "12");

			// text.appendText("text");

			final Tspan tspan = new Tspan();
			tspan.setText("text");
			text.appendTspan(tspan);

			group.loaderAddChild(null, text);
			text.build();

			// Update animation state or group and it's decendants so that it reflects new animation values.
			// We could also call diagram.update(0.0) or SVGCache.getSVGUniverse().update(). Note that calling
			// circle.update(0.0) won't display anything since even though it will update the circle's state,
			// it won't update the parent group's state.
			group.updateTime(0.0);

			// Keep track of circles so we can remove them later
			this.extraElements.add(text);
		}
		catch (final SVGException e) {
			e.printStackTrace();
		}

	}

	public void removeElement() {
		final int size = this.extraElements.size();
		if (size == 0) {
			return;
		}

		final int idx = (int) (Math.random() * size);
		final ShapeElement shapeElement = (ShapeElement) this.extraElements.remove(idx);

		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Group group = (Group) diagram.getElement("extraElementGroup");

		try {
			group.removeChild(shapeElement);
		}
		catch (final SVGException e) {
			e.printStackTrace();
		}

	}
}

/**
 * 
 * @author kitfox
 */
public class SVGIconDemoFrame extends javax.swing.JFrame {
	public static final long serialVersionUID = 0;

	DynamicIconPanel panel = new DynamicIconPanel();

	/** Creates new form SVGIconDemo */
	public SVGIconDemoFrame() {
		this.initComponents();

		this.panel_display.add(this.panel, BorderLayout.CENTER);

		this.pack();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		this.panel_display = new javax.swing.JPanel();
		this.jPanel2 = new javax.swing.JPanel();
		this.jPanel3 = new javax.swing.JPanel();
		this.bn_back = new javax.swing.JButton();
		this.bn_front = new javax.swing.JButton();
		this.bn_add = new javax.swing.JButton();
		this.jButton1 = new javax.swing.JButton();
		this.bn_remove = new javax.swing.JButton();
		this.jPanel1 = new javax.swing.JPanel();
		this.jLabel1 = new javax.swing.JLabel();
		this.text_userText = new javax.swing.JTextField();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.panel_display.setLayout(new java.awt.BorderLayout());

		this.getContentPane().add(this.panel_display, java.awt.BorderLayout.CENTER);

		this.jPanel2.setLayout(new javax.swing.BoxLayout(this.jPanel2, javax.swing.BoxLayout.Y_AXIS));

		this.bn_back.setText("Background");
		this.bn_back.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SVGIconDemoFrame.this.bn_backActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.bn_back);

		this.bn_front.setText("Foreground");
		this.bn_front.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SVGIconDemoFrame.this.bn_frontActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.bn_front);

		this.bn_add.setText("Add Circle");
		this.bn_add.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SVGIconDemoFrame.this.bn_addActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.bn_add);

		this.jButton1.setText("Add Text");
		this.jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SVGIconDemoFrame.this.jButton1ActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.jButton1);

		this.bn_remove.setText("Remove");
		this.bn_remove.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SVGIconDemoFrame.this.bn_removeActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.bn_remove);

		this.jPanel2.add(this.jPanel3);

		this.jLabel1.setText("Text");
		this.jPanel1.add(this.jLabel1);

		this.text_userText.setText("Hello!");
		this.text_userText.setPreferredSize(new java.awt.Dimension(200, 20));
		this.text_userText.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SVGIconDemoFrame.this.text_userTextActionPerformed(evt);
			}
		});

		this.jPanel1.add(this.text_userText);

		this.jPanel2.add(this.jPanel1);

		this.getContentPane().add(this.jPanel2, java.awt.BorderLayout.SOUTH);

		this.pack();
	}// </editor-fold>

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.addText();
		this.repaint();
	}

	private void text_userTextActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.setText(this.text_userText.getText());
		this.repaint();
	}

	private void bn_removeActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.removeElement();
		this.repaint();
	}

	private void bn_addActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.addCircle();
		this.repaint();
	}

	private void bn_frontActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.setCircleForeground(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
		this.repaint();
	}

	private void bn_backActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.setCircleBackground(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
		this.repaint();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SVGIconDemoFrame().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify
	private javax.swing.JButton bn_add;
	private javax.swing.JButton bn_back;
	private javax.swing.JButton bn_front;
	private javax.swing.JButton bn_remove;
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel panel_display;
	private javax.swing.JTextField text_userText;
	// End of variables declaration

}