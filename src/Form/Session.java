/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

/**
 *
 * @author Totok Kurniawan
 */
public class Session {

    public static String getBln() {
        return bln;
    }

    public static void setBln(String bln) {
        Session.bln = bln;
    }
private static String bln;
    public static int getTotal_harga() {
        return total_harga;
    }

    public static void setTotal_harga(int total_harga) {
        Session.total_harga = total_harga;
    }
    private static int total_harga;

    public static String getU_id() {
        return U_id;
    }

    public static void setU_id(String U_id) {
        Session.U_id = U_id;
    }

    public static String getU_username() {
        return U_username;
    }

    public static void setU_username(String U_username) {
        Session.U_username = U_username;
    }

    public static String getU_password() {
        return U_password;
    }

    public static void setU_password(String U_password) {
        Session.U_password = U_password;
    }
    private static String U_id;

    public static String getU_NamaLengkap() {
        return U_NamaLengkap;
    }

    public static void setU_NamaLengkap(String U_NamaLengkap) {
        Session.U_NamaLengkap = U_NamaLengkap;
    }
    private static String U_NamaLengkap;
    private static String U_username;
    private static String U_password;
    private static String U_KodeStruk;

    public static String getU_KodeStruk() {
        return U_KodeStruk;
    }

    public static void setU_KodeStruk(String U_KodeStruk) {
        Session.U_KodeStruk = U_KodeStruk;
    }

    public static String getU_Nopol() {
        return U_Nopol;
    }

    public static void setU_Nopol(String U_Nopol) {
        Session.U_Nopol = U_Nopol;
    }
    private static String U_Nopol;

    public static String getU_datapelanggan() {
        return U_datapelanggan;
    }

    public static void setU_datapelanggan(String U_datapelanggan) {
        Session.U_datapelanggan = U_datapelanggan;
    }
    private static String U_datapelanggan;

    public static String getU_datamobil() {
        return U_datamobil;
    }

    public static void setU_datamobil(String U_datamobil) {
        Session.U_datamobil = U_datamobil;
    }
    private static String U_datamobil;

    public static String getU_filename() {
        return U_filename;
    }

    public static void setU_filename(String U_filename) {
        Session.U_filename = U_filename;
    }
    private static String U_filename;

    static Object getBarcode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   

}
