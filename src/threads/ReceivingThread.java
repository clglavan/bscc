/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import bezier.Bcs;
import bezier.Bm;
import bezier.Punct;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;


/**
 *
 * @author eroot
 */
public class ReceivingThread extends Thread {

    Socket ThreadSocket = null;
    int id;
    DataInputStream is = null;
    DataOutputStream os = null;
    ArrayList<DataOutputStream> sockets;
    ArrayList<ObjectOutputStream> object_stream;
    boolean run;
    JTextArea text;
    String newline = "\n";
    ArrayList<String> nicks;

    ArrayList<Bcs> bezier_server;
    ArrayList<Bcs> bezier_clients;

    Bm module = new Bm();

    public ReceivingThread(ArrayList<String> nicks, Socket TempSocket, ArrayList<DataOutputStream> sockets,
            ArrayList<ObjectOutputStream> object_stream, int i, JTextArea text, ArrayList<Bcs> bezier_server, ArrayList<Bcs> bezier_clients) throws SocketException {

        this.ThreadSocket = TempSocket;
        this.sockets = sockets;
        this.id = i;
        this.text = text;
        this.nicks = nicks;
        this.bezier_server = bezier_server;
        this.object_stream = object_stream;
        this.bezier_clients = bezier_clients;

        try {
            is = new DataInputStream(TempSocket.getInputStream());
            os = new DataOutputStream(TempSocket.getOutputStream());
        } catch (IOException ex) {
            text.append("-> Could not initilize streams in receiving thread" + newline);
        }

    }

    public void terminate() {
        id = 0;
        is = null;
        os = null;
        sockets = null;
        text = null;
        ThreadSocket = null;
        run = false;
        nicks = null;
    }

    @Override
    public void run() {

        text.append("-> Receiving Thread started" + newline);
        run = true;
        String temp = "";
        try {
            while (run) {

                temp = is.readUTF();

                if (temp.equals(">keyspace")) {
                    sockets.get(id).writeUTF("/keyspace");
                    object_stream.get(id).writeObject(bezier_server.get(id));
                    object_stream.get(id).writeObject(bezier_clients.get(id));
                    text.append("Keyspace sent to " + id + ": " + nicks.get(id) + newline);
                } else {

                    String message = bezier_server.get(id).decrypt(temp);
                    System.out.println("mmessage at server " + message);

                    if (message.startsWith(">")) {
                        message = message.substring(1);
                        String[] comm = message.split(" ");
                        switch (comm[0]) {
                            case "nick":
                                nicks.set(id, comm[1]);
                                break;
                           

                        }
                        text.append("-> Received: " + message + newline);
                    } else {

                        for (int i = 0; i < bezier_clients.size(); i++) {
                            //if (!sockets.get(i).equals(ThreadSocket)) {
                            String encrypted = bezier_clients.get(i).encrypt(nicks.get(id) + ": " + message);
                            sockets.get(i).writeUTF(encrypted);
                            //    System.out.println("counter for client from server "+ bezier_clients.get(id).counter + " encrypted message is "+ encrypted);
                            // }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            run = false;
//            nicks.remove(id);
            this.interrupt();
            text.append("-> Could not read from client " + ex.toString() + newline);

        }

    }
}
