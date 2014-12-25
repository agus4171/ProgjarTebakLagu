/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp_progjar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class MainProgramController implements Initializable {
    @FXML
    private AnchorPane mainPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPane.getChildren().clear();
        FXMLLoader loader =  new FXMLLoader(login.LogggController.class.getResource("loggg.fxml"));
        try {
            login.LogggController Controller;
            mainPane.getChildren().add((Node)loader.load());
           
            
            Controller = loader.getController();
            Controller.setmainPane(mainPane);
            loader.setController(Controller);
            
        } catch (IOException ex) {
            Logger.getLogger(MainProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
