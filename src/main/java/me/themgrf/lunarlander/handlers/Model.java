package me.themgrf.lunarlander.handlers;

import java.awt.event.*;
import javax.swing.*;

/**
 * Model class for handling actions
 */
public class Model implements ActionListener {

    // Use 1m grid, 1pixel, 1m
    public final int width = 1000;
    public final int[] ground;

    // TODO: Turns these into private and use getters instead
    public final double dt = 0.05;
    public final double g = 1.62;
    public double x = 100;
    public double y = 50;
    public double O = 0;
    public double xdot = 10;
    public double ydot;
    public double v;
    public double Odot;
    public double fuel = 100;
    public double altitude;
    public double throttle;
    public double thrust;
    public double roll;
    public boolean isFlying = true;
    public boolean isCrashed = false;

    public JPanel view;

    /**
     * Constructor for creation a model with terrain
     */
    public Model() {
        ground = new int[width];
        double[] ter = Terrain.wrapped(Terrain.ground(ground.length, 32));
        for (int x = 0; x < ground.length; x++) {
            ground[x] = 600 - (int) (ter[x] * 20);
        }
    }

    /**
     * Event fired when an action is performed
     * @param e The event being fired
     */
    public void actionPerformed(ActionEvent e) {
        // Single pass through iterration
        thrust = throttle * 3 / 100;
        fuel -= throttle * 0.01 * dt;
        if (fuel < 0) {
            fuel = 0;
            thrust = 0;
        }

        double xdotdot = Math.sin(Math.toRadians(O)) * thrust;
        double ydotdot = g - Math.cos(Math.toRadians(O)) * thrust;

        if (isFlying) O += roll * 60 * dt;

        x += xdot * dt + 0.5 * xdotdot * dt * dt;
        y += ydot * dt + 0.5 * ydotdot * dt * dt;

        xdot += xdotdot * dt;
        ydot += ydotdot * dt;
        v = Math.sqrt(xdot * xdot + ydot * ydot);

        // Keep x wrapped
        if (x < 0) x += width;
        if (x >= width) x -= width;

        // Altitude above ground
        altitude = ground[(int) x] - y;
        isFlying = altitude > 0;
        // On the ground is it crashed?
        if (!isFlying) {
            /*
            if already crashed -- do nothing
            if not crashed, if vel is over 10, that's a crash!
            if not upright that's also a crash.
             */
            if (!isCrashed) isCrashed = (v > 10 || Math.abs(O) > 15);
        }

        if (!isFlying) {
            y = ground[(int) x];
            ydot = xdot = 0;
            O = 0;
        }
        view.repaint();
    }

    /**
     * Reset the model
     */
    public void reset() {
        x = 100;
        y = 50;
        O = 0;
        xdot = 10;
        ydot = 10;
        fuel = 100;
        Odot = 0;
        throttle = 0;
        isFlying = true;
        isCrashed = false;
    }

}
