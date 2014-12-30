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
public class jawabanSoal implements Serializable{
    private String jawaban;
    private long Waktu;

    /**
     * @return the jawaban
     */
    public String getJawaban() {
        return jawaban;
    }

    /**
     * @param jawaban the jawaban to set
     */
    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }

    /**
     * @return the Waktu
     */
    public long getWaktu() {
        return Waktu;
    }

    /**
     * @param Waktu the Waktu to set
     */
    public void setWaktu(long Waktu) {
        this.Waktu = Waktu;
    }
}
