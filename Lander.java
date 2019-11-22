import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Lander extends JFrame implements Runnable {
    public static final long serialVersionUID = 2L;

    static Properties defaults = new Properties();;

    public static void main ( String[] args ) throws UnknownHostException, IOException, ClassNotFoundException {

        defaults.load(Class.forName("Lander").getResourceAsStream("Defaults.properties" ));

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                new Lander();
            }
        });
    }

    DatagramPanel connection = new DatagramPanel();
    lander.Model lander = new lander.Model();
    lander.Controller controller;

    public Lander() {
        super("Lunar Lander");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = this.getContentPane();
        //content.setLayout(new BorderLayout());
        content.add(connection, BorderLayout.PAGE_START);
        connection.port.setEditable(false);
        connection.addressname.setEditable(false);

        lander.View moon = new lander.View(lander);
        content.add(moon, BorderLayout.CENTER);

        controller = new lander.Controller(lander);
        //this.setContentPane(content);
        /* add panel for reset buttons */
        JPanel controls = new JPanel(new FlowLayout());
        JButton reset = new JButton("reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent b) {
                lander.reset();
            }
        });
        controls.add(reset);
        content.add(controls, BorderLayout.PAGE_END);

        this.pack();
        this.setVisible(true);

        (new javax.swing.Timer((int)(lander.dt*1000),lander)).start();
        /* start thread that handles communications */
        (new Thread(this)).start();
    }

    public void run() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            int portno = 65200;
            DatagramSocket socket = new DatagramSocket(portno, addr);
            connection.setAddress((InetSocketAddress)socket.getLocalSocketAddress());
            while(true) {
                /* set up socket for reception */
                if(socket!=null) {
                    /* start with fresh datagram packet */
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive( packet );
                    /* extract message and pick apart into
                       lines and key:value pairs
                    */
                    String message = new String(packet.getData());
                    String[] lines = message.trim().split("\n");

                    String reply = controller.handle(lines);
					System.out.println(reply);
                    packet.setData(reply.getBytes());
                    socket.send(packet);
                }
            }
        }
        catch(Exception e) {
            System.err.println(e);
            System.err.println("in Lander.run()");
            System.exit(-1);
        }
    }
}
