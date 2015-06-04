/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import bezier.Bcs;
import bezier.Bm;
import bezier.Interfata;
import bezier.Punct;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author eroot
 */
public class TcpClient extends Thread {

    Socket Client = null;
    DataOutputStream Dos = null;
    DataInputStream Ois = null;
    ObjectInputStream objis = null;
    String Adress = "";
    JTextArea text;
    int Port;
    int id = -1;
    String newline = "\n";
    boolean running = true;

    Bcs bezier_client;
    Bcs bezier_server;

    Bm module = new Bm();

    public TcpClient(String adress, int port, JTextArea text) {
        this.text = text;
        this.Adress = adress;
        this.Port = port;

    }

    public Socket get_socket() {
        return Client;
    }

    public void terminate() {
        text = null;
        Adress = null;
        Port = 0;
        id = 0;
        running = false;
        Dos = null;
        Ois = null;
        Client = null;
        interrupt();
    }

    public void initializeConnection() {

        try {

            Client = new Socket(Adress, Port);
            // Client.connect(new InetSocketAddress(Adress, Port), 1000);
            //Client.setSoTimeout(2000);
            text.append("-> Connected to: " + Adress + ":" + Port + newline);
            text.append(Client.toString() + " opened" + newline);
        } catch (Exception e) {
            text.append("-> Error initilizing socket " + newline);
            //System.out.println("\n Error initilizing socket " + e.getMessage());
        }

        try {

            Dos = new DataOutputStream(Client.getOutputStream());

            Ois = new DataInputStream(Client.getInputStream());
            objis = new ObjectInputStream(Client.getInputStream());
            text.append("-> streams opened" + newline);
            //  System.out.println("streams opened");

        } catch (IOException e) {
            text.append("-> Error initilizing Data Streams" + newline);
            //    System.out.println("\n Error initilizing Data Streams");
        }

    }

    public int get_Id() {

        return id;
    }

    public void setId(int nou) {

        id = nou;
    }

    
    
    public void sendMessage(String message) {
        try {
            if (message.equals(">keyspace")) {
                Dos.writeUTF(message);
            }
            else if(message.equals(">clearspace")){
                bezier_client.counter++;
                bezier_server.counter++;
                
            } else
            if (message.equals(">seespace")) {

                Interfata client = new Interfata(bezier_client,bezier_client.getMaxim());
                bezier_client.generate = true;
                bezier_client.draw = true;
                client.setTile("Client Bezier keyspace");
                
                try {
//                    client.grabScreenShot();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Interfata server = new Interfata(bezier_server,bezier_server.getMaxim());
                bezier_server.generate = true;
                bezier_server.draw = true;
                server.setTile("Server Bezier keyspace");
                try {
                    //  server.grabScreenShot();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                String encrypted = bezier_server.encrypt(message);
                System.out.println("mesaj " + encrypted);
                Dos.writeUTF(encrypted);
            }
        } catch (IOException e) {
            text.append("-> Could not send message" + newline);
            System.out.println("Could not send message");
        }
//        catch (NullPointerException n) {
//            text.append("-> Socket not initialized" + newline);
//            System.out.println("-> Socket not initialized" + newline);
//        }
    }

    public String receiveMessage() {
        try {
            String crypted = Ois.readUTF();
            if (crypted.equals("/force_keyspace") || crypted.equals("/keyspace")) {
                return crypted;
            } else {
                String decrypted = bezier_client.decrypt(crypted);
            //    System.out.println("mesaj " + decrypted);
                //  System.out.println("counter for client from client " + bezier_client.counter + " encrypted message is " + crypted);
                return decrypted;
            }
        } catch (IOException e) {
            text.append("-> Could not receive message" + newline);
            System.out.println("-> Could not receive message" + e.toString());
        }
//        } catch (NullPointerException n) {
//            text.append("-> Socket not initialized" + newline);
//            System.out.println("-> Socket not initialized"+ n.toString());
//        }
        return "error";
    }

    public Object receiveObject() {
        try {
            return objis.readObject();
        } catch (IOException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int receiveId() throws IOException {
        return Ois.readInt();
    }

    @Override
    public void run() {

        String temp = "";
        while (running) {
            temp = receiveMessage();
            if (temp.startsWith("/")) {
                server_check_command(temp);
            } else {
                text.append(temp + newline);
            }

        }
    }

    private boolean server_check_command(String msg) {

        switch (msg) {
            case "/deny":
                text.append("The server denied your connection" + newline);
                this.Client = null;
                try {
                    this.Dos.close();
                    this.Ois.close();
                    this.running = false;
                } catch (IOException ex) {
                    Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "/keyspace":
                bezier_server = (Bcs) receiveObject();
                bezier_client = (Bcs) receiveObject();
                text.append("The server has sent the keyspaces" + newline);
                break;
            case "/force_keyspace":
                try {
                    Dos.writeUTF(">keyspace");
                } catch (IOException ex) {
                    Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "/calibrate_server":
                bezier_server.counter++;
                break;
        }

        return false;
    }

    public InetAddress getAdress() {
        return Client.getInetAddress();
    }

}
