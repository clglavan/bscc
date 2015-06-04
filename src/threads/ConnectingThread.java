/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import bezier.Bcs;
import bezier.Bm;
import bezier.Punct;
import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import windows.MainFrame;

/**
 *
 * @author Cristi
 */
public class ConnectingThread extends Thread {

    int i;
    ArrayList<DataOutputStream> sockets = null;
    ArrayList<ObjectOutputStream> object_stream = null;
    Socket TempSocket = null;
    ArrayList<ReceivingThread> Rthreads;
    String newline = "\n";
    JTextArea text;
    MainFrame frame;
    DataOutputStream tos = null;
    ArrayList<String> nicks;

    ArrayList<Bcs> bezier_server = new ArrayList<>();
    Bm module = new Bm();
    ArrayList<Bcs> bezier_clients;

    public ConnectingThread(ArrayList<String> nicks, ArrayList<ReceivingThread> Rthreads, ArrayList<DataOutputStream> sockets,
            ArrayList<ObjectOutputStream> object_stream, Socket TempSocket, JTextArea text, MainFrame frame, int i, ArrayList<Bcs> bezier_server, ArrayList<Bcs> bezier_clients) {
        this.i = i;
        this.Rthreads = Rthreads;
        this.sockets = sockets;
        this.TempSocket = TempSocket;
        this.text = text;
        this.frame = frame;
        this.nicks = nicks;
        this.bezier_server = bezier_server;
        this.object_stream = object_stream;
        this.bezier_clients = bezier_clients;
        System.out.println("Connect o data");
    }

    public void terminate() {
        i = 0;
        sockets = null;
        TempSocket = null;
        text = null;
        tos = null;
        for (ReceivingThread Rthread : Rthreads) {
            Rthread.terminate();
        }
        Rthreads = null;
        nicks = null;
    }
    int reply = -1;
    int response = 0;

    @Override
    synchronized public void run() {

//        try {
//            SwingUtilities.invokeAndWait(new Runnable() {
//                @Override
//                synchronized public void run() {
//                    System.out.println("something");
//
//                }
//            });
//        } catch (Exception e) {
//        }
        
       // reply = frame.showConfirm("Incoming connection from " + TempSocket.getInetAddress(), "Incoming connection");
        System.out.println("gata");
        if (true) {

            try {
                sockets.add(new DataOutputStream(TempSocket.getOutputStream()));
                object_stream.add(new ObjectOutputStream(TempSocket.getOutputStream()));
                System.out.println("asdsfasdf");
                text.append("-> Client connected: " + TempSocket.toString() + newline);

                bezier_clients.add(i, new Bcs());
                bezier_clients.get(i).generate_control_points(600, 100, 0.001);

                bezier_server.add(i, new Bcs());
                bezier_server.get(i).generate_control_points(600, 100, 0.001);

                ReceivingThread receive = new ReceivingThread(nicks, TempSocket, sockets, object_stream, i, text, bezier_server, bezier_clients);
                receive.start();
                Rthreads.add(receive);

                sockets.get(i).writeUTF("/force_keyspace");
                System.out.println("trimis");
            } catch (IOException ex) {
                text.append("Error receiving connection" + newline);
            }
        } else {
            try {
                tos = new DataOutputStream(TempSocket.getOutputStream());

                tos.writeUTF("/deny");
                tos.close();
                text.append("Denied connection to " + TempSocket.getInetAddress() + newline);
            } catch (IOException ex) {
                text.append("Could not send denial back to client" + newline);
            }
        }

        System.out.println("done");

    }

}
