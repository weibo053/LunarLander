package me.themgrf.lunarlander;

import me.themgrf.lunarlander.handlers.Controller;
import me.themgrf.lunarlander.handlers.Model;
import me.themgrf.lunarlander.handlers.View;

import java.awt.*;
import javax.swing.*;
import java.net.*;

/**
 * Lunar Lander system
 *
 * @author alun.moon@northumbria.ac.uk
 * @author thomas.griffiths@northumbria.ac.uk
 * @version 1.1.0
 */
public class Lander extends JFrame implements Runnable {

    private final DatagramPanel connection = new DatagramPanel();
    private final Model lander = new Model();
    private final Controller controller;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Lander::new);
    }

    public Lander() {
        super("Lunar Lander");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = getContentPane();
        //content.setLayout(new BorderLayout());
        content.add(connection, BorderLayout.PAGE_START);
        connection.port.setEditable(false);
        connection.addressname.setEditable(false);

        View moon = new View(lander);
        content.add(moon, BorderLayout.CENTER);

        controller = new Controller(lander);
        //this.setContentPane(content);
        // add panel for reset buttons
        JPanel controls = new JPanel(new FlowLayout());
        JButton reset = new JButton("reset");
        reset.addActionListener(b -> lander.reset());
        controls.add(reset);
        content.add(controls, BorderLayout.PAGE_END);

        pack();
        setVisible(true);

        (new javax.swing.Timer((int) (lander.dt * 1000), lander)).start();
        // start thread that handles communications
        new Thread(this).start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            int portno = 65200;
            DatagramSocket socket = new DatagramSocket(portno, addr);
            connection.setAddress((InetSocketAddress) socket.getLocalSocketAddress());
            while (true) {
                // set up socket for reception
                if (socket != null) {
                    // start with fresh datagram packet
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    // extract message and pick apart into lines and key:value pairs
                    String message = new String(packet.getData());
                    String[] lines = message.trim().split("\n");

                    String reply = controller.handle(lines);
                    System.out.println(reply);
                    packet.setData(reply.getBytes());
                    socket.send(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
