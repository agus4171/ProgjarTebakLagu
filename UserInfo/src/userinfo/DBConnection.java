/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinfo;

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
 * @author MAgus
 */
public class DBConnection {
    private Connection connection;
    private Statement stt;
    private ResultSet rstt;
    private String query;
    ArrayList<String> listLagu;
    
    public DBConnection()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tebaklagu", "root", "");
            stt = (Statement) connection.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*public void setDataPlayer()
    {
        query = "INSERT INTO player (ID_Player, Password) VALUER ('"+this.addr+"',)";
        try {
            rstt = stt.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    public ArrayList selectDataJudul()
    {
        query = "SELECT Judul FROM judul";
        //String queryTitle = "SELECT Title, Path, Status FROM lagu";
        //int random = 3;
        ArrayList listJudul = new ArrayList();
        try {
            setRstt(getStt().executeQuery(query));
            while(getRstt().next())
            {
                String judul = getRstt().getString("Judul");
                listJudul.add(judul);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listJudul;
    }
    
    public ArrayList selectDataLagu()
    {
        query = "SELECT Title FROM lagu";
        ArrayList listTitle = new ArrayList();
        try {
            setRstt(getStt().executeQuery(query));
            while(getRstt().next())
            {
                String title = getRstt().getString("Title");
                listTitle.add(title);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTitle;
    }
    
    /*public void selectDataPlayer()
    {
        query = "SELECT * FROM player WHERE ID_Player='"+this+"'";
        try {
            rstt = stt.executeQuery(query);
            while(rstt.next())
            {
                String passwd = rstt.getString("Passwd");
            }
            if(this.passwd == passwd)
            {
                System.out.println("berhasil login");
            }
            else
            {
                System.out.println("Your Username or Password Incorrect");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
            
    public  void getDataPlayer()
    {
        query = "SELECT * FROM player";
        try {
            setRstt(getStt().executeQuery(query));
            //System.out.println("record from database");
            while (getRstt().next())
            {
                String id = getRstt().getString("ID_Player");
                String passwd = getRstt().getString("Password");
                String score = getRstt().getString("Score");
                System.out.println("ID : "+id+" Passwd : "+passwd+" Score : "+score);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> getDataLagu()
    {
        query = "SELECT * FROM lagu";
        listLagu = new ArrayList();
        try {
            setRstt(getStt().executeQuery(query));
            while (getRstt().next()) { 
                String id = getRstt().getString("ID_Lagu");
                String titleLagu = getRstt().getString("Title");
                String pathLagu = getRstt().getString("Path");
                String statusLagu = getRstt().getString("Status");
                listLagu.add(titleLagu);
                //listLagu.add(titleLagu + pathLagu + statusLagu);
                //System.out.println("ID : "+id+" Judul : "+titleLagu+" Path : "+pathLagu+" Status : "+statusLagu);
            }
            System.out.println(listLagu);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listLagu;
    }

    /**
     * @return the stt
     */
    public Statement getStt() {
        return stt;
    }

    /**
     * @param stt the stt to set
     */
    public void setStt(Statement stt) {
        this.stt = stt;
    }

    /**
     * @return the rstt
     */
    public ResultSet getRstt() {
        return rstt;
    }

    /**
     * @param rstt the rstt to set
     */
    public void setRstt(ResultSet rstt) {
        this.rstt = rstt;
    }
}
