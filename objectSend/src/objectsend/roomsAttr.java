/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectsend;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class roomsAttr implements Serializable{
    private String command;
    private String argument;
    private String name;
    private int numberTracks;
    private String level;
    private int numberPlayer;
    private int maxPlayer;
    private String availableSlot;
    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }
    public void setAvailableSlot()
    {
        availableSlot=new String(numberPlayer + "/" + maxPlayer);
    }
    public String getAvailableSlot()
    {
        return availableSlot;
    }
    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * @param argument the argument to set
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the numberTracks
     */
    public int getNumberTracks() {
        return numberTracks;
    }

    /**
     * @param numberTracks the numberTracks to set
     */
    public void setNumberTracks(int numberTracks) {
        this.numberTracks = numberTracks;
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
     * @return the numberPlayer
     */
    public int getNumberPlayer() {
        return numberPlayer;
    }

    /**
     * @param numberPlayer the numberPlayer to set
     */
    public void setNumberPlayer(int numberPlayer) {
        this.numberPlayer = numberPlayer;
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
