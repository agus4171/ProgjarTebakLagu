/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp_progjar;

import static fp_progjar.FP_Progjar.loaders;
import static fp_progjar.FP_Progjar.nodes;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
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
    int indexLoader;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            mainPane.getChildren().clear();
            login.LogggController Controller;
            Node node= (Node) loaders.get(1).load();
            mainPane.getChildren().add(node);
            Controller = loaders.get(1).getController();
            Controller.setmainPane(mainPane);
            loaders.get(1).setController(Controller);
            nodes.add((Node) node);
        } catch (IOException ex) {
            Logger.getLogger(MainProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
