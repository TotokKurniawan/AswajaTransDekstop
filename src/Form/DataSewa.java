/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form;

/**
 *
 * @author Totok Kurniawan
 */
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import java.util.*;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class DataSewa extends javax.swing.JFrame {

    /**
     * Creates new form Logout
     */
//    DefaultTableModel model1;
    String Imgpath;
    Float Byarx;
    HashMap param = new HashMap();

    ArrayList<String> idList = new ArrayList<String>();

    public DataSewa() {
        initComponents();
        loaddata();
        Tampil_Jam();
        Tampil_Tanggal();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        TampilFoto();
    }
  
public void TampilFoto() {
    try {
        String sql = "SELECT Foto FROM user WHERE id = ?";
        Connection conn = Koneksi.Connect.KoneksiDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, Session.getU_id());
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String fotoFilename = rs.getString("Foto");
            if (fotoFilename == null) {
                // Jika fotoFilename null, tampilkan ikon default
                ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/Gambar/profile.png"));
                txtfoto.setIcon(defaultIcon);
            } else {
                File fotoFile = new File(fotoFilename);

                if (fotoFile.exists()) {
                    ImageIcon imageIcon = new ImageIcon(fotoFile.getAbsolutePath());
                    Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
                    txtfoto.setIcon(new ImageIcon(image));
                } else {
                    // Jika file gambar tidak ditemukan, tampilkan ikon default
                    ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/Gambar/profile.png"));
                    txtfoto.setIcon(defaultIcon);
                    JOptionPane.showMessageDialog(null, "File gambar tidak ditemukan");
                }
            }
        } else {
            // Jika tidak ada data foto yang ditemukan, tampilkan ikon default
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/Gambar/profile.png"));
            txtfoto.setIcon(defaultIcon);
        }

        rs.close();
        pst.close();
        conn.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam mengakses database: " + e.getMessage());
    }
}
      public void Tampil_Jam(){
        ActionListener taskPerformer = new ActionListener() {
 
        @Override
            public void actionPerformed(ActionEvent evt) {
            String nol_jam = "", nol_menit = "",nol_detik = "";
 
            java.util.Date dateTime = new java.util.Date();
            int nilai_jam = dateTime.getHours();
            int nilai_menit = dateTime.getMinutes();
            int nilai_detik = dateTime.getSeconds();
 
            if(nilai_jam <= 9) nol_jam= "0";
            if(nilai_menit <= 9) nol_menit= "0";
            if(nilai_detik <= 9) nol_detik= "0";
 
            String jam = nol_jam + Integer.toString(nilai_jam);
            String menit = nol_menit + Integer.toString(nilai_menit);
            String detik = nol_detik + Integer.toString(nilai_detik);
 
        waktu.setText(jam+":"+menit+":"+detik+"");
            }
        };
    new javax.swing.Timer(1000, taskPerformer).start();
    }   
 
    public void Tampil_Tanggal() {
        java.util.Date tglsekarang = new java.util.Date();
        SimpleDateFormat smpdtfmt = new SimpleDateFormat("dd MMMMMMMMM yyyy", Locale.getDefault());
        String tanggal1 = smpdtfmt.format(tglsekarang);
        tgl1.setText(tanggal1);
    }

    public void loaddata() {
        DefaultTableModel model1 = new DefaultTableModel();
        model1.addColumn("Kode Struk");
        model1.addColumn("NIK");
        model1.addColumn("Nama Pelanggan");
        model1.addColumn("ID Admin");
        model1.addColumn("Nama Admin");
        model1.addColumn("Merk Mobil");
        model1.addColumn("Tanggal Rental");
        model1.addColumn("Tanggal Kembali");
        model1.addColumn("Tanggal Pengembalian");
        model1.addColumn("Total Harga");
        model1.addColumn("Total Sewa");
        model1.addColumn("Bayar");
        model1.addColumn("Sisa Bayar");
        model1.addColumn("Status Bayar");
        model1.addColumn("Denda");
        model1.addColumn("Status Mobil");
        tabeldatasewa.setModel(model1);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
            Statement stat = conn.createStatement();
            String sql = "SELECT sewa.*, pelanggan.NIK, pelanggan.Nama_Pelanggan,user.id,  user.Nama,  mobil.MerkMobil,detail_sewa.Tgl_Kembali,detail_sewa.Denda,detail_sewa.tanggal_pengembalian,mobil.Status\n"
                    + "FROM sewa\n"
                    + "JOIN pelanggan ON pelanggan.NIK = sewa.NIK\n"
                    + "JOIN user ON user.id = sewa.id\n"
                    + "JOIN detail_sewa ON detail_sewa.id_sewa = sewa.id_sewa\n"
                    + "JOIN mobil ON mobil.Nopol = detail_sewa.Nopol\n where mobil.Status ='mobil sedang disewa'";
            ResultSet res = stat.executeQuery(sql);

            while (res.next()) {
                Object[] obj = new Object[16];
                obj[0] = res.getString("id_Sewa");
                obj[1] = res.getString("NIK");
                obj[2] = res.getString("Nama_Pelanggan");
                obj[3] = res.getString("id");
                obj[4] = res.getString("Nama");
                obj[5] = res.getString("MerkMobil");
                obj[6] = res.getString("Tgl_sewa");
                obj[7] = res.getString("Tgl_Kembali");
                obj[8] = res.getString("tanggal_pengembalian");
                obj[9] = res.getString("Total_Harga");
                obj[10] = res.getString("Total_Sewa");
                obj[11] = res.getString("bayar");
                obj[12] = res.getString("Sisa yang harus dibayar");
                obj[13] = res.getString("StatusBayar");
                obj[14] = res.getString("Denda");
                obj[15] = res.getString("Status");
                model1.addRow(obj);
            }
            tabeldatasewa.setModel(model1);
             int jumlahsew = tabeldatasewa.getRowCount();
            jumlah.setText("" + jumlahsew);

        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }

    public void cari() {
        DefaultTableModel model = (DefaultTableModel) tabeldatasewa.getModel();
        model.getDataVector().removeAllElements();
        String nggolek1 = "SELECT sewa.*, pelanggan.NIK, pelanggan.Nama_Pelanggan, user.id, user.Nama, mobil.Nopol, mobil.MerkMobil, detail_sewa.Tgl_Kembali, detail_sewa.Denda, mobil.Status\n"
                + "FROM sewa\n"
                + "JOIN pelanggan ON pelanggan.NIK = sewa.NIK\n"
                + "JOIN user ON user.id = sewa.id\n"
                + "JOIN detail_sewa ON detail_sewa.id_sewa = sewa.id_sewa\n"
                + "JOIN mobil ON mobil.Nopol = detail_sewa.Nopol\n"
                + "WHERE MerkMobil LIKE '%" + cari.getText()
                + "%' OR TypeMobil LIKE '%" + cari.getText()
                + "%' OR Harga LIKE '%" + cari.getText()
                + "%' OR Nama_Pelanggan LIKE '%" + cari.getText()
                + "%'";

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(nggolek1);

            while (res.next()) {
                Object[] obj = new Object[16];
                obj[0] = res.getString("id_Sewa");
                obj[1] = res.getString("NIK");
                obj[2] = res.getString("Nama_Pelanggan");
                obj[3] = res.getString("id");
                obj[4] = res.getString("Nama");
                obj[5] = res.getString("Nopol");
                obj[6] = res.getString("MerkMobil");
                obj[7] = res.getString("Tgl_sewa");
                obj[8] = res.getString("Tgl_Kembali");
                obj[9] = res.getString("Total_Harga");
                obj[10] = res.getString("Total_Sewa");
                obj[11] = res.getString("bayar");
                obj[12] = res.getString("Sisa yang harus dibayar");
                obj[13] = res.getString("StatusBayar");
                obj[14] = res.getString("Denda");
                obj[15] = res.getString("Status");
                model.addRow(obj);
            }
            int jumlahmobil = tabeldatasewa.getRowCount();
            jumlah.setText("" + jumlahmobil);
            tabeldatasewa.setModel(model);
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }

//    public void loaddata() {
//        try {
//            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
//            Statement stat = conn.createStatement();
//            String sql = "SELECT sewa.*, pelanggan.NIK, pelanggan.Nama_Pelanggan, user.id, user.Nama, mobil.Nopol, mobil.MerkMobil\n"
//                    + "FROM sewa\n"
//                    + "JOIN pelanggan ON pelanggan.NIK = sewa.NIK\n"
//                    + "JOIN user ON user.id = sewa.id_user\n"
//                    + "JOIN detail_sewa ON detail_sewa.id_sewa = sewa.id_sewa\n"
//                    + "JOIN mobil ON mobil.Nopol = detail_sewa.Nopol";
//            ResultSet res = stat.executeQuery(sql);
//    
//            while (res.next()) {
//                Object[] obj = new Object[15];
//                obj[0] = res.getString("id_Sewa");
//                obj[1] = res.getString("NIK");
//                obj[2] = res.getString("Nama_Pelanggan");
//                obj[3] = res.getString("id");
//                obj[4] = res.getString("Nama");
//                obj[5] = res.getString("Nopol");
//                obj[6] = res.getString("Tgl_sewa");
//                obj[7] = res.getString("Tgl_Kembali");
//                obj[8] = res.getString("Total_Harga");
//                obj[9] = res.getString("Total_Sewa");
//                obj[10] = res.getString("bayar");
//                obj[11] = res.getString("Sisa yang harus dibayar");
//                obj[12] = res.getString("StatusBayar");
//                obj[13] = res.getString("Denda");
//                obj[14] = res.getString("StatusMobil");
//                ;
//                model1.addRow(obj);
//            }
//
//        } catch (SQLException err) {
//            JOptionPane.showMessageDialog(null, err.getMessage());
//        }
//    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabeldatasewa = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTotalHarga = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        sis_byr = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        TXT_Kembalian = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        nik = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        by = new javax.swing.JTextField();
        stby = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        Kembali = new rojerusan.RSMaterialButtonRectangle();
        Kembali1 = new rojerusan.RSMaterialButtonRectangle();
        Detailsewa = new rojerusan.RSMaterialButtonRectangle();
        tgl_kbl = new javax.swing.JTextField();
        tgl_rental = new javax.swing.JTextField();
        kd_nt = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        totalsewa = new javax.swing.JTextField();
        cari = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jumlah = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tgl1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        Refresh = new rojerusan.RSMaterialButtonRectangle();
        jButton1 = new javax.swing.JButton();
        Side_Bar = new javax.swing.JPanel();
        Dashboard = new javax.swing.JButton();
        DatMob1 = new javax.swing.JButton();
        DatPel1 = new javax.swing.JButton();
        DatMob2 = new javax.swing.JButton();
        TranSew1 = new javax.swing.JButton();
        DatTranSew1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        Log1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtfoto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 69, 1156, 16));

        jLabel19.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel19.setText("Data Sewa Mobil");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(451, 103, -1, -1));

        tabeldatasewa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        }

    );
    tabeldatasewa.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tabeldatasewaMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(tabeldatasewa);

    jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 1100, 224));

    jLabel12.setText("Bayar");
    jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 490, -1, -1));

    jLabel29.setText(":");
    jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 540, 3, -1));

    jLabel13.setText("Status Bayar");
    jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 540, -1, -1));

    txtTotalHarga.setEnabled(false);
    txtTotalHarga.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtTotalHargaActionPerformed(evt);
        }
    });
    jPanel1.add(txtTotalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 450, 149, -1));

    jLabel14.setText("NIK");
    jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 450, -1, -1));

    sis_byr.setEnabled(false);
    sis_byr.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sis_byrActionPerformed(evt);
        }
    });
    jPanel1.add(sis_byr, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 540, 149, -1));

    jLabel15.setText("Sisa yang harus dibayar");
    jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 540, -1, -1));

    TXT_Kembalian.setEnabled(false);
    TXT_Kembalian.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            TXT_KembalianActionPerformed(evt);
        }
    });
    jPanel1.add(TXT_Kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 580, 149, -1));

    jLabel16.setText("Kembalian");
    jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 580, -1, -1));

    jLabel17.setText(":");
    jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 490, -1, -1));

    jLabel20.setText(":");
    jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 530, -1, -1));

    jLabel21.setText(":");
    jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 450, 3, -1));

    jLabel22.setText(":");
    jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 580, -1, -1));

    id.setEnabled(false);
    id.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            idActionPerformed(evt);
        }
    });
    jPanel1.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 490, 149, -1));

    jLabel23.setText(":");
    jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 490, -1, -1));

    jLabel8.setText("Id Admin");
    jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 490, -1, -1));

    jLabel24.setText(":");
    jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 540, -1, -1));

    jLabel26.setText("Total Sewa");
    jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 490, -1, -1));

    jLabel10.setText("Tanggal Rental");
    jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 530, -1, -1));

    nik.setEnabled(false);
    nik.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            nikActionPerformed(evt);
        }
    });
    jPanel1.add(nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 450, 149, -1));

    jLabel11.setText("Tanggal Kembali");
    jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 580, -1, -1));

    by.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            byActionPerformed(evt);
        }
    });
    by.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            byKeyReleased(evt);
        }
    });
    jPanel1.add(by, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 490, 149, -1));

    stby.setEnabled(false);
    stby.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            stbyActionPerformed(evt);
        }
    });
    jPanel1.add(stby, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 530, 149, -1));

    jLabel27.setText(":");
    jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 490, 3, -1));

    jLabel28.setText(":");
    jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 490, 3, -1));

    jLabel7.setFont(new java.awt.Font("Arial Black", 3, 36)); // NOI18N
    jLabel7.setText("Welcome to Re-Mob");
    jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

    jLabel32.setText(":");
    jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 580, 3, -1));

    Kembali.setBackground(new java.awt.Color(153, 153, 153));
    Kembali.setForeground(new java.awt.Color(51, 51, 51));
    Kembali.setText("Kembali");
    jPanel1.add(Kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 650, 102, 43));

    Kembali1.setBackground(new java.awt.Color(51, 255, 51));
    Kembali1.setForeground(new java.awt.Color(51, 51, 51));
    Kembali1.setText("Simpan");
    Kembali1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Kembali1ActionPerformed(evt);
        }
    });
    jPanel1.add(Kembali1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 650, 102, 43));

    Detailsewa.setBackground(new java.awt.Color(0, 255, 204));
    Detailsewa.setForeground(new java.awt.Color(51, 51, 51));
    Detailsewa.setText("Detail");
    Detailsewa.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DetailsewaActionPerformed(evt);
        }
    });
    jPanel1.add(Detailsewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 650, 102, 43));

    tgl_kbl.setEnabled(false);
    jPanel1.add(tgl_kbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 150, -1));

    tgl_rental.setEnabled(false);
    jPanel1.add(tgl_rental, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 530, 150, -1));

    kd_nt.setEnabled(false);
    kd_nt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            kd_ntActionPerformed(evt);
        }
    });
    jPanel1.add(kd_nt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 450, 149, -1));

    jLabel30.setText(":");
    jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, -1, -1));

    jLabel25.setText("Kode Struk");
    jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, -1, -1));

    jLabel35.setText("Total Harga");
    jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 450, -1, -1));

    jLabel36.setText(":");
    jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 450, 3, -1));

    totalsewa.setEnabled(false);
    totalsewa.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            totalsewaActionPerformed(evt);
        }
    });
    jPanel1.add(totalsewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 490, 149, -1));

    cari.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            cariKeyReleased(evt);
        }
    });
    jPanel1.add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 200, -1));

    jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
    jLabel5.setText("Pencarian :");
    jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

    jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
    jLabel9.setText("Jumlah Data Sewa :");
    jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, -1, -1));

    jumlah.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
    jumlah.setText("0");
    jPanel1.add(jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 170, -1, -1));

    jPanel3.setBackground(new java.awt.Color(255, 255, 255));
    jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    tgl1.setBackground(new java.awt.Color(255, 255, 255));
    tgl1.setText("Tanggal Sekarang :");
    jPanel3.add(tgl1);

    jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 180, 30));

    jPanel2.setBackground(new java.awt.Color(255, 255, 255));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    waktu.setBackground(new java.awt.Color(255, 255, 255));
    waktu.setText("Waktu Sekarang :");
    jPanel2.add(waktu);

    jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 90, 180, 30));

    Refresh.setBackground(new java.awt.Color(255, 255, 51));
    Refresh.setForeground(new java.awt.Color(51, 51, 51));
    Refresh.setText("Refresh");
    Refresh.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            RefreshActionPerformed(evt);
        }
    });
    jPanel1.add(Refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 650, 102, 43));

    jButton1.setText("HITUNG");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 580, -1, -1));

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 1150, 750));

    Side_Bar.setBackground(new java.awt.Color(105, 105, 105));
    Side_Bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    Dashboard.setBackground(java.awt.SystemColor.controlLtHighlight);
    Dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/dashboard.png"))); // NOI18N
    Dashboard.setText("Dashboard");
    Dashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    Dashboard.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DashboardActionPerformed(evt);
        }
    });
    Side_Bar.add(Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 182, 182, -1));

    DatMob1.setBackground(java.awt.SystemColor.controlLtHighlight);
    DatMob1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/data mobil.png"))); // NOI18N
    DatMob1.setText("Data Mobil");
    DatMob1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DatMob1.setPreferredSize(new java.awt.Dimension(87, 22));
    DatMob1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DatMob1ActionPerformed(evt);
        }
    });
    Side_Bar.add(DatMob1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 182, 45));

    DatPel1.setBackground(java.awt.SystemColor.controlLtHighlight);
    DatPel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/pelanggan.png"))); // NOI18N
    DatPel1.setText("Data Pelanggan");
    DatPel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DatPel1.setPreferredSize(new java.awt.Dimension(87, 22));
    DatPel1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DatPel1ActionPerformed(evt);
        }
    });
    Side_Bar.add(DatPel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 182, 46));

    DatMob2.setBackground(java.awt.SystemColor.controlLtHighlight);
    DatMob2.setIcon(new javax.swing.ImageIcon("C:\\Users\\ndraa\\Documents\\car_repair_16784.png")); // NOI18N
    DatMob2.setText("Pengeluaran");
    DatMob2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DatMob2.setPreferredSize(new java.awt.Dimension(87, 22));
    DatMob2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DatMob2ActionPerformed(evt);
        }
    });
    Side_Bar.add(DatMob2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 182, 45));

    TranSew1.setBackground(java.awt.SystemColor.controlLtHighlight);
    TranSew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/sewa mobil_1.png"))); // NOI18N
    TranSew1.setText("Sewa Mobil");
    TranSew1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    TranSew1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            TranSew1ActionPerformed(evt);
        }
    });
    Side_Bar.add(TranSew1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 182, -1));

    DatTranSew1.setBackground(java.awt.SystemColor.controlLtHighlight);
    DatTranSew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/pengembalian.png"))); // NOI18N
    DatTranSew1.setText("Data Sewa Mobil");
    DatTranSew1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DatTranSew1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DatTranSew1ActionPerformed(evt);
        }
    });
    Side_Bar.add(DatTranSew1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 182, -1));

    jButton11.setBackground(java.awt.SystemColor.controlLtHighlight);
    jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/laporan.png"))); // NOI18N
    jButton11.setText("Laporan");
    jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jButton11.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton11ActionPerformed(evt);
        }
    });
    Side_Bar.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 182, -1));

    Log1.setBackground(java.awt.SystemColor.controlLtHighlight);
    Log1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Logout.png"))); // NOI18N
    Log1.setText("Logout");
    Log1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    Log1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Log1ActionPerformed(evt);
        }
    });
    Side_Bar.add(Log1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 670, 182, -1));

    jLabel6.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(255, 255, 255));
    jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis panjang.png"))); // NOI18N
    Side_Bar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 0, -1, 756));

    txtfoto.setFont(new java.awt.Font("Arial Black", 3, 18)); // NOI18N
    txtfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    txtfoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/profile.png"))); // NOI18N
    Side_Bar.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

    getContentPane().add(Side_Bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 750));

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalHargaActionPerformed

    private void sis_byrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sis_byrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sis_byrActionPerformed

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void nikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nikActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nikActionPerformed

    private void byActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_byActionPerformed

    private void DetailsewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DetailsewaActionPerformed
        // TODO add your handling code here:
        if (Session.getU_KodeStruk() != null) {
            new DetailTransaksiSewa().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih salah satu data");
        }
    }//GEN-LAST:event_DetailsewaActionPerformed

    private void kd_ntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_ntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_ntActionPerformed

    private void totalsewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalsewaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalsewaActionPerformed

    private void byKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_byKeyReleased
    // TODO add your handling code here:
 


    }//GEN-LAST:event_byKeyReleased

    private void Kembali1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Kembali1ActionPerformed
        // TODO add your handling code here:
        String KD = kd_nt.getText();
        int byr = Integer.parseInt(by.getText());
        int sisa = Integer.parseInt(sis_byr.getText());
        String Status_byr = stby.getText();
        int adminn = Integer.parseInt(id.getText());

        try {
            String sql9 = "UPDATE sewa SET bayar ='" + byr + "', `Sisa yang harus dibayar` ='" + sisa + "', StatusBayar ='" + Status_byr + "' WHERE id_Sewa ='" + KD + "'";
            Connection conn = Koneksi.Connect.KoneksiDB();

            Statement s = conn.createStatement();
            s.executeUpdate(sql9);

            loaddata();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan ");

            s.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

//        String KD = kd_nt.getText();
//        int byr = Integer.parseInt(by.getText());
//        int sisa = Integer.parseInt(sis_byr.getText());
//        String Status_byr = stby.getText();
//        int adminn = Integer.parseInt(id.getText());
//        
//            try {
//            java.sql.Connection conn = Koneksi.Connect.KoneksiDB();
//            String sql = "sewa SET id_Sewa = ?, bayar = ?, sisa yang harus dibayar = ?, StatusBayar = ?, ";
//            PreparedStatement pst1 = conn.prepareStatement(sql);
//            pst1.setString(1, KD);
//            pst1.setInt(2, byr);
//            pst1.setInt(3, sisa);
//            pst1.setString(4, stby.getText());
//            pst1.executeUpdate();
//            
//            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
//            pst1.close();
//
////            String sql = "Update sewa set id_user ='"+adminn+"',bayar='"+byr+"',Sisa yang harus dibayar='"+sisa+"',StatusBayar='"+Status_byr+"'where id_user='"
//            loaddata();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e.getMessage());
//        }
//        
    }//GEN-LAST:event_Kembali1ActionPerformed

    private void stbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stbyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stbyActionPerformed

    private void TXT_KembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXT_KembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXT_KembalianActionPerformed

    private void tabeldatasewaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabeldatasewaMouseClicked
        // TODO add your handling code here:
        int baris = tabeldatasewa.rowAtPoint(evt.getPoint());
        String kode = tabeldatasewa.getValueAt(baris, 0).toString();
        kd_nt.setText(kode);
        Session.setU_KodeStruk(kode);
        String NIK = tabeldatasewa.getValueAt(baris, 1).toString();
        nik.setText(NIK);

        String admin = tabeldatasewa.getValueAt(baris, 3).toString();
        id.setText(admin);

        String sisaba = tabeldatasewa.getValueAt(baris, 12).toString();
        sis_byr.setText(sisaba);

        String tanggal_rental = tabeldatasewa.getValueAt(baris, 6).toString();
        tgl_rental.setText(tanggal_rental);

        String tanggal_kembali = tabeldatasewa.getValueAt(baris, 7).toString();
        tgl_kbl.setText(tanggal_kembali);

        String Total_Harga = tabeldatasewa.getValueAt(baris, 9).toString();
        txtTotalHarga.setText(Total_Harga);

        String Total_Sewa = tabeldatasewa.getValueAt(baris, 10).toString();
        totalsewa.setText(Total_Sewa);
    }//GEN-LAST:event_tabeldatasewaMouseClicked

    private void cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyReleased
        cari();
        // TODO add your handling code here:
    }//GEN-LAST:event_cariKeyReleased

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        Dashboard menu = new Dashboard();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void DatMob1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatMob1ActionPerformed
        // TODO add your handling code here:
        try {
            DataMobil1 mob1 = new DataMobil1();
            mob1.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(DataMobil1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_DatMob1ActionPerformed

    private void DatPel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatPel1ActionPerformed
        // TODO add your handling code here:
        DataPelanggan DatPel = null;
        try {
            DatPel = new DataPelanggan();
        } catch (SQLException ex) {
            Logger.getLogger(DataPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        DatPel.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DatPel1ActionPerformed

    private void DatMob2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatMob2ActionPerformed
        // TODO add your handling code here:

        try {
            new Pengeluaran().setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Pengeluaran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_DatMob2ActionPerformed

    private void TranSew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TranSew1ActionPerformed
        // TODO add your handling code here:
        try {
            new TransaksiSewa().setVisible(true);
                    this.dispose();

        } catch (SQLException ex) {
            Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TranSew1ActionPerformed

    private void DatTranSew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatTranSew1ActionPerformed
        // TODO add your handling code here:
        DataSewa DatTranSew = new DataSewa();
        DatTranSew.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DatTranSew1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Laporan lap = new Laporan();
        lap.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void Log1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Log1ActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            Login log = new Login();
            log.setVisible(true);
        }
    }//GEN-LAST:event_Log1ActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        // TODO add your handling code here:
        kd_nt.setText("");
        id.setText("");
        tgl_rental.setText("");
        tgl_kbl.setText("");
        nik.setText("");
        by.setText("");
        txtTotalHarga.setText("");
        TXT_Kembalian.setText("");
        stby.setText("");
        sis_byr.setText("");
        totalsewa.setText("");
    }//GEN-LAST:event_RefreshActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
String bayrText = by.getText();
String sisText = sis_byr.getText();

if (!bayrText.isEmpty() && !sisText.isEmpty()) {
    int bayr = Integer.parseInt(bayrText);
    int sis = Integer.parseInt(sisText);

    int fungsi = sis - bayr;
    int lain = bayr - sis;

    if (fungsi == 0) {
        stby.setText("lunas");
        sis_byr.setText(String.valueOf(fungsi));
        TXT_Kembalian.setText(String.valueOf(fungsi));
    } else if (bayr > sis) {
        stby.setText("lunas");
        sis_byr.setText("0");
        TXT_Kembalian.setText(String.valueOf(lain));
    } else if (fungsi == sis) {
        stby.setText("belum bayar");
        sis_byr.setText(String.valueOf(fungsi));
        TXT_Kembalian.setText("0");
    } else if (fungsi < sis) {
        stby.setText("kurang");
        sis_byr.setText(String.valueOf(fungsi));
        TXT_Kembalian.setText("0");
    }
} else {
    // Handle input kosong atau tidak valid
    // Tambahkan kode yang sesuai di sini
}

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DataSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataSewa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DatMob1;
    private javax.swing.JButton DatMob2;
    private javax.swing.JButton DatPel1;
    private javax.swing.JButton DatTranSew1;
    private rojerusan.RSMaterialButtonRectangle Detailsewa;
    private rojerusan.RSMaterialButtonRectangle Kembali;
    private rojerusan.RSMaterialButtonRectangle Kembali1;
    private javax.swing.JButton Log1;
    private rojerusan.RSMaterialButtonRectangle Refresh;
    private javax.swing.JPanel Side_Bar;
    private javax.swing.JTextField TXT_Kembalian;
    private javax.swing.JButton TranSew1;
    private javax.swing.JTextField by;
    private javax.swing.JTextField cari;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jumlah;
    private javax.swing.JTextField kd_nt;
    private javax.swing.JTextField nik;
    private javax.swing.JTextField sis_byr;
    private javax.swing.JTextField stby;
    private javax.swing.JTable tabeldatasewa;
    private javax.swing.JLabel tgl1;
    private javax.swing.JTextField tgl_kbl;
    private javax.swing.JTextField tgl_rental;
    private javax.swing.JTextField totalsewa;
    private javax.swing.JTextField txtTotalHarga;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
