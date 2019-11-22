package lander;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;

public class Model implements ActionListener {
    /* use 1m grid, 1pixel, 1m */
    final int width = 1000;
    int[] ground;

    public final double dt = 0.05;
    final  double g = 1.62;
    double x=100;
    double y=50;
    double O=0;
    double xdot = 10;
    double ydot;
    double v;
    double Odot;
    double fuel=100;
    double altitude;
    double throttle;
    double thrust ;
    double roll ;
    boolean isflying = true;
    boolean iscrashed = false;

    JPanel view;
    public void actionPerformed(ActionEvent t) {
        /* single pass through iterration  */
        thrust = throttle*3/100;
        fuel -= throttle*0.01*dt;
        if(fuel<0) {
            fuel = 0;
            thrust = 0;
        }

        double xdotdot = Math.sin(Math.toRadians(O))*thrust;
        double ydotdot = g-Math.cos(Math.toRadians(O))*thrust;

        if(isflying) O += roll*60*dt;

        x += xdot*dt + 0.5*xdotdot*dt*dt;
        y += ydot*dt + 0.5*ydotdot*dt*dt;

        xdot += xdotdot*dt;
        ydot += ydotdot*dt;
        v = Math.sqrt(xdot*xdot + ydot*ydot);

        /* Keep x wrapped */
        if( x<0 ) x += width;
        if( x>=width ) x -= width;

        /* altitude above ground */
        altitude = ground[(int)x]-y;
        isflying = altitude>0;
        /* on the ground is it crashed? */
        if( !isflying ) {
            /* if already crashed -- do nothing
            if not crashed, if vel is over 10, that's a crash!
            if not upright that's also a crash.
            */
            if( !iscrashed ) iscrashed = (v>10 || Math.abs(O)>15);
        }

        if(!isflying) {
            y = ground[(int)x];
            ydot = xdot = 0;
            O = 0;
        }
        view.repaint();
    }


    public Model() {
        ground = new int[width];
        double[] ter = Terrain.wrapped( Terrain.ground(ground.length,32) );
        for(int x=0 ; x<ground.length ; x++ ) {
            ground[x] =  600-(int)(ter[x]*20);
        }
    }

    public void reset() {
        x=100;
        y=50;
        O=0;
        xdot = 10;
        ydot = 10;
        fuel=100;
        Odot=0;
        throttle=0;
        isflying = true;
        iscrashed = false;
    }

}
