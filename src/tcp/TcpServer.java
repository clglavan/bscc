/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import bezier.Bcs;
import bezier.Punct;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import threads.ConnectingThread;
import threads.ReceivingThread;
import windows.MainFrame;
/**
 *
 * @author eroot
 */
public class TcpServer extends Thread {

    ServerSocket Server = null;
    DataOutputStream tos = null;
    JTextArea text;
    MainFrame frame;
    String newline = "\n";
    private boolean running = true;
    ArrayList<String> nicks = new ArrayList<>();
    ArrayList<DataOutputStream> sockets = new ArrayList<>();
    ArrayList<ObjectOutputStream> object_stream = new ArrayList<>();
    ArrayList<ReceivingThread> Rthreads = new ArrayList<>();
    ArrayList<ConnectingThread> Cthreads = new ArrayList<>();
    ArrayList<Bcs> bezier_clients = new ArrayList<>();
    int port;

    ArrayList<Bcs> bezier_server = new ArrayList<>();

    

    public TcpServer(int port_tcp, JTextArea text, MainFrame frame) throws IOException {
        Server = new ServerSocket(port_tcp);
        this.port = port_tcp;
        this.text = text;
        this.frame = frame;
        this.text.append("-> ServerSocket opened(" + port_tcp + ")" + newline);

       
        

    }

    public void terminate() {
        tos = null;
        text = null;
        running = false;
        Server = null;
        sockets.clear();
        sockets = null;
        nicks.clear();
        nicks = null;
        for (ReceivingThread Rthread : Rthreads) {
            Rthread.terminate();
        }
        for (ConnectingThread Cthread : Cthreads) {
            Cthread.terminate();
        }
        port = 0;
    }

    public void start_listen() {
        running = true;
    }

    public void stop_listen() {
        running = false;
    }

    @Override
    public void run() {

        int i = 0;

        while (true) {

            try {
                Socket TempSocket = Server.accept();
                if (running) {
                    nicks.add(i, "Guest-" + i);
                    ConnectingThread con = new ConnectingThread(nicks, Rthreads, sockets, object_stream, TempSocket, text, frame, i, bezier_server, bezier_clients);
                    Cthreads.add(con);
                    con.start();

                    i++;
                } else {
                    tos = new DataOutputStream(TempSocket.getOutputStream());

                    tos.writeUTF("/deny");
                    tos.close();
                }
            } catch (IOException e) {
                text.append("-> Error receving client: " + e.toString() + newline);
            }
        }

    }

    public InetAddress getAdress() {
        return Server.getInetAddress();
    }

}
