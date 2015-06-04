/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Cristi
 */
public class Help {

    private static Point point = new Point();
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton ok = new JButton("Close");
    JTextArea text = new JTextArea();

    public Help() {
        frame.setUndecorated(true);
        frame.setSize(600, 600);
        panel.setSize(600, 600);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        panel.add(text);
        text.setPreferredSize(new Dimension(600, 550));
        text.setLineWrap(true);
        text.setFocusable(false);
        
        panel.add(Box.createVerticalGlue());
        ok.setAlignmentX(Box.CENTER_ALIGNMENT);
        panel.add(ok);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setContentPane(panel);
     //   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text.setText("Hello and welcome, please take a moment and understand how to use the program: \n\n\n"
                + "This is a peer-to-peer chat that is designed to give the user as much control as possible over the background procceses \n\n"
                + "In the following lines the '{example}' model means that example is a field chose by the user \n\n"
                + "To establish a connection you must either be a client, or the server itself \n"
                + "- As the server you must type '/host new {port}' in order to instantiate a ServerSocket object \n"
                + "- As the server you must type '/host listen start' in order to allow incoming client (Socket) connections \n\n"
                + "- As the client you must type '/client connect {localhost} {port}' in order to connect to the desired host\n\n"
                + "These easy steps will ensure a working chat connection.");
        
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = frame.getLocation();
                frame.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
            }
        });
    }
}
