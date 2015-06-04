/*
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
public class Log {
     private static Point point = new Point();
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton ok = new JButton("Close");
    JTextArea text = new JTextArea();
    public Log() {
        frame.setUndecorated(true);
        frame.setSize(600, 200);
        panel.setSize(600, 200);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        panel.add(text);
        text.setPreferredSize(new Dimension(600, 150));
        text.setLineWrap(true);
        text.setFocusable(false);
        
        panel.add(Box.createVerticalGlue());
        ok.setAlignmentX(Box.CENTER_ALIGNMENT);
        panel.add(ok);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text.setText("");
        
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
    public void setText(String msg){
      text.append(msg+"\n");
    }
}
