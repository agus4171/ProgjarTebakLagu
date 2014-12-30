/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp_progjar_server;

import java.io.IOException;
import java.util.ArrayList;
import objectsend.cmd;

/**
 *
 * @author Administrator
 */
public class room {
   private ArrayList<handler> room;
   private String roomName;
   private String level;
   private int nTracks;
   private int maxPlayer;
   public room(String name,String level,int nTracks,int maxPlayer)
   {
       room=new ArrayList<>();
       this.roomName=name;
       this.level=level;
       this.nTracks=nTracks;
       this.maxPlayer=maxPlayer;
   }
   public void sendChat(String chatText) throws IOException
   {
       cmd chatMessage=new cmd();
       chatMessage.setChattext(chatText);
       chatMessage.setCommand("CHAT");
       chatMessage.setArgument("USER");
       for(int i=0;i<room.size();i++)
       {
           room.get(i).sendobject(chatMessage);
           
       }
   }
   public ArrayList<handler> users()
   {
       return this.room;
   }
   public int getNumberPlayer()
   {
       return users().size();
   }
   public String getRoomName()
   {
       return this.roomName;
   }
   public void add(handler user)
   {
       this.room.add(user);
   }
   public void remove(handler user)
   {
       this.room.remove(user);
   }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return the nTracks
     */
    public int getnTracks() {
        return nTracks;
    }

    /**
     * @param nTracks the nTracks to set
     */
    public void setnTracks(int nTracks) {
        this.nTracks = nTracks;
    }

    /**
     * @return the maxPlayer
     */
    public int getMaxPlayer() {
        return maxPlayer;
    }

    /**
     * @param maxPlayer the maxPlayer to set
     */
    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }
   
}
