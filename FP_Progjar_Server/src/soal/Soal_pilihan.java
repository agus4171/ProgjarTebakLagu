/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soal;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Agus
 */
public class Soal_pilihan {
    private String status;
    private String jawaban;
    private File lagu;
    private ArrayList<String> listJudul;
    private ArrayList<String> listJudulRan;
    private ArrayList<String> listTempJudul;
    private ArrayList<String> listTempLagu;
    private String judulLagu;
    private String pathLagu;
    
    db_connection connection;
    
    public Soal_pilihan()
    {
        listJudul = new ArrayList<>();
        connection = new db_connection();
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
    
    public ArrayList<String> getListJudul(int nextSong, int level) {
        listTempJudul = connection.selectDataJudul();
        listTempLagu = connection.selectDataLagu(level);
        
        listJudulRan = new ArrayList<>();
        for (int i = 0; i < listTempJudul.size(); i++) {
            int index = new Random().nextInt(listTempJudul.size());
            String judulRan = listTempJudul.get(index);
            if(listJudulRan.contains(judulRan) == false && listJudulRan.size() < 3)
            {
                listJudulRan.add(judulRan);
            }
        }
        String randomLagu = listTempLagu.get(nextSong);
        int idx = randomLagu.indexOf('|');
        pathLagu = randomLagu.substring(idx +1);
        String judulLagu = randomLagu.substring(0, idx);
            System.out.println(judulLagu + " -> " + pathLagu);
        listJudulRan.add(judulLagu);
        
        lagu = new File(pathLagu);
        //System.out.println(lagu.getAbsolutePath());
        //System.out.println("ini list lagu pilihan : "+listJudulRan.size()+listJudulRan);
        
        while (listJudul.size() < 4) {
            for (int i = 0; i < listJudulRan.size(); i++) {
                int index = new Random().nextInt(listJudulRan.size());
                String randomJudul = listJudulRan.get(index);
                if(listJudul.contains(randomJudul) == false && listJudul.size() < 4)
                {
                    listJudul.add(randomJudul);
                }
            }   
        }
        System.out.println("OPTION : "+listJudul.size()+listJudul);
        return listJudul;
    }
    
    public String getPath()
    {
        return this.lagu.getAbsolutePath();
    }

    /**
     * @param listJudul the listJudul to set
     */
    public void setListJudul(ArrayList<String> listJudul) {
        this.listJudul = listJudul;
    }
    
}
