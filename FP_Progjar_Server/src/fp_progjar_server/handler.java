/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp_progjar_server;

//import SoalTebakLagu.DBConnection;
import java.io.File;
import java.io.FileInputStream;
import objectsend.cmd;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import objectsend.PlayerStat;
import objectsend.file;
import objectsend.jawabanSoal;
import objectsend.roomsAttr;
import soal.*;

/**
 *
 * @author Administrator
 */
public class handler implements Runnable {

    private Socket sockcli;
    private ObjectOutputStream objectoutput;
    private ObjectInputStream objectinput;

    private DateFormat dateFormat;
    private Date date;
    private String LoginTime;
    private String addr;
    private String passwd;
    private final ArrayList<handler> alhandler;
    private ArrayList<String> aluser;
    private ArrayList<room> rooms;
    private double skor=0;
    private int totalSkor=0;
    //new deklarasi by agus
    private String query;
    private String password;
    private ArrayList<String> listPlayer;

    private int speed = 1024;
    db_connection playerConnection = new db_connection();

    public handler(Socket cli, ArrayList<handler> alhandler, ArrayList<room> rooms) throws IOException {
        this.alhandler = alhandler;
        this.sockcli = cli;
        this.objectoutput = new ObjectOutputStream(cli.getOutputStream());
        this.objectinput = new ObjectInputStream(cli.getInputStream());
        aluser = new ArrayList<>();
        this.rooms = rooms;

       

    }
    public String getName()
    {
        return this.addr;
    }
    public double getSkor()
    {
        return skor;
    }
    public synchronized void ListUser() throws IOException {
        ArrayList<cmd> alluser = new ArrayList<>();

        for (int i = 0; i < this.alhandler.size(); i++) {
            cmd user = new cmd();
            handler client = this.alhandler.get(i);
            user.setNamaUser(client.addr);
            user.setIp(client.sockcli.getInetAddress().toString() + ":" + client.sockcli.getPort());
            user.setLoginDate(client.LoginTime);
            alluser.add(user);
            System.out.println(client.addr);

        }

        this.sendobject(alluser);

    }

    public void sendobject(Object objeckKirim) throws IOException {
        this.objectoutput.writeObject(objeckKirim);
        this.objectoutput.flush();
        this.objectoutput.reset();

    }

    @Override
    public void run() {

        Object o;
        cmd request;
        cmd command;
        System.out.println("connected\n");
        
        try {
            while ((o = this.objectinput.readObject()) != null) {
                if (o instanceof cmd) {
                    
                    request = (cmd) o;
                    if(request.getCommand().equals("DUMMY"))
                    {
                        if(request.getArgument()!=null && request.getArgument().equals("EXITROOM"))
                        for (room room : rooms) {
                            if (room.users().contains(this)) {
                                room.users().remove(this);
                            }
                        }
                        this.sendobject(o);
                    }
                    System.out.println(request.getCommand() + " " + request.getArgument());
                    if (request.getCommand().equals("LOGIN")) {
                        command = new cmd();
                        command.setCommand("LOGIN");

                        
                        //System.out.println();
                        this.addr = request.getNamaUser();
                        this.passwd = request.getPassword();
                        query = "SELECT * FROM player WHERE ID_Player = '"+this.addr+"'";
                        listPlayer = playerConnection.selectDataPlayer(query);
                        if(listPlayer.isEmpty() == false)
                        {
                            String namaUser = listPlayer.get(0);
                            int idx = namaUser.indexOf('|');
                            password = namaUser.substring(idx +1);
                        }
                        if (this.passwd.equals(password)) {
                            
                            command.setArgument("OK");
                        } else {
                            command.setArgument("FAILED");
                        }
                        this.sendobject(command);

                        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        this.date = new Date();
                        this.LoginTime = dateFormat.format(date);
                    } else if (request.getCommand().equals("CHAT") && request.getArgument().equals("ROOM")) {
                        room newRoom = null;
                        for (room room : rooms) {
                            if (room.getRoomName().equals(request.getRoomName())) {
                                newRoom = room;
                                newRoom.sendChat(this.addr + ": " + request.getChattext() + "\n");
                                
                                break;
                            }
                        }
                    } else if (request.getCommand().equals("LIST") && request.getArgument().equals("ROOM")) {
                        ArrayList<roomsAttr> userRooms = new ArrayList<>();
                        for(int i=0;i<rooms.size();i++)
                        {
                            if(rooms.get(i).users().isEmpty())rooms.remove(rooms.get(i));
                        }
                        for (int i = 0; i < rooms.size(); i++) {
                            
                            roomsAttr room =new roomsAttr();
                            room.setName(rooms.get(i).getRoomName());
                            room.setLevel(rooms.get(i).getLevel());
                            room.setNumberTracks(rooms.get(i).getnTracks());
                            room.setNumberPlayer(rooms.get(i).getNumberPlayer());
                            room.setMaxPlayer(rooms.get(i).getMaxPlayer());
                            room.setAvailableSlot();
                            userRooms.add(room);
                        }
                        this.sendobject(userRooms);

                    }
                    else if(request.getCommand().equals("JOIN") && request.getArgument().equals("ROOM"))
                    {
                        command=new cmd();
                        command.setCommand("JOIN");
                        for(int i=0;i<rooms.size();i++)
                        {
                            System.out.println(rooms.get(i).getRoomName() + " " + request.getRoomName());
                            if(rooms.get(i).getRoomName().equals(request.getRoomName()))
                            {
                                rooms.get(i).add(this);
                                command.setArgument("OK");
                                System.out.println("masuk");
                                break;
                            }
                        }
                        this.sendobject(command);
                    }
                    else if(request.getCommand().equals("ROOM") && request.getArgument().equals("MEMBER"))
                    {
                        for(int i=0;i<rooms.size();i++)
                        {
                            if(rooms.get(i).getRoomName().equals(request.getRoomName()))
                            {
                                ArrayList<String> member = new ArrayList<>();
                                for(int j=0;j<rooms.get(i).users().size();j++)
                                {
                                    member.add(rooms.get(i).users().get(j).getName());
                                }
                                this.sendobject(member);
                            }
                        }
                    }
                    else if(request.getCommand().equals("START") && request.getArgument().equals("GAME"))
                    {
                        for(int i=0;i<rooms.size();i++)
                        {
                            if(rooms.get(i).getRoomName().equals(request.getRoomName()))
                            {
                                command = new cmd();
                                for(int j=0;j<rooms.get(i).users().size();j++)
                                {
                                    command.setCommand("STARTING");
                                    command.setArgument("GAME");
                                    rooms.get(i).users().get(j).sendobject(command);
                                }
                                break;
                            }
                            
                        }
                    }
                    else if(request.getCommand().equals("UPDATE") && request.getArgument().equals("SCOREBOARD"))
                    {
                        ArrayList<PlayerStat> players=new ArrayList<>();
                        for(int i=0;i<rooms.size();i++)
                        {
                            if(rooms.get(i).getRoomName().equals(request.getRoomName()))
                            {
                                for(int j=0;j<rooms.get(i).users().size();j++)
                                {
                                    PlayerStat player=new PlayerStat();
                                    player.setName(rooms.get(i).users().get(j).getName());
                                    player.setSkor(rooms.get(i).users().get(j).getSkor());
                                    players.add(player);
                                }
                                sendobject(players);
                                break;
                            }
                        }
                    }
                    else if(request.getCommand().equals("START") && request.getArgument().equals("TRANSFER"))
                    {
                        int nTracks = 0;
                        int level = 0;
                        String roomName = null;
                        String lvl = null;
                        for(int i=0;i<rooms.size();i++)
                        {
                            if(rooms.get(i).getRoomName().equals(request.getRoomName()))
                            {
                                nTracks=rooms.get(i).getnTracks();
                                roomName=rooms.get(i).getRoomName();
                                if(rooms.get(i).getLevel().equals("EASY"))
                                {
                                    lvl="EASY";
                                    level=1;
                                }
                                else if(rooms.get(i).getLevel().equals("MEDIUM"))
                                {
                                    lvl="MEDIUM";
                                    level=2;
                                }
                                else if(rooms.get(i).getLevel().equals("HARD"))
                                {
                                    lvl="HARD";
                                    level=3;
                                }
                                break;
                            }
                        }
                        final int tracks =nTracks;
                        final int nlevel=level;
                        final String roomNames=roomName;
                        final String nlvl=lvl;
                        Thread transferFile= new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    
                                    String IP=sockcli.getInetAddress().toString().substring(0,0) + sockcli.getInetAddress().toString().substring(1);
                                    
                                    socketio sockfile=new socketio(IP,9001);
                                    roomsAttr command =new roomsAttr();
                                    command.setCommand("NUMBER");
                                    command.setArgument("SONG");
                                    command.setNumberTracks(tracks);
                                    sockfile.sendobject(command);
                                    ArrayList<ArrayList<String>> theSoal = new ArrayList<>();
                                    Soal_pilihan soal =new Soal_pilihan();
                                    soal.queryLagu(nlevel);
                                    
                                    for(int i=0;i<tracks;i++)
                                    {
                                        System.out.println(i);
                                        
                                        ArrayList<String> judulBuffer = new ArrayList<>();
                                        judulBuffer=soal.getListJudul(i);
                                        
                                        
                                        theSoal.add(judulBuffer);
                                        
                                        File fLagu=new File(soal.getPath());
                                        byte[] bytefile= new byte[10240];
                                        int byteread;
                                        file fileSend = new file();
                                        fileSend.setStatus("START");
                                        FileInputStream fis=new FileInputStream(fLagu);
                                        while((byteread=fis.read(bytefile))>=0)
                                        {
                                            fileSend.setFile(bytefile);
                                            sockfile.sendobject(fileSend);
                                        }
                                        fileSend.setStatus("FINISH");
                                        sockfile.sendobject(fileSend);
                                        
                                    }
                                    sockfile.sendobject(theSoal);
                                    Thread.sleep(1000);
                                    roomsAttr room = new roomsAttr();
                                    room.setCommand("GAME");
                                    room.setArgument("READY");
                                    
                                    room.setName(roomNames);
                                    room.setLevel(nlvl);
                                    sendobject(room);
                                    System.out.println(IP + " " + room);
                                    
                                    
                                } catch (IOException ex) {
                                    Logger.getLogger(handler.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(handler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        transferFile.start();
                    }
                }
                else if(o instanceof roomsAttr)
                {
                    roomsAttr roomAtr =(roomsAttr) o;
                    System.out.println(roomAtr.getCommand() + " " + roomAtr.getArgument());
                    if(roomAtr.getCommand().equals("CREATE") && roomAtr.getArgument().equals("ROOM"))
                    {
                         room room = new room(roomAtr.getName(),roomAtr.getLevel(),roomAtr.getNumberTracks(),roomAtr.getMaxPlayer());
                         room.add(this);
                         rooms.add(room);
                    }
                    
                }
                else if(o instanceof jawabanSoal)
                {
                    jawabanSoal answer= (jawabanSoal) o;
                    String jawaban= answer.getJawaban();
                    long time =answer.getWaktu()/1000;
                    Soal_pilihan soal =new Soal_pilihan();
                    
                    System.out.println(time);
                    if(!soal.getJawaban(jawaban).equals("null"))
                    {
                        skor+= 100-(time*5);
                        System.out.println(time +" " + skor);
                    }
                    else System.out.println("jawaban salah");
                }

            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("connection lost\n");
        synchronized (this.alhandler) {
            this.alhandler.remove(this);
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).users().contains(this)) {
                    rooms.get(i).remove(this);
                }
            }
        }

        System.out.println("remove thread\n");

        try {
            this.sockcli.close();
            this.objectoutput.close();
            this.objectinput.close();
        } catch (IOException ex) {
            Logger.getLogger(handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("end of connection\n");

    }
}
