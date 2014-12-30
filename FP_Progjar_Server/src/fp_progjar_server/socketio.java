/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fp_progjar_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author Administrator
 */
public class socketio {
    private ObjectInputStream objectinput;
    private ObjectOutputStream objectoutput;

    private Socket socketcli;
    private Socket sockfile;
    private int speed =1024;
    
    public socketio(String Ip,int port) throws IOException
    {
        
        this.socketcli= new Socket(Ip,port);            
        this.objectoutput=new ObjectOutputStream(this.socketcli.getOutputStream());
        this.objectinput=new ObjectInputStream(this.socketcli.getInputStream());
    }
    
    public socketio(Socket sock) throws IOException
    {
        
        this.socketcli= sock;            
        this.objectoutput=new ObjectOutputStream(this.socketcli.getOutputStream());
        this.objectinput=new ObjectInputStream(this.socketcli.getInputStream());
    }
    
    public void sendobject(Object objekKirim) throws IOException
    {        
        this.objectoutput.writeObject(objekKirim);
        this.objectoutput.flush();
        this.objectoutput.reset();       
    }
    public void disconnect() throws IOException
    {
        this.objectinput.close();
        this.objectoutput.close();
        this.socketcli.close();
    }
    public Object readobject() throws IOException, ClassNotFoundException
    {
        return this.objectinput.readObject();
    }
    
    
    
}
