/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Play;

import static fp_progjar.FP_Progjar.loaders;
import static fp_progjar.FP_Progjar.nodes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import objectsend.PlayerStat;
import objectsend.cmd;
import objectsend.file;
import objectsend.jawabanSoal;
import objectsend.roomsAttr;
import room.InRoomController;
import room.ListroomController;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class PlayController implements Initializable {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatText;
    @FXML
    private Label timer;
    @FXML
    private Button AChoice;
    @FXML
    private Button BChoice;
    @FXML
    private Button CChoice;
    @FXML
    private Button DChoice;
    @FXML
    private TableView<PlayerStat> scoreBoard;
    
    private TableColumn<PlayerStat, String> playerName;
    
    private TableColumn<PlayerStat, String> playerSkor;
    private ObservableList<PlayerStat> allPlayer;
    @FXML
    private ProgressBar loadingBar;
    @FXML
    private Button cancelBtn;
    private String roomName;
    private jawabanSoal jSoal;
    
    private AnchorPane mainPane;
    private sockClass.socketio SockClient;
    private static boolean timerBoolA=true;
    private long starTime;
    private static boolean Tbool;
    private static sockClass.socketio staticSocket;
    private ArrayList<ArrayList<String>> theSoal;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AChoice.setDisable(true);
        BChoice.setDisable(true);
        CChoice.setDisable(true);
        DChoice.setDisable(true);
        playerName = new TableColumn<>("Name");
        playerSkor = new TableColumn<>("Score");
        playerName.setCellValueFactory(new PropertyValueFactory("name"));
        playerSkor.setCellValueFactory(new PropertyValueFactory("skor"));
        playerName.prefWidthProperty().bind(scoreBoard.widthProperty().multiply(0.50));
        playerSkor.prefWidthProperty().bind(scoreBoard.widthProperty().multiply(0.50));
        scoreBoard.getColumns().setAll(playerName,playerSkor);
        
    }
    public PlayController()
    {
        Tbool=true;
        scoreBoard = new TableView<>();
        jSoal=new jawabanSoal();
    }
    
    public void listener()
    {
        cmd response;
        roomsAttr rAttr;
        Object o;
        while(Tbool)
        {
            
            try {
                if(SockClient!=null){
                    o =  SockClient.readobject();
                    
                    if(o instanceof cmd)
                    {
                        response=(cmd) o;
                        //System.out.println(response.getArgument());
                        //System.out.println(response.getChattext());
                        if(response.getCommand().equals("CHAT") && response.getArgument().equals("USER"))
                        {
                            chatArea.appendText(response.getChattext());
                        }
                        
                    }
                    else if(o instanceof roomsAttr)
                    {
                        rAttr = (roomsAttr) o;
                        
                        if(rAttr.getCommand().equals("GAME") && rAttr.getArgument().equals("READY"))
                        {
                            loadingBar.setVisible(false);
                            
                            
                            Thread playSong;
                            playSong = new Thread(new Runnable() {
                                
                                @Override
                                public void run() {
                                    for(int i=0;i<theSoal.size();i++)
                                    {
                                        try {
                                            final int j=i;
                                            int t=30;
                                            if(Tbool==false)break;
                                            timerBoolA=true;
                                            jSoal.setJawaban("null");
                                            jSoal.setWaktu(0);
                                            starTime=System.currentTimeMillis();
                                            final Media media = new Media(new File("cache\\"+i+".mp3").toURI().toURL().toString());
                                            
                                            final MediaPlayer mediaPlayer = new MediaPlayer(media);
                                            mediaPlayer.setStartTime(Duration.seconds(60.0));
                                            mediaPlayer.setStopTime(Duration.seconds(90.0));
                                            
                                            mediaPlayer.play();
                                            Platform.runLater(new Runnable() {
                                                
                                                @Override
                                                public void run() {
                                                    AChoice.setText(theSoal.get(j).get(0));
                                                    BChoice.setText(theSoal.get(j).get(1));
                                                    CChoice.setText(theSoal.get(j).get(2));
                                                    DChoice.setText(theSoal.get(j).get(3));
                                                    AChoice.setDisable(false);
                                                    BChoice.setDisable(false);
                                                    CChoice.setDisable(false);
                                                    DChoice.setDisable(false);
                                                }
                                            });
                                            while(timerBoolA)
                                            {
                                                try {
                                                    final String text="Time: " + t ;
                                                    
                                                    Platform.runLater(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                        timer.setText(text);
                                                    
                                                        }
                                                    });
                                                    Thread.sleep(1000);
                                                    t--;
                                                    
                                                    if(t==0)
                                                    {  
                                                        timerBoolA=false;
                                                    }
                                                } catch (InterruptedException ex) {
                                                    Logger.getLogger(PlayController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                            mediaPlayer.stop();
                                        } catch (MalformedURLException ex) {
                                            Logger.getLogger(PlayController.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            AChoice.setDisable(true);
                                            BChoice.setDisable(true);
                                            CChoice.setDisable(true);
                                            DChoice.setDisable(true);
                                            AChoice.setText("A");
                                            BChoice.setText("B");
                                            CChoice.setText("C");
                                            DChoice.setText("D");
                                        }
                                    });
                                    
                                }
                            });
                            playSong.start();
                            
                        }
                    }
                    else if(o instanceof ArrayList<?>)
                    {
                        try
                        {
                            final ArrayList<PlayerStat> players= (ArrayList<PlayerStat>) o;
                            //System.out.println(players);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    allPlayer = scoreBoard.getItems();
                                    allPlayer.clear();
                                    allPlayer.addAll(players);
                                    scoreBoard.setItems(allPlayer);
                                    scoreBoard.getColumns().setAll(playerName,playerSkor);
                                }
                            });
                            
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
    public void setMainPane(AnchorPane mainPane)
    {
        this.mainPane=mainPane;
        
    }
    public void setSocket(final sockClass.socketio SockClient)
    {
        Tbool=true;
        loadingBar.setVisible(true);
        this.SockClient =SockClient;
        staticSocket=SockClient;
        chatArea.setText("");
        Thread updateScoreBoard= new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while(Tbool)
                    {
                        cmd command = new cmd();
                        command.setCommand("UPDATE");
                        command.setArgument("SCOREBOARD");
                        command.setRoomName(roomName);
                        synchronized(SockClient){
                            SockClient.sendobject(command);
                        }Thread.sleep(2000);
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(PlayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PlayController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Thread songTransfer = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    new File("cache").mkdir();
                    cmd command = new cmd();
                    command.setCommand("START");
                    command.setArgument("TRANSFER");
                    command.setRoomName(roomName);
                    synchronized(SockClient)
                    {
                        SockClient.sendobject(command);
                    }
                    ServerSocket fileListener= new ServerSocket(9001);
                    sockClass.socketio transferSocket = new sockClass.socketio(fileListener.accept());
                    Object o=transferSocket.readobject();
                    if(o instanceof roomsAttr)
                    {
                        roomsAttr response = (roomsAttr) o;
                        if(response.getCommand().equals("NUMBER") && response.getArgument().equals("SONG"))
                        {
                            for(int i=0;i<response.getNumberTracks();i++)
                            {
                                file inputfile = new file();
                                FileOutputStream fos =new FileOutputStream("cache/"+i+".mp3");
                                while((inputfile=(file)transferSocket.readobject())!=null)
                                {
                                    fos.write(inputfile.getFile());

                                    if(inputfile.getStatus().equals("FINISH"))
                                    {
                                        break;
                                    }
                                }
                                fos.close();
                            }

                        }
                    }
                    o=transferSocket.readobject();
                    if(o instanceof ArrayList<?>)
                    {
                        theSoal =(ArrayList<ArrayList<String>>) o;
                        
                    }
                    fileListener.close();
                    transferSocket.disconnect();

                } catch (IOException ex) {
                    Logger.getLogger(PlayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PlayController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Thread listen= new Thread(new Runnable() {

            @Override
            public void run() {
                listener();
            }
        });
        listen.start();
        songTransfer.start();
        updateScoreBoard.start();
    }
    public void setRoomName(String roomName)
    {
        this.roomName=roomName;
    }
    @FXML
    private void onEnter(ActionEvent event) throws IOException {
        String chatMsg=chatText.getText();
        cmd chat = new cmd();
        chat.setCommand("CHAT");
        chat.setArgument("ROOM");
        chat.setRoomName(roomName);
        chat.setChattext(chatMsg);
        synchronized(SockClient){
            SockClient.sendobject(chat);
            
        }
        this.chatText.setText("");
    }
    public void cancelClick() throws IOException
    {
        cancel();
        mainPane.getChildren().clear();
        
        ListroomController Controller;
        mainPane.getChildren().add(nodes.get(2));
        
        
        Controller = loaders.get(2).getController();

        Controller.setMainPane(mainPane);
        Controller.setSocket(SockClient);

        loaders.get(2).setController(Controller);
    }
    public static void cancel() throws IOException
    {
        Tbool=false;
        timerBoolA=false;
        if(staticSocket!=null){
            cmd command= new cmd();
            command.setCommand("DUMMY");
            command.setArgument("EXITROOM");
            staticSocket.sendobject(command);
        }
        
    }
    @FXML
    private void ChooseA(MouseEvent event) throws IOException {
        long calculate = System.currentTimeMillis()-starTime;
        jSoal.setJawaban(AChoice.getText());
        jSoal.setWaktu(calculate);
        synchronized(SockClient){
            SockClient.sendobject(jSoal);
        }
        //System.out.println(jSoal);
        AChoice.setDisable(true);
        BChoice.setDisable(true);
        CChoice.setDisable(true);
        DChoice.setDisable(true);
        timerBoolA=false;
    }

    @FXML
    private void ChooseB(MouseEvent event) throws IOException {
        long calculate = System.currentTimeMillis()-starTime;
        jSoal.setJawaban(BChoice.getText());
        jSoal.setWaktu(calculate);
        synchronized(SockClient){
            SockClient.sendobject(jSoal);
        }//System.out.println(jSoal);
        AChoice.setDisable(true);
        BChoice.setDisable(true);
        CChoice.setDisable(true);
        DChoice.setDisable(true);
        
        timerBoolA=false;
    }

    @FXML
    private void ChooseC(MouseEvent event) throws IOException {
        long calculate = System.currentTimeMillis()-starTime;
        jSoal.setJawaban(CChoice.getText());
        jSoal.setWaktu(calculate);
        synchronized(SockClient){
            SockClient.sendobject(jSoal);
        }//System.out.println(jSoal);
        AChoice.setDisable(true);
        BChoice.setDisable(true);
        CChoice.setDisable(true);
        DChoice.setDisable(true);
        
        timerBoolA=false;
    }

    @FXML
    private void ChooseD(MouseEvent event) throws IOException {
        long calculate = System.currentTimeMillis()-starTime;
        jSoal.setJawaban(DChoice.getText());
        jSoal.setWaktu(calculate);
        synchronized(SockClient){
            SockClient.sendobject(jSoal);
        }//System.out.println(jSoal);
        AChoice.setDisable(true);
        BChoice.setDisable(true);
        CChoice.setDisable(true);
        DChoice.setDisable(true);
        
        timerBoolA=false;
    }
    
}
