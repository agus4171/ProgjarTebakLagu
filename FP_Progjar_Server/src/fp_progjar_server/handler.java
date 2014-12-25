/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp_progjar_server;

//import SoalTebakLagu.DBConnection;
import objectsend.cmd;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.DocFlavor;
import objectsend.roomsAttr;
import soal.db_connection;

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
                    if(request.getCommand().equals("DUMMY"))this.sendobject(o);
                    System.out.println(request.getCommand() + " " + request.getArgument());
                    if (request.getCommand().equals("LOGIN")) {
                        command = new cmd();
                        command.setCommand("LOGIN");

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
