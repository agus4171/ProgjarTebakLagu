/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package room;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import objectsend.cmd;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class InRoomController extends Thread implements Initializable {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatText;
    @FXML
    private Label roomLabel;
    @FXML
    private TextField p1;
    @FXML
    private TextField p2;
    @FXML
    private TextField p3;
    @FXML
    private TextField p4;
    @FXML
    private TextField p5;
    @FXML
    private Button startGame;
    ArrayList<TextField> alField;
    private AnchorPane mainPane;
    private sockClass.socketio SockClient;
    private static sockClass.socketio staticSocket;
    private String hak;
    private String roomName;
    private static boolean Tbool;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alField=new ArrayList<>();
        alField.add(p1);
        alField.add(p2);
        alField.add(p3);
        alField.add(p4);
        alField.add(p5);
        this.start();
        final cmd request=new cmd();
        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                while(Tbool){
                    request.setCommand("ROOM");
                    request.setArgument("MEMBER");
                    request.setRoomName(roomName);
                    
                    try {
                        SockClient.sendobject(request);
                        Thread.sleep(1000);
                    } catch (IOException ex) {
                        Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        });
        t.start();
    }    
    public InRoomController() throws IOException
    {
        Tbool=true;
        
        
    }
    @Override
    public void run()
    {
        cmd response;
        
        Object o;
        while(Tbool)
        {
            
            try {
                if(SockClient!=null){
                    o =  SockClient.readobject();
                    
                    if(o instanceof cmd)
                    {
                        response=(cmd) o;
                        System.out.println(response.getChattext());
                        if(response.getCommand().equals("CHAT") && response.getArgument().equals("USER"))
                        {
                            chatArea.appendText(response.getChattext());
                        }
                    }
                    else if(o instanceof ArrayList<?>)
                    {
                        try
                        {
                            ArrayList<String> member= (ArrayList<String>) o;
                            for(int i=0;i<alField.size();i++)
                            {
                                alField.get(i).setText("");
                            }
                            for(int i=0;i<member.size();i++)
                            {                                
                                alField.get(i).setText(member.get(i));
                            }
                        }
                        catch(Exception e)
                        {
                            System.out.println("Wrong class");
                        }
                    }
                      
                }
            } catch (IOException ex) {
                Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static void cancel() throws IOException
    {
        Tbool=false;
        if(staticSocket!=null){
            cmd command= new cmd();
            command.setCommand("DUMMY");
            staticSocket.sendobject(command);
        }
        
    }
    public void setMainPane(AnchorPane mainPane)
    {
        this.mainPane=mainPane;
    }
    public void setOwner(String roomName)
    {
        this.hak="OWNER";
        this.roomName=roomName;
        this.roomLabel.setText(roomName);
    }
    public void setFollower(String roomName)
    {
        this.hak="FOLLOWER";
        this.roomName=roomName;
        this.roomLabel.setText(roomName);
        this.startGame.setDisable(true);
    }
    public void setSocket(sockClass.socketio SockClient)
    {
        this.SockClient =SockClient;
        staticSocket=SockClient;
        
    }
    @FXML
    private void onStartGame(MouseEvent event) {
        
    }

    @FXML
    private void onCancel(MouseEvent event) {
        
    }

    @FXML
    private void onEnter(ActionEvent event) throws IOException {
        String chatMsg=chatText.getText();
        cmd chat = new cmd();
        chat.setCommand("CHAT");
        chat.setArgument("ROOM");
        chat.setRoomName(roomName);
        chat.setChattext(chatMsg);
        SockClient.sendobject(chat);
        this.chatText.setText("");
    }
    
}
