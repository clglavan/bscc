/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Interfata {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new BorderLayout());
    ArrayList<JTextField> textFieldList = new ArrayList<JTextField>();
    JButton ok = new JButton("Ok");
    JButton refresh = new JButton("Refresh");

    int maxim;

    public Interfata(JPanel panel, int maxim) {
        try {
            this.maxim = maxim;
            drawFrame();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Interfata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Interfata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Interfata.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawPanel();
        this.panel.add(panel);

        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        refresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.repaint();
               // frame.revalidate();
            }
        });
    }

    void drawFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        
        frame.getContentPane().add(new Bcs());
        //  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(maxim, maxim);
        frame.setResizable(true);
        frame.setVisible(true);

        //  frame.setLayout(null);
        // frame.revalidate();
    }

    void drawPanel() {
        frame.add(panel);
        frame.setContentPane(panel);
        panel.add(ok); ok.setVisible(true);
        panel.add(refresh); refresh.setVisible(true);
        panel.setVisible(true);
    }

    public void setTile(String title) {
        frame.setTitle(title);
    }

    public void grabScreenShot() throws Exception {
        BufferedImage image = (BufferedImage) frame.createImage(frame.getContentPane().getSize().width, frame.getContentPane().getSize().height);
        frame.getContentPane().paint(image.getGraphics());
        try {
            ImageIO.write(image, "png", new File("C:\\Users\\Cristi\\Dropbox\\Licenta\\Application\\src\\constants\\Space.png"));
            System.out.println("Image was created");
        } catch (IOException e) {
            System.out.println("Had trouble writing the image.");
            throw e;
        }
    }

    public void piThread(final int cycle, final int milisec) {
        Thread t = new Thread(new Runnable() {
            synchronized public void run() {
                int c = cycle;
                while (c >= 0) {
                    c--;
                    frame.repaint();
                    try {
                        wait(milisec);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Interfata.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();

    }
}
