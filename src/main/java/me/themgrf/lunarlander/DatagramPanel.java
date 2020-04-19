package me.themgrf.lunarlander;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;

import java.net.*;
import java.io.*;

/**
 * Place holder for ip-address and port number for internet addressing
 * Duplicate class from LanderDash
 *
 * @see "me.themgrf.landerdash.DatagramPanel"
 */
public class DatagramPanel extends JPanel {

    public final JTextField addressname, port;

    /**
     * Constructor for creation of the datagram panel
     */
    public DatagramPanel() {
        /* create a JPanel populated with border and text fields */
        super(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBorder(BorderFactory.createTitledBorder("Socket Address"));
        add(new JLabel("IP:"));
        addressname = new JTextField(10);
        add(addressname);
        add(new JLabel("port:"));
        port = new JTextField(5);
        add(port);
    }

    /**
     * Set the current hostname address
     * @param address The current hostname address
     */
    void setAddress(InetSocketAddress address) {
        addressname.setText(address.getHostString());
        port.setText(Integer.toString(address.getPort()));
    }

    /*void setIP(String ip) {
        addressname.setText(ip);
    }

    void setIP(InetAddress adr) {
        addressname.setText(adr.getHostAddress());
    }

    void setPort(int p) {
        port.setText(Integer.toString(p));
    }

    void setPort(String p) {
        port.setText(p);
    }*/
}
