/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package room;

import static fp_progjar.FP_Progjar.loaders;
import static fp_progjar.FP_Progjar.nodes;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import objectsend.roomsAttr;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class CreateroomController implements Initializable {
    @FXML
    private Button createRomBtn;
    @FXML
    private TextField roomName;
    @FXML
    private ComboBox<String> level;
    @FXML
    private ComboBox<String> tracks;
    @FXML
    private Button cancelBtn;
    private AnchorPane mainPane;
    private sockClass.socketio SockClient;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        level.getItems().addAll("EASY","MEDIUM","HARD");
        level.setValue("EASY");
        tracks.getItems().addAll("3","5","10");
        tracks.setValue("3");
    }
    public  CreateroomController()
    {
        level=new ComboBox<>();
        tracks = new ComboBox<>();
        
    }
    public void setMainPane(AnchorPane mainPane)
    {
        this.mainPane=mainPane;
        
    }
    public void setSocket(sockClass.socketio SockClient)
    {
        this.SockClient =SockClient;
    }
    @FXML
    private void createRoom(MouseEvent event) throws IOException {
        roomsAttr roomAttr= new roomsAttr();
        roomAttr.setCommand("CREATE");
        roomAttr.setArgument("ROOM");
        roomAttr.setName(roomName.getText());
        roomAttr.setLevel(level.getSelectionModel().getSelectedItem());
        roomAttr.setMaxPlayer(5);
        roomAttr.setNumberTracks(Integer.parseInt(tracks.getSelectionModel().getSelectedItem()));
        
        SockClient.sendobject(roomAttr);
        
        mainPane.getChildren().clear();
       
        InRoomController Controller;
        if(nodes.size()<5){
            Node node= (Node) loaders.get(4).load();
            mainPane.getChildren().add(node);
            nodes.add(node);
        }
        else mainPane.getChildren().add(nodes.get(4));

        
        Controller = loaders.get(4).getController();
        
        Controller.setOwner(roomName.getText());
        Controller.setMainPane(mainPane);
        Controller.setSocket(SockClient);
        loaders.get(4).setController(Controller);
    }
    @FXML
    private void cancel()
    {
        mainPane.getChildren().clear();
        
        ListroomController Controller;
        mainPane.getChildren().add(nodes.get(2));
        
        
        Controller = loaders.get(2).getController();

        Controller.setMainPane(mainPane);
        Controller.setSocket(SockClient);

        loaders.get(2).setController(Controller);
    }
    
}
