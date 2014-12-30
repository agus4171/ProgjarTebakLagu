/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soal;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agus
 */
public class db_connection {
    private Connection connection;
    private Statement stt;
    private ResultSet rstt;
    private String query;
    
    public db_connection()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tebaklagu", "root", "");
            stt = (Statement) connection.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public ArrayList selectDataJudul()
    {
        query = "SELECT Judul FROM judul";
        ArrayList<String> listJudul = new ArrayList();
        try {
            rstt = stt.executeQuery(query);
            while(rstt.next())
            {
                String judul = rstt.getString("Judul");
                listJudul.add(judul);
            }
        } catch (SQLException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listJudul; 
    }
    
    public ArrayList<String> selectDataLagu(int Status)
    {
    
        query = "SELECT Title, Path FROM lagu WHERE Status = '"+Status+"' LIMIT  20";
        ArrayList<String> listTitle = new ArrayList();
        //System.out.println(query);
        try {
            rstt = stt.executeQuery(query);
            while(rstt.next())
            {
                String title = rstt.getString("Title");
                String path = rstt.getString("Path");
                listTitle.add(title+"|"+path);
            }
        } catch (SQLException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTitle;
    }
    
    public String pilihJudul(String pilihLagu)
    {
        query = "SELECT Title FROM lagu WHERE Title = \""+pilihLagu+"\"";
        String title = "null";
        try {
            rstt = stt.executeQuery(query);
            while(rstt.next())
            {
                //rstt = stt.executeQuery(query);
                title = rstt.getString("Title");
            }
        } catch (SQLException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return title;
    }
    
    public ArrayList selectDataPlayer(String query)
    {
        //query = "SELECT * FROM player";
        ArrayList<String> listID_Player = new ArrayList();
        try {
            rstt = stt.executeQuery(query);
            while(rstt.next())
            {
                String idPlayer = rstt.getString("ID_Player");
                String passwd = rstt.getString("Password");
                String score = rstt.getString("Score");
                listID_Player.add(idPlayer+"|"+passwd);
            }
            //System.out.println(listID_Player);
        } catch (SQLException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listID_Player;
    }
            
    public  void getDataPlayer()
    {
        query = "SELECT * FROM player";
        try {
            rstt = stt.executeQuery(query);
            while (rstt.next())
            {
                String id = rstt.getString("ID_Player");
                String passwd = rstt.getString("Password");
                String score = rstt.getString("Score");
                System.out.println("ID : "+id+" Passwd : "+passwd+" Score : "+score);
            }
        } catch (SQLException ex) {
            Logger.getLogger(db_connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
