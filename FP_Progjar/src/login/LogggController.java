/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import objectsend.cmd;
import room.ListroomController;
import sockClass.socketio;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class LogggController implements Initializable {
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private Button loginbtn;
    
    private sockClass.socketio client;
    
    private AnchorPane mainPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            
    }    
    
    public LogggController() throws IOException
    {
        
         this.mainPane=mainPane;
         
    }
    public void setmainPane(AnchorPane mainPane)
    {
        this.mainPane=mainPane;
    }
    private void MessageBox(String message)
    {
        Button ok;
            ok=new Button("OK");
            final Stage dialogStage = new Stage();
            ok.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                  dialogStage.close();
                }
            });
            
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(new Text(message),ok).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
            dialogStage.show();
    }
    
    @FXML
    private void onEnter(ActionEvent event) throws ClassNotFoundException, Throwable {
        try {
            client =new socketio("10.151.36.24", 9000);
            cmd response = new cmd();
            cmd login =new cmd();
            login.setCommand("LOGIN");
            login.setArgument("USER");
            login.setNamaUser(username.getText());
            login.setPassword(password.getText());
            client.sendobject(login);
            response = (cmd) client.readobject();
            if(response.getCommand().equals("LOGIN") && response.getArgument().equals("OK"))
            {
                
                mainPane.getChildren().clear();
                FXMLLoader loader =  new FXMLLoader(room.ListroomController.class.getResource("listroom.fxml"));
                ListroomController Controller;
                mainPane.getChildren().add((Node)loader.load());
                Controller = loader.getController();
               
                Controller.setMainPane(mainPane);
                Controller.setSocket(client);
                Controller.setUser(username.getText());
                loader.setController(Controller);
                
            }
            else if(response.getCommand().equals("LOGIN") && response.getArgument().equals("FAILED"))
            {
                MessageBox("username dan password anda salah");
            } 
        } catch (IOException ex) {
            Logger.getLogger(LogggController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void LoginBtn(MouseEvent event) throws ClassNotFoundException {
        try {
            client =new socketio("127.0.0.1", 9000);
            cmd response = new cmd();
            cmd login =new cmd();
            login.setCommand("LOGIN");
            login.setArgument("USER");
            login.setNamaUser(username.getText());
            login.setPassword(password.getText());
            client.sendobject(login);
            response = (cmd) client.readobject();
            if(response.getCommand().equals("LOGIN") && response.getArgument().equals("OK"))
            {
                mainPane.getChildren().clear();
                FXMLLoader loader =  new FXMLLoader(room.ListroomController.class.getResource("listroom.fxml"));
                ListroomController Controller;
                mainPane.getChildren().add((Node)loader.load());
                Controller = loader.getController();
                Controller.setMainPane(mainPane);
                Controller.setSocket(client);
                loader.setController(Controller);
            }
            else if(response.getCommand().equals("LOGIN") && response.getArgument().equals("FAILED"))
            {
                MessageBox("username dan password anda salah");
            } 
        } catch (IOException ex) {
            Logger.getLogger(LogggController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
