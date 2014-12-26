/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package room;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import objectsend.cmd;
import objectsend.roomsAttr;
import sockClass.socketio;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ListroomController extends Thread implements Initializable {

    @FXML
    private TableView<roomsAttr> roomTable;
    @FXML
    private TableColumn<roomsAttr, String> roomNames;
    @FXML
    private TableColumn<roomsAttr, String> roomLevel;
    @FXML
    private TableColumn<roomsAttr, String> numberPlayer;
    private ObservableList<roomsAttr> allRoom;
    @FXML
    private Button createRoom;
    @FXML
    private Button joinRoom;
    private AnchorPane mainPane;
    private socketio Sockclient;
    public static boolean Tbool;
    @FXML
    private Label usernameLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomNames = new TableColumn<>("Name");
        roomLevel = new TableColumn<>("Level");
        numberPlayer = new TableColumn<>("Number Player");
        roomNames.setCellValueFactory(new PropertyValueFactory("name"));
        roomLevel.setCellValueFactory(new PropertyValueFactory("level"));
        numberPlayer.setCellValueFactory(new PropertyValueFactory("availableSlot"));
        roomNames.prefWidthProperty().bind(roomTable.widthProperty().multiply(0.25));
        roomLevel.prefWidthProperty().bind(roomTable.widthProperty().multiply(0.25));
        numberPlayer.prefWidthProperty().bind(roomTable.widthProperty().multiply(0.25));
        roomTable.getColumns().setAll(roomNames, roomLevel, numberPlayer);
        this.start();
    }

    public ListroomController() {
        roomTable = new TableView<>();
        Tbool = true;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
        while (Tbool) {

            cmd request = new cmd();
            try {
                if (Sockclient != null) {
                    Object o;
                    final ArrayList<roomsAttr> listroom;
                    request.setCommand("LIST");
                    request.setArgument("ROOM");
                    Sockclient.sendobject(request);
                    o = Sockclient.readobject();
                    if (o instanceof ArrayList<?>) {
                        listroom = (ArrayList<roomsAttr>) o;
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                int i= roomTable.getSelectionModel().getSelectedIndex();
                                
                                allRoom = roomTable.getItems();
                                allRoom.clear();
                                allRoom.addAll(listroom);
                                roomTable.setItems(allRoom);
                                roomTable.getColumns().setAll(roomNames, roomLevel, numberPlayer);
                                
                                roomTable.getSelectionModel().select(i);
                            }
                        });
                        System.out.println(listroom);
                    }
                    else if(o instanceof cmd)
                    {
                        cmd command = (cmd) o;
                        if(command.getCommand().equals("JOIN") && command.getArgument().equals("OK"))
                        {
                            System.out.println("JOINED TO " + roomTable.getSelectionModel().getSelectedItem().getName());
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        cancel();
                                        mainPane.getChildren().clear();
                                        FXMLLoader loader =  new FXMLLoader(room.InRoomController.class.getResource("inRoom.fxml"));
                                        InRoomController Controller;
                                        mainPane.getChildren().add((Node)loader.load());
                                        Controller = loader.getController();
                                        
                                        Controller.setFollower(roomTable.getSelectionModel().getSelectedItem().getName());
                                        Controller.setMainPane(mainPane);
                                        Controller.setSocket(Sockclient);
                                        loader.setController(Controller);
                                        
                                    } catch (IOException ex) {
                                        Logger.getLogger(ListroomController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            
                        }
                    }
                    
                    Thread.sleep(1000);
                }

            } catch (IOException ex) {
                Logger.getLogger(ListroomController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListroomController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ListroomController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void cancel() {
        Tbool = false;
    }

    public void setMainPane(AnchorPane mainPane) {
        this.mainPane = mainPane;
    }

    public void setSocket(socketio Sockclient) {
        this.Sockclient = Sockclient;
    }
    public void setUser(String user)
    {
        this.usernameLabel.setText(user);
    }
    @FXML
    private void createNewRoom(MouseEvent event) throws IOException, Throwable {
        cancel();
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(room.CreateroomController.class.getResource("createroom.fxml"));
        CreateroomController Controller;
        mainPane.getChildren().add((Node) loader.load());

        Controller = loader.getController();
        Controller.setMainPane(mainPane);
        Controller.setSocket(this.Sockclient);

        loader.setController(Controller);

    }

    @FXML
    private void joinNewRoom(MouseEvent event) throws IOException {
        
        cmd command = new cmd();
        command.setCommand("JOIN");
        command.setArgument("ROOM");
        if (!roomTable.getSelectionModel().isEmpty()) {
            command.setRoomName(roomTable.getSelectionModel().getSelectedItem().getName());
            Sockclient.sendobject(command);   
        }
    }

}
