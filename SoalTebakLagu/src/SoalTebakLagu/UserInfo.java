/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoalTebakLagu;

/**
 *
 * @author MAgus
 */
public class UserInfo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DBConnection connection = new DBConnection();
        Soal soal = new Soal();
        soal.getListJudul();
    }
    
}
