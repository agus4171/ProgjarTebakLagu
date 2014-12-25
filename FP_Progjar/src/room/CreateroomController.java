/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package room;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objectsend.cmd;
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
        FXMLLoader loader =  new FXMLLoader(room.InRoomController.class.getResource("inRoom.fxml"));
        InRoomController Controller;
        mainPane.getChildren().add((Node)loader.load());
        Controller = loader.getController();
        
        Controller.setOwner(roomName.getText());
        Controller.setMainPane(mainPane);
        Controller.setSocket(SockClient);
        loader.setController(Controller);
    }
    @FXML
    private void cancel()
    {
        
    }
    
}
