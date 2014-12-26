/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import objectsend.cmd;
import sockClass.socketio;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ChatRoomController extends Thread implements Initializable {
    @FXML
    private TextField chatfField;
    @FXML
    private Button chatSendBtn;
    @FXML
    private TextArea chatArea;
    
    private sockClass.socketio client;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            client =new socketio("127.0.0.1", 9000);
            cmd login =new cmd();
            login.setCommand("LOGIN");
            login.setArgument("USER");
            login.setNamaUser("Anonymus");
            login.setPassword("aaa");
            client.sendobject(login);
        } catch (IOException ex) {
            Logger.getLogger(ChatRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.start();
    }   
    
    public void run()
    {
        cmd response = new cmd();
        try {
            
            
            while(response!=null){
                response = (cmd) client.readobject();
                System.out.println(response.getCommand() + " " + response.getArgument());
                if(response.getCommand().equals("CHAT") && response.getArgument().equals("USER"))
                {
                    chatArea.appendText(response.getChattext());
                }
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ChatRoomController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void sendChat(MouseEvent event) throws IOException {
        String chatMsg=chatfField.getText();
        cmd chat = new cmd();
        chat.setCommand("CHAT");
        chat.setArgument("ROOM");
        chat.setRoomName("HARMONI");
        chat.setChattext(chatMsg);
        client.sendobject(chat);
        
    }
    @FXML
    private void onEnter() throws IOException
    {
        String chatMsg=chatfField.getText();
        cmd chat = new cmd();
        chat.setCommand("CHAT");
        chat.setArgument("ROOM");
        chat.setRoomName("HARMONI");
        chat.setChattext(chatMsg);
        client.sendobject(chat);
        this.chatfField.setText("");
    }
    
}
