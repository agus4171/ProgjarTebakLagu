/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp_progjar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Administrator
 */
public class FP_Progjar extends Application {
    public static ArrayList<FXMLLoader> loaders;
    public static ArrayList<Node> nodes;
    @Override
    public void start(final Stage primaryStage) throws IOException {
       loaders=new ArrayList<>();
       nodes=new ArrayList<>();
       //0
       FXMLLoader loaderMain =  new FXMLLoader();
       loaderMain.setLocation(getClass().getResource("MainProgram.fxml"));
       loaders.add(loaderMain);
       
       //1
       FXMLLoader loaderLogin =  new FXMLLoader();
       loaderLogin.setLocation(login.LogggController.class.getResource("loggg.fxml"));
       loaders.add(loaderLogin);
       
       //2
       FXMLLoader loaderListRoom =  new FXMLLoader();
       loaderListRoom.setLocation(room.ListroomController.class.getResource("listroom.fxml"));
       loaders.add(loaderListRoom);
       //3
       FXMLLoader loaderCreateRoom=new FXMLLoader();
       loaderCreateRoom.setLocation(room.CreateroomController.class.getResource("createroom.fxml"));
       loaders.add(loaderCreateRoom);
       //4
       FXMLLoader loaderInRoom =  new FXMLLoader();
       loaderInRoom.setLocation(room.InRoomController.class.getResource("inRoom.fxml"));
       loaders.add(loaderInRoom);
       //5
       FXMLLoader loaderPlay =  new FXMLLoader();
       loaderPlay.setLocation(Play.PlayController.class.getResource("play.fxml"));
       loaders.add(loaderPlay);
       
       
       //nodes.add((Node) loaders.get(2).load());
       //nodes.add((Node) loaders.get(3).load());
       //nodes.add((Node) loaders.get(4).load());
       //nodes.add((Node) loaders.get(5).load());
       Node node =(Node) loaders.get(0).load();
       nodes.add(node );
       
       Parent root = (Parent) node; //FXMLLoader.load(getClass().getResource("MainProgram.fxml"));
       
       
       Scene scene =new Scene(root);
       primaryStage.setScene(scene);
       primaryStage.show();
       primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

           @Override
           public void handle(WindowEvent t) {
               room.ListroomController.cancel();
               try {
                   room.InRoomController.cancel();
               } catch (IOException ex) {
                   System.out.println("Close");
                   primaryStage.close();
                   Logger.getLogger(FP_Progjar.class.getName()).log(Level.SEVERE, null, ex);
                   
               }
               
           }
       });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
