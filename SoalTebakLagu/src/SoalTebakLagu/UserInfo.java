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
        Soal soalA = new Soal();
        Soal soalB = new Soal();
        Soal soalC = new Soal();
        Soal soalD = new Soal();
        Soal soalE = new Soal();
        int i = 0;
        System.out.println("SOAL A : ");soalA.getListJudul(i++);
        System.out.println("SOAL B : ");soalB.getListJudul(i++);
        System.out.println("SOAL C : ");soalC.getListJudul(i++);
        System.out.println("SOAL D : ");soalD.getListJudul(i++);
        System.out.println("SOAL E : ");soalE.getListJudul(i++);
        connection.selectDataPlayer();
    }
    
}
