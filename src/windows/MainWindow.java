/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import misc.Autocomplete;

/**
 *
 * @author Cristi
 */
public class MainWindow {

    private static Point point = new Point();
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel status = new JPanel();
    JTextField cmd = new JTextField();
    JButton ok = new JButton("");
    JTextArea text = new JTextArea("", 5, 50);
    JPopupMenu popup;
    // JTextPane text = new JTextPane();
    JScrollPane txt = new JScrollPane(text);
    JLabel srvL = new JLabel("Server");
    JLabel clnL = new JLabel("Client");
    JTextField clnip = new JTextField();
    JTextField srvip = new JTextField();

    String newline = "\n";

    ArrayList<String> keywords;
    private static final String COMMIT_ACTION = "commit";

    public MainWindow() {
        frame.setUndecorated(true);
        frame.setSize(500, 450);
        frame.setBackground(Color.white);
        
      //  status.setBackground(Color.yellow);
    
      //  status.setLayout(new FlowLayout());
      //  status.setSize(500, 50);
        // frame.add(BorderLayout.PAGE_START, status);
        
      //  status.add(clnL);
      //  status.add(clnip);
       // clnip.setEnabled(false);
      //  clnip.setPreferredSize(new Dimension(100, 20));
       // status.add(srvL);
       // status.add(srvip);
      //  srvip.setPreferredSize(new Dimension(100,20));
       // srvip.setEnabled(false);

       
        panel.setSize(500, 350);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        cmd.setPreferredSize(new Dimension(500, 20));
        cmd.setToolTipText("/command or plaintext");
        cmd.setText("/help");
        panel.add(ok);
        ok.setVisible(false);

        panel.add(Box.createVerticalGlue());
        ok.setAlignmentX(Box.CENTER_ALIGNMENT);

        panel.add(txt);
        //panel.add(new JLabel(" "));
        panel.add(cmd);
        txt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        txt.setPreferredSize(new Dimension(300, 300));
        //text.setVisible(false);
        text.setBorder(createEmptyBorder());

        text.setLineWrap(true);

        text.setFocusable(false);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(false);

        JRootPane rootPane = frame.getRootPane();
        rootPane.setDefaultButton(ok);

        cmd.requestFocus();
        cmd.setCaretPosition(cmd.getText().length());

        text.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });
        text.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = frame.getLocation();
                frame.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
            }
        });

        // autocomplete cmd
        // Without this, cursor always leaves text field
        cmd.setFocusTraversalKeysEnabled(false);

// Our words to complete
        keywords = new ArrayList<String>(5);
        keywords.add("new");
        keywords.add("clear");
        keywords.add("help");
        keywords.add("exit");
        keywords.add("client");
        keywords.add("connect");
        keywords.add("localhost");
        keywords.add("1040");
        keywords.add("server");
        keywords.add("new");
        keywords.add("listening");
        keywords.add("start");
        keywords.add("stop");
        keywords.add("close");

        Autocomplete autoComplete = new Autocomplete(cmd, keywords);
        cmd.getDocument().addDocumentListener(autoComplete);

// Maps the tab key to the commit action, which finishes the autocomplete
// when given a suggestion
        cmd.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        cmd.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());

        // end autocomplete
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTextField getCmd() {
        return cmd;
    }

    public JTextArea getText() {
        return text;
    }

    public JPopupMenu getPopup() {
        return popup;
    }

    public JScrollPane getTxt() {
        return txt;
    }

    public JButton getOk() {
        return ok;
    }

    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

}
