/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectsend;

import java.io.Serializable;

/**
 *
 * @author Agus
 */
public class PlayerStat implements Serializable{
    private String name;
    private double skor;

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
     * @return the skor
     */
    public double getSkor() {
        return skor;
    }

    /**
     * @param skor the skor to set
     */
    public void setSkor(double skor) {
        this.skor = skor;
    }
}
