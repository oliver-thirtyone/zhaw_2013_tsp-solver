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
		StringReader reader = new StringReader(makeDynamicSVG());
		uri = SVGCache.getSVGUniverse().loadSVG(reader, "myImage");
		icon = new SVGIcon();
		icon.setAntiAlias(true);
		icon.setSvgURI(uri);

		setPreferredSize(new Dimension(400, 400));
	}

	@Override
	public void paintComponent(Graphics g) {
		final int width = getWidth();
		final int height = getHeight();

		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);

		icon.paintIcon(this, g, 0, 0);
	}

	private String makeDynamicSVG() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

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
		SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
		Circle circle = (Circle) diagram.getElement("bigCircle");

		String colorStrn = Integer.toHexString(color.getRGB() & 0xffffff);
		try {
			if (!circle.hasAttribute("stroke", AnimationElement.AT_CSS)) {
				circle.addAttribute("stroke", AnimationElement.AT_CSS, "#" + colorStrn);
			} else {
				circle.setAttribute("stroke", AnimationElement.AT_CSS, "#" + colorStrn);
			}
		} catch (SVGElementException e) {
			e.printStackTrace();
		}
	}

	public void setCircleBackground(Color color) {
		SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
		Circle circle = (Circle) diagram.getElement("bigCircle");

		String colorStrn = Integer.toHexString(color.getRGB() & 0xffffff);
		try {
			if (!circle.hasAttribute("fill", AnimationElement.AT_CSS)) {
				circle.addAttribute("fill", AnimationElement.AT_CSS, "#" + colorStrn);
			} else {
				circle.setAttribute("fill", AnimationElement.AT_CSS, "#" + colorStrn);
			}
		} catch (SVGElementException e) {
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
		Tspan tspan = (Tspan) diagram.getElement("userText");

		tspan.setText(text);

		Text textEle = (Text) diagram.getElement("userTextParent");

		try {
			textEle.rebuild();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCircle() {
		SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
		Group group = (Group) diagram.getElement("extraElementGroup");

		Circle circle = new Circle();
		try {
			int cx = (int) (Math.random() * 400);
			int cy = (int) (Math.random() * 400);

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
			extraElements.add(circle);
		} catch (SVGException e) {
			e.printStackTrace();
		}

	}

	public void addText() {
		SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
		Group group = (Group) diagram.getElement("extraElementGroup");

		Text text = new Text();
		try {
			int x = (int) (Math.random() * 300) + 50;
			int y = (int) (Math.random() * 300) + 50;

			text.addAttribute("x", AnimationElement.AT_XML, "" + x);
			text.addAttribute("y", AnimationElement.AT_XML, "" + y);
			text.addAttribute("font-size", AnimationElement.AT_CSS, "12");

			// text.appendText("text");

			Tspan tspan = new Tspan();
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
			extraElements.add(text);
		} catch (SVGException e) {
			e.printStackTrace();
		}

	}

	public void removeElement() {
		int size = extraElements.size();
		if (size == 0)
			return;

		int idx = (int) (Math.random() * size);
		ShapeElement shapeElement = (ShapeElement) extraElements.remove(idx);

		SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
		Group group = (Group) diagram.getElement("extraElementGroup");

		try {
			group.removeChild(shapeElement);
		} catch (SVGException e) {
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
		initComponents();

		panel_display.add(panel, BorderLayout.CENTER);

		pack();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		panel_display = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		bn_back = new javax.swing.JButton();
		bn_front = new javax.swing.JButton();
		bn_add = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		bn_remove = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		text_userText = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		panel_display.setLayout(new java.awt.BorderLayout());

		getContentPane().add(panel_display, java.awt.BorderLayout.CENTER);

		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

		bn_back.setText("Background");
		bn_back.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bn_backActionPerformed(evt);
			}
		});

		jPanel3.add(bn_back);

		bn_front.setText("Foreground");
		bn_front.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bn_frontActionPerformed(evt);
			}
		});

		jPanel3.add(bn_front);

		bn_add.setText("Add Circle");
		bn_add.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bn_addActionPerformed(evt);
			}
		});

		jPanel3.add(bn_add);

		jButton1.setText("Add Text");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jPanel3.add(jButton1);

		bn_remove.setText("Remove");
		bn_remove.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bn_removeActionPerformed(evt);
			}
		});

		jPanel3.add(bn_remove);

		jPanel2.add(jPanel3);

		jLabel1.setText("Text");
		jPanel1.add(jLabel1);

		text_userText.setText("Hello!");
		text_userText.setPreferredSize(new java.awt.Dimension(200, 20));
		text_userText.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				text_userTextActionPerformed(evt);
			}
		});

		jPanel1.add(text_userText);

		jPanel2.add(jPanel1);

		getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

		pack();
	}// </editor-fold>

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		panel.addText();
		repaint();
	}

	private void text_userTextActionPerformed(java.awt.event.ActionEvent evt) {
		panel.setText(text_userText.getText());
		repaint();
	}

	private void bn_removeActionPerformed(java.awt.event.ActionEvent evt) {
		panel.removeElement();
		repaint();
	}

	private void bn_addActionPerformed(java.awt.event.ActionEvent evt) {
		panel.addCircle();
		repaint();
	}

	private void bn_frontActionPerformed(java.awt.event.ActionEvent evt) {
		panel.setCircleForeground(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
		repaint();
	}

	private void bn_backActionPerformed(java.awt.event.ActionEvent evt) {
		panel.setCircleBackground(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
		repaint();
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