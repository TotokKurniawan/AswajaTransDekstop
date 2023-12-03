/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form;

import Koneksi.Connect;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Totok Kurniawan
 */
public class DataMobil1 extends javax.swing.JFrame {

    /**
     * Creates new form Logout
     */
    String sql;
    Statement s;
    ResultSet r;
    String[] data;
    Connection conn;
    DefaultTableModel model;

    public DataMobil1() throws SQLException {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        data = new String[5];
        conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
        loaddata();
        Tampil_Jam();
        Tampil_Tanggal();
        txtnopol.requestFocus();
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
    new Timer(1000, taskPerformer).start();
    }   
    
 
public void Tampil_Tanggal() {
    java.util.Date tglsekarang = new java.util.Date();
    SimpleDateFormat smpdtfmt = new SimpleDateFormat("dd MMMMMMMMM yyyy", Locale.getDefault());
    String tanggal1 = smpdtfmt.format(tglsekarang);
    tgl1.setText(tanggal1);}

    public void loaddata() {
        model = (DefaultTableModel) jTable3.getModel();
        model.getDataVector().removeAllElements();
        sql = "CALL `TampilMobil`();";

        try {
            s = conn.createStatement();
            r = s.executeQuery(sql);

            while (r.next()) {
                data[0] = r.getString("Nopol");
                data[1] = r.getString("MerkMobil");
                data[2] = r.getString("TypeMobil");
                data[3] = r.getString("Harga");
                data[4] = r.getString("Status");
                model.addRow(data);
            }
            s.close();
            r.close();

            int jumlahmobil = jTable3.getRowCount();
            jumlah.setText("" + jumlahmobil);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }

    public void cari() {
        model = (DefaultTableModel) jTable3.getModel();
        model.getDataVector().removeAllElements();
        String nggolek = "select * from mobil WHERE Nopol like'%" + cari.getText()
                + "%' || MerkMobil like'%" + cari.getText()
                + "%' || TypeMobil like'%" + cari.getText()
                + "%' || Harga like'%" + cari.getText() + "'";

        try {
            s = conn.createStatement();
            r = s.executeQuery(nggolek);

            while (r.next()) {
                data[0] = r.getString("Nopol");
                data[1] = r.getString("MerkMobil");
                data[2] = r.getString("TypeMobil");
                data[3] = r.getString("Harga");
                data[4] = r.getString("Status");
                model.addRow(data);
            }
            s.close();
            r.close();

            int jumlahmobil = jTable3.getRowCount();
            jumlah.setText("" + jumlahmobil);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }

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
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        cari = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtharga = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txttype = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtmerl = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtnopol = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jumlah = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        BtnHapus = new rojerusan.RSMaterialButtonRectangle();
        BtnEdit = new rojerusan.RSMaterialButtonRectangle();
        BtnSimpan = new rojerusan.RSMaterialButtonRectangle();
        Edit2 = new rojerusan.RSMaterialButtonRectangle();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        tgl1 = new javax.swing.JLabel();
        lanjut = new rojerusan.RSMaterialButtonRectangle();
        jPanel3 = new javax.swing.JPanel();
        Log1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        DatTranSew1 = new javax.swing.JButton();
        TranSew1 = new javax.swing.JButton();
        DatMob2 = new javax.swing.JButton();
        DatPel1 = new javax.swing.JButton();
        DatMob1 = new javax.swing.JButton();
        Dashboard = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtfoto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 69, 1156, 16));

        jLabel7.setFont(new java.awt.Font("Arial Black", 3, 36)); // NOI18N
        jLabel7.setText("Welcome to Re-Mob");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nopol Kendaraan", "Merk Kendaraan", "Type Kendaraan", "Harga", "Status"
            }
        ){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        }
    );
    jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTable3MouseClicked(evt);
        }
    });
    jScrollPane3.setViewportView(jTable3);

    jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 260, 655, 270));

    cari.setText("Pencarian");
    cari.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            cariFocusGained(evt);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            cariFocusLost(evt);
        }
    });
    cari.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            cariKeyReleased(evt);
        }
    });
    jPanel1.add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 220, 290, -1));

    jLabel16.setText(":");
    jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 223, 12, -1));

    jLabel8.setText(":");
    jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 263, 12, -1));

    jLabel9.setText(":");
    jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 303, 12, -1));

    jLabel10.setText(":");
    jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 343, 12, -1));

    jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jLabel17.setText("Type Kendaraan *");
    jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 298, -1, -1));
    jPanel1.add(txtharga, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 340, 185, -1));

    jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jLabel5.setText("Merk Kendaraan *");
    jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 258, -1, -1));

    txttype.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            txttypeKeyPressed(evt);
        }
    });
    jPanel1.add(txttype, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 300, 185, -1));

    jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jLabel1.setText("Plat Nomor *");
    jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 218, -1, -1));

    txtmerl.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            txtmerlKeyPressed(evt);
        }
    });
    jPanel1.add(txtmerl, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 260, 185, -1));

    jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jLabel18.setText("Harga *");
    jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 338, -1, -1));

    txtnopol.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtnopolActionPerformed(evt);
        }
    });
    txtnopol.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            txtnopolKeyPressed(evt);
        }
    });
    jPanel1.add(txtnopol, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 220, 185, -1));

    jLabel19.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
    jLabel19.setText("Tambah Data Mobil");
    jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(383, 117, -1, -1));

    jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jLabel12.setText("Jumlah Mobil  :");
    jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 220, -1, -1));

    jumlah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
    jumlah.setText("0");
    jPanel1.add(jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 220, -1, -1));

    jLabel25.setText("Status");
    jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 382, -1, -1));

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mobil Belum Disewa", "Mobil Sedang Disewa", " " }));
    jComboBox1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jComboBox1ActionPerformed(evt);
        }
    });
    jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 378, 185, -1));

    jLabel14.setText(":");
    jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 382, 12, -1));

    BtnHapus.setBackground(new java.awt.Color(255, 0, 0));
    BtnHapus.setForeground(new java.awt.Color(0, 0, 0));
    BtnHapus.setText("Hapus");
    BtnHapus.setPreferredSize(new java.awt.Dimension(300, 75));
    BtnHapus.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BtnHapusActionPerformed(evt);
        }
    });
    jPanel1.add(BtnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 520, 118, 42));

    BtnEdit.setBackground(new java.awt.Color(255, 255, 51));
    BtnEdit.setForeground(new java.awt.Color(0, 0, 0));
    BtnEdit.setText("Edit");
    BtnEdit.setPreferredSize(new java.awt.Dimension(300, 75));
    BtnEdit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BtnEditActionPerformed(evt);
        }
    });
    jPanel1.add(BtnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 440, 118, 42));

    BtnSimpan.setBackground(new java.awt.Color(0, 204, 0));
    BtnSimpan.setForeground(new java.awt.Color(0, 0, 0));
    BtnSimpan.setText("Simpan");
    BtnSimpan.setPreferredSize(new java.awt.Dimension(300, 75));
    BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BtnSimpanActionPerformed(evt);
        }
    });
    jPanel1.add(BtnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 440, 121, 42));

    Edit2.setBackground(new java.awt.Color(153, 153, 153));
    Edit2.setForeground(new java.awt.Color(0, 0, 0));
    Edit2.setText("Kembali");
    Edit2.setPreferredSize(new java.awt.Dimension(300, 75));
    Edit2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Edit2ActionPerformed(evt);
        }
    });
    jPanel1.add(Edit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 520, 118, 42));

    jPanel2.setBackground(new java.awt.Color(255, 255, 255));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    waktu.setBackground(new java.awt.Color(255, 255, 255));
    waktu.setText("Waktu Sekarang :");
    jPanel2.add(waktu);

    jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 100, 180, 30));

    jPanel4.setBackground(new java.awt.Color(255, 255, 255));
    jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    tgl1.setBackground(new java.awt.Color(255, 255, 255));
    tgl1.setText("Tanggal Sekarang :");
    jPanel4.add(tgl1);

    jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 180, 30));

    lanjut.setBackground(new java.awt.Color(102, 102, 255));
    lanjut.setForeground(new java.awt.Color(0, 0, 0));
    lanjut.setText("Lanjut Pengeluaran");
    lanjut.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            lanjutActionPerformed(evt);
        }
    });
    jPanel1.add(lanjut, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, 341, 42));

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 1150, 750));

    jPanel3.setBackground(new java.awt.Color(105, 105, 105));
    jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    Log1.setBackground(java.awt.SystemColor.controlLtHighlight);
    Log1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Logout.png"))); // NOI18N
    Log1.setText("Logout");
    Log1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    Log1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Log1ActionPerformed(evt);
        }
    });
    jPanel3.add(Log1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 670, 182, -1));

    jButton11.setBackground(java.awt.SystemColor.controlLtHighlight);
    jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/laporan.png"))); // NOI18N
    jButton11.setText("Laporan");
    jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jButton11.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton11ActionPerformed(evt);
        }
    });
    jPanel3.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 182, -1));

    DatTranSew1.setBackground(java.awt.SystemColor.controlLtHighlight);
    DatTranSew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/pengembalian.png"))); // NOI18N
    DatTranSew1.setText("Data Sewa Mobil");
    DatTranSew1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    DatTranSew1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DatTranSew1ActionPerformed(evt);
        }
    });
    jPanel3.add(DatTranSew1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 182, -1));

    TranSew1.setBackground(java.awt.SystemColor.controlLtHighlight);
    TranSew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/sewa mobil_1.png"))); // NOI18N
    TranSew1.setText("Sewa Mobil");
    TranSew1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    TranSew1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            TranSew1ActionPerformed(evt);
        }
    });
    jPanel3.add(TranSew1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 182, -1));

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
    jPanel3.add(DatMob2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 182, 45));

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
    jPanel3.add(DatPel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 182, 46));

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
    jPanel3.add(DatMob1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 182, 45));

    Dashboard.setBackground(java.awt.SystemColor.controlLtHighlight);
    Dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/dashboard.png"))); // NOI18N
    Dashboard.setText("Dashboard");
    Dashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    Dashboard.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            DashboardActionPerformed(evt);
        }
    });
    jPanel3.add(Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 182, 182, -1));

    jLabel6.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
    jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis panjang.png"))); // NOI18N
    jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 0, -1, 756));

    txtfoto.setFont(new java.awt.Font("Arial Black", 3, 18)); // NOI18N
    txtfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    txtfoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/profile.png"))); // NOI18N
    jPanel3.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

    getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 750));

    pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        int baris = jTable3.rowAtPoint(evt.getPoint());

        String nopol = jTable3.getValueAt(baris, 0).toString();
        txtnopol.setText(nopol);
        Session.setU_Nopol(nopol);

        String merk = jTable3.getValueAt(baris, 1).toString();
        txtmerl.setText(merk);

        String type = jTable3.getValueAt(baris, 2).toString();
        txttype.setText(type);

        String harga = jTable3.getValueAt(baris, 3).toString();
        txtharga.setText(harga);

    }//GEN-LAST:event_jTable3MouseClicked

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        // TODO add your handling code here:
        int row = jTable3.getSelectedRow();
        String idhapus = jTable3.getValueAt(row, 0).toString();

        try {

            sql = "delete from mobil where Nopol ='" + idhapus + "'";

            s = conn.createStatement();
            s.execute(sql);

            loaddata();
            JOptionPane.showMessageDialog(null, "Data berhasil hapus ");

            s.close();
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        String nopol = txtnopol.getText();
        String merk = txtmerl.getText();
        String type = txttype.getText();
        String harga = txtharga.getText();
        String status = (String) jComboBox1.getSelectedItem();

        try {
            if (nopol.isEmpty() || merk.isEmpty() || type.isEmpty() || harga.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(null,"Harap Isi Terlebih Dahulu ");
            } else {
            sql = "insert into mobil values ('" + nopol + "','" + merk + "','" + type + "','" + harga + "','" + status + "')";

            s = conn.createStatement();
            s.executeUpdate(sql);
            loaddata();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan ");

            s.close();
                        }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data telah Tersedia" + e.getMessage());
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        // TODO add your handling code here:
        int row = jTable3.getSelectedRow();
        String idupdate = jTable3.getValueAt(row, 0).toString();

        String nopol = txtnopol.getText();
        String merk = txtmerl.getText();
        String type = txttype.getText();
        String harga = txtharga.getText();
        String status = (String) jComboBox1.getSelectedItem();

        try {

            sql = "update mobil set Nopol = '" + nopol + "',merkmobil ='" + merk + "',typemobil ='" + type + "',harga='" + harga + "',status ='" + status + "' where nopol ='" + idupdate + "'";

            s = conn.createStatement();
            s.execute(sql);

            loaddata();
            JOptionPane.showMessageDialog(null, "Data berhasil dirubah ");

            s.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Tidak Berubah" + e.getMessage());
        }
    }//GEN-LAST:event_BtnEditActionPerformed

    private void Edit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit2ActionPerformed
        // TODO add your handling code here:
        try {
            DataPelanggan DatPel = new DataPelanggan();
            DatPel.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(DataPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Edit2ActionPerformed

    private void txtnopolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnopolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnopolActionPerformed

    private void cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyReleased
        // TODO add your handling code here:
                cari();

        
          
    }//GEN-LAST:event_cariKeyReleased

    private void cariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cariFocusGained
        // TODO add your handling code here:
        String cari1 = cari.getText();
        if(cari1.equals("Pencarian")){
            cari.setText("");
        }
    }//GEN-LAST:event_cariFocusGained

    private void cariFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cariFocusLost
        // TODO add your handling code here:
         String cari1 = cari.getText();
        if(cari1.equals("")|cari1.equals("Pencarian")){
            cari.setText("Pencarian");}
    }//GEN-LAST:event_cariFocusLost

    private void txtnopolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnopolKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtmerl.requestFocus();}
        
    }//GEN-LAST:event_txtnopolKeyPressed

    private void txtmerlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmerlKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txttype.requestFocus();}
        
    }//GEN-LAST:event_txtmerlKeyPressed

    private void txttypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttypeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtharga.requestFocus();}
        
    }//GEN-LAST:event_txttypeKeyPressed

    private void Log1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Log1ActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            Login log = new Login();
            log.setVisible(true);
        }
    }//GEN-LAST:event_Log1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Laporan lap = new Laporan();
        lap.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void DatTranSew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatTranSew1ActionPerformed
        // TODO add your handling code here:
        DataSewa DatTranSew = new DataSewa();
        DatTranSew.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DatTranSew1ActionPerformed

    private void TranSew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TranSew1ActionPerformed
        // TODO add your handling code here:
        try {
            new TransaksiSewa().setVisible(true);
                    this.dispose();

        } catch (SQLException ex) {
            Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TranSew1ActionPerformed

    private void DatMob2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatMob2ActionPerformed
        // TODO add your handling code here:

        try {
            new Pengeluaran().setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Pengeluaran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_DatMob2ActionPerformed

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

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        Dashboard menu = new Dashboard();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void lanjutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lanjutActionPerformed
        // TODO add your handling code here:
        if(txtnopol.getText().isEmpty()|| txtmerl.getText().isEmpty() || txttype.getText().isEmpty() || txtharga.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Harap Pilih Data Terlebih Dahulu");
        }else{
            try {
                new Pengeluaran().setVisible(true);
                this.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
                this.dispose();

            }
        }
    }//GEN-LAST:event_lanjutActionPerformed

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
            java.util.logging.Logger.getLogger(DataMobil1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataMobil1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataMobil1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataMobil1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new DataMobil1().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(DataMobil1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonRectangle BtnEdit;
    private rojerusan.RSMaterialButtonRectangle BtnHapus;
    private rojerusan.RSMaterialButtonRectangle BtnSimpan;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DatMob1;
    private javax.swing.JButton DatMob2;
    private javax.swing.JButton DatPel1;
    private javax.swing.JButton DatTranSew1;
    private rojerusan.RSMaterialButtonRectangle Edit2;
    private javax.swing.JButton Log1;
    private javax.swing.JButton TranSew1;
    private javax.swing.JTextField cari;
    private javax.swing.JButton jButton11;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel jumlah;
    private rojerusan.RSMaterialButtonRectangle lanjut;
    private javax.swing.JLabel tgl1;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtmerl;
    private javax.swing.JTextField txtnopol;
    private javax.swing.JTextField txttype;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
