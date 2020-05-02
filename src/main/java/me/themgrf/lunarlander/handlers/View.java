package me.themgrf.lunarlander.handlers;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * View class for creating the GUI
 */
public class View extends JPanel {

    private final Model model;
    private final Path2D outline = landershape();

    /**
     * Constructor for creating the GUI view
     * @param model The model to use in the GUI
     */
    public View(Model model) {
        this.model = model;
        setPreferredSize(new Dimension(1000, 700));
        model.view = this;
    }

    /**
     * Paint different graphical components
     * @param g The graphical component to paint
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int x = 0; x < model.ground.length; x++) {
            g.setColor(Color.darkGray);
            g.drawLine(x, 700, x, model.ground[x]);
        }

        //drawAltitude(g);
        g.translate((int) model.x, (int) model.y);
        drawLander(g);
        //drawVelocity(g);
        //drawFuel(g);
    }

    /**
     * Draw the lunar lander to the GUI
     * @param g The graphical lander component
     */
    protected void drawLander(Graphics g) {
        final int[] x = {-10, -8, 8, 10};
        final int[] y = {0, -8, -8, 0};
        Graphics2D g2d = (Graphics2D) g;
        //g2d.drawString(String.format("(%5.1f,%5.1f)",model.x,model.y), 20, -15);
        //g2d.drawString(String.format("%3.1f\u00B0(%.2f)",model.O,model.roll), 20, -25);

        AffineTransform at = g2d.getTransform();
        g2d.rotate(Math.toRadians(model.O));
        drawThrust(g);
        g2d.setColor(Color.blue);
        g2d.draw(outline);
        g2d.setTransform(at);

        if (!model.isFlying) {
            if (model.isCrashed) {
                g2d.setColor(Color.red);
                g2d.drawString("Crashed \u26A0", -10, -35);
            } else {
                g2d.drawString("down \u263A", -10, -35);
            }
        }
    }

    /**
     * Draw the velocity of the lander
     * @param g The velocity graphical component
     */
    protected void drawVelocity(Graphics g) {
        g.setColor(Color.green.darker());
        g.drawLine(0, 0, (int) model.xdot, (int) model.ydot);
        g.drawString(String.format("%5.1f m/s", model.v),
                (int) model.xdot, (int) model.ydot);
    }

    /**
     * Draw the thrust of the lander
     * @param g The thrust graphical component
     */
    protected void drawThrust(Graphics g) {
        int[] x = {-3, 0, +3};
        int[] y = {-2, -2 + (int) (model.thrust * 10), -2};
        g.setColor(Color.red);
        g.drawPolyline(x, y, 3);
    }

    /**
     * Draw the fuel of the lander
     * @param g The fuel graphical component
     */
    protected void drawFuel(Graphics g) {
        g.setColor(Color.green);
        g.drawLine(-21, 0, -21, -(int) model.fuel);
    }

    /**
     * Draw the altitude of the lander
     * @param g The altitude graphical component
     */
    protected void drawAltitude(Graphics g) {
        g.setColor(Color.green.darker());
        g.drawLine((int) model.x, (int) model.y, (int) model.x, model.ground[(int) model.x]);
    }

    /**
     * Draw the lander shape
     * @return A 2D geometric path shape
     */
    private Path2D landershape() {
        GeneralPath s = new GeneralPath();
        s.moveTo(-20, -1);
        s.lineTo(-18, 0);
        s.lineTo(-16, -1);
        s.moveTo(-18, 0);
        s.lineTo(-13, -12);
        s.lineTo(-10, -13);
        s.lineTo(10, -13);
        s.lineTo(12, -11);
        s.lineTo(18, 0);
        s.moveTo(20, -1);
        s.lineTo(18, 0);
        s.lineTo(16, -1);
        s.moveTo(-16, -4);
        s.lineTo(-10, -5);
        s.lineTo(-13, -12);
        s.moveTo(16, -4);
        s.lineTo(10, -5);
        s.lineTo(12, -11);
        s.append(new Rectangle(-10, -13, 20, 7), false);
        s.append(new RoundRectangle2D.Float(-6, -28, 12, 12, 8, 8), false);
        s.moveTo(-2, -5);
        s.lineTo(-3, -2);
        s.lineTo(3, -2);
        s.lineTo(2, -5);
        return s;
    }
}
