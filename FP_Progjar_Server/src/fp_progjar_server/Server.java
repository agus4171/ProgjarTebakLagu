/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fp_progjar_server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Media;
import soal.*;

/**
 *
 * @author Administrator
 */
public class Server {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, MalformedURLException {
      
          
        ArrayList<handler> alhandler;
        ArrayList<room> rooms;
        new File("cache").mkdir();
        ServerSocket sServer = null;
        try {
            alhandler =new ArrayList<>();
            rooms= new ArrayList<>();
            sServer = new ServerSocket(9000);
            while(true){
                handler client;
                client = new handler(sServer.accept(), alhandler,rooms);
                synchronized(alhandler)
                {
                    
                    alhandler.add(client);
                    Thread t = new Thread(client);
                    t.start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
}
