/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.


 This product includes GeoLite data created by MaxMind, available from
 <a href="http://www.maxmind.com">http://www.maxmind.com</a>.


 */
package bscc;

import bezier.Bcs;
import bezier.Interfata;
import bezier.Punct;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import tcp.TcpClient;
import tcp.TcpServer;
import windows.Help;
import windows.MainFrame;
import windows.MainWindow;


/**
 *
 * @author Cristi
 */
public class BSCC {
    
    TcpClient tcln = null;
    TcpServer tsrv = null;
    String newline = "\n";
    
   
   
    //MainWindow window = new MainWindow();
    MainFrame window = new MainFrame();
    /// end autocomplete
    public BSCC() {
        
        window.getDummyOkButton().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                check_message(window.getCmd().getText());
                window.getCmd().setText("");
            }
        });    
       
        
    }
    
    private void setClient(String Adress, int port) {
        tcln = new TcpClient(Adress, port, this.window.getText());
        tcln.initializeConnection();
        tcln.start();
    }
    
    private void setServer(int port) throws IOException {
        tsrv = new TcpServer(port, window.getText(),window);
        tsrv.start();
      
        tcln = new TcpClient("localhost", port, this.window.getText());
        tcln.initializeConnection();
        tcln.start();
        
       
        window.getText().append("Keyspace generated");
    }
    
    private void check_message(String message) {
        
        if (message.startsWith("/")) {
            do_command(message.substring(1));
        } else {
            // if (tsrv == null && tcln != null) {
            send_message(message);
            //  }
        }
        
    }
    
    private void do_command(String command) {
        
        String[] comm = command.split(" ");
        switch (comm[0]) {
            case "new":
                BSCC nou = new BSCC();
                
                break;
            case "help":
                Help h = new Help();
                break;
            case "clear":
                window.getText().setText("");
                break;
                                         
            
            case "client":
                
                switch (comm[1]) {
                    case "connect":
                       
                        try {
                             setClient(comm[2], Integer.parseInt(comm[3]));
                            window.getLabelClientIp().setText("Client"+tcln.get_socket().getInetAddress().toString());
                        } catch (Exception e) {
                            window.getText().append("-> Error initalizing Socket: " + e.toString() + newline);
                        }
                        break;
                    case "close":
                        
                        tcln.terminate();
                        tcln = null;
                        window.getText().append("-> Client is now offline" + newline);
                        break;
                }
                
                break;
            case "server":
                
                switch (comm[1]) {
                    case "new":
                        try {
                         
                            setServer(Integer.parseInt(comm[2]));
                            window.getLabelServerIP().setText("Server");
                         
                       
                           
                            
                        } catch (Exception e) {
                            window.getText().append("-> Error initializing ServerSocket: " + e.toString() + newline);
                        }
                        break;
                    
                    case "listen":
                        switch (comm[2]) {
                            case "start":
                                try {
                                    tsrv.start_listen();
                                    window.getText().append("-> Server is listening " + newline);
                                } catch (Exception e) {
                                    window.getText().append("-> Error trying to listen from server" + newline);
                                }
                                break;
                            case "stop":
                                try {
                                    tsrv.stop_listen();
                                    window.getText().append("-> Server stopped listening" + newline);
                                } catch (Exception ex) {
                                    window.getText().append("-> Could not pauze listening " + ex.toString() + newline);
                                }
                                
                                break;
                            
                        }
                        break;
                    case "close":
                        
                        tsrv.terminate();
                        tsrv = null;
                        window.getText().append("-> Server is now offline" + newline);
                        break;
                    
                }
                
                break;
            case "exit":
                System.exit(0);
                break;
            
        }
        
    }
    
    private void send_message(String message) {
        tcln.sendMessage(message);
    }
    
    public static void main(String[] args) {
        BSCC cln = new BSCC();
    }
}
