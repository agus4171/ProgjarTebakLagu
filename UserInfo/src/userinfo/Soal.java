/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinfo;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author MAgus
 */
public class Soal {
    private String path;
    private String status;
    private String jawaban;
    private ArrayList<String> listJudul;
    private ArrayList<String> listTempJudul;
    private ArrayList<String> listTempLagu;
    
    DBConnection connection;
    public Soal()
    {
        listJudul = new ArrayList<>();
        connection = new DBConnection();
    }
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

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
     * @return the listJudul
     */
    public ArrayList<String> getListJudul() {
        listTempJudul = connection.selectDataJudul();
        listTempLagu = connection.selectDataLagu();
        for (int i = 0; i < listTempJudul.size(); i++) {
                int index = new Random().nextInt(listTempJudul.size());
                String randomJudul = listTempJudul.get(index);
                
                if(listJudul.contains(randomJudul) == false && listJudul.size() < 3)
                    listJudul.add(randomJudul);   
            }
        int index = new Random().nextInt(listTempLagu.size());
        String randomLagu = listTempLagu.get(index);
        //System.out.println(randomLagu);    
        listJudul.add(randomLagu);
        System.out.println(listJudul);
        return listJudul;
    }

    /**
     * @param listJudul the listJudul to set
     */
    public void setListJudul(ArrayList<String> listJudul) {
        this.listJudul = listJudul;
    }
}
