/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package room;

import Play.PlayController;
import static fp_progjar.FP_Progjar.loaders;
import static fp_progjar.FP_Progjar.nodes;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
public class InRoomController implements Initializable {
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
        
    }    
    public InRoomController() throws IOException
    {
        
        
        
    }
    
    public void listener()
    {
        
        
        Object o;
        while(Tbool)
        {
            final cmd response;
            try {
                if(SockClient!=null){
                    synchronized(SockClient){
                        o =  SockClient.readobject();
                    }
                    
                    if(o instanceof cmd)
                    {
                        response=(cmd) o;
                        System.out.println(response.getChattext());
                        if(response.getCommand().equals("CHAT") && response.getArgument().equals("USER"))
                        {
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    chatArea.appendText(response.getChattext());
                                }
                            });
                        }
                        
                        if(response.getCommand().equals("STARTING") && response.getArgument().equals("GAME"))
                        {
                            cancel();
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        mainPane.getChildren().clear();
                                        PlayController Controller;
                                        if(nodes.size()<6){
                                            Node node= (Node) loaders.get(5).load();
                                            mainPane.getChildren().add(node);
                                            nodes.add(node);
                                        }
                                        else mainPane.getChildren().add(nodes.get(5));
                                        
                                        Controller = loaders.get(5).getController();
                                        Controller.setMainPane(mainPane);
                                        Controller.setSocket(SockClient);
                                        Controller.setRoomName(roomName);
                                        loaders.get(5).setController(Controller);
                                        
                                    } catch (IOException ex) {
                                        Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            
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
        this.startGame.setDisable(false);
    }
    public void setFollower(String roomName)
    {
        this.hak="FOLLOWER";
        this.roomName=roomName;
        this.roomLabel.setText(roomName);
        this.startGame.setDisable(true);
    }
    public void setSocket(final sockClass.socketio SockClient)
    {
        Tbool=true;
        this.SockClient =SockClient;
        staticSocket=SockClient;
        chatArea.setText("");
        final cmd request=new cmd();
        Thread listen= new Thread(new Runnable() {

            @Override
            public void run() {
                listener();
            }
        });
        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                while(Tbool){
                    request.setCommand("ROOM");
                    request.setArgument("MEMBER");
                    request.setRoomName(roomName);
                    
                    try {
                        SockClient.sendobject(request);
                        Thread.sleep(2000);
                    } catch (IOException ex) {
                        Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InRoomController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        });
        t.start();
        listen.start();
        
        
    }
    @FXML
    private void onStartGame(MouseEvent event) throws IOException {
        cmd game = new cmd();
        game.setCommand("START");
        game.setArgument("GAME");
        game.setRoomName(roomName);
        
        SockClient.sendobject(game);
    }

    @FXML
    private void onCancel(MouseEvent event) throws IOException {
        Tbool=false;
        if(staticSocket!=null){
            cmd command= new cmd();
            command.setCommand("DUMMY");
            command.setArgument("EXITROOM");
            staticSocket.sendobject(command);
        }
        
        mainPane.getChildren().clear();
        
        ListroomController Controller;
        mainPane.getChildren().add(nodes.get(2));
        
        
        Controller = loaders.get(2).getController();

        Controller.setMainPane(mainPane);
        Controller.setSocket(SockClient);

        loaders.get(2).setController(Controller);
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
