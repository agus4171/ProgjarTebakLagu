/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import static fp_progjar.FP_Progjar.loaders;
import static fp_progjar.FP_Progjar.nodes;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
    public void setMainPane(AnchorPane mainPane)
    {
        this.mainPane=mainPane;
    }
    public LogggController() throws IOException
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
    public  void login() throws ClassNotFoundException
    {
        try {
            client =new socketio("10.151.36.85", 9000);
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
                
                ListroomController Controller;
                
                if(nodes.size()<3){
                   Node node= (Node) loaders.get(2).load();
                    mainPane.getChildren().add(node);
                    nodes.add(node);
                }
                else mainPane.getChildren().add(nodes.get(2));
                Controller = loaders.get(2).getController();
               
                Controller.setMainPane(mainPane);
                Controller.setSocket(client);
                Controller.setUser(username.getText());
                loaders.get(2).setController(Controller);
                
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
    private void onEnter(ActionEvent event) throws ClassNotFoundException, Throwable {
        login();
    }
    
    @FXML
    private void LoginBtn(MouseEvent event) throws ClassNotFoundException {
        login();
    }
    
}
