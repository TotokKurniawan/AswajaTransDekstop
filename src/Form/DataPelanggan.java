/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form;

/**
 *
 * @author Totok Kurniawan
 */
import com.barcodelib.barcode.Linear;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.output.OutputException;

public class DataPelanggan extends javax.swing.JFrame {

    String sql;
    Statement s;
    ResultSet r;
    String[] data;
    Connection conn;
    DefaultTableModel model;

    /**
     * Creates new form Logout
     */
    public DataPelanggan() throws SQLException {
        initComponents();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        data = new String[4];
        conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
        model = (DefaultTableModel) jTable3.getModel();
        loaddata();
        Tampil_Jam();
        Tampil_Tanggal();
        NIK1.requestFocus();
TampilFoto();
//anggep();
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
        model.getDataVector().removeAllElements();
        sql = "CALL `TampilPelanggan`();";
        try {
            s = conn.createStatement();
            r = s.executeQuery(sql);

            while (r.next()) {
                data[0] = r.getString("NIK");
                data[1] = r.getString("Nama_Pelanggan");
                data[2] = r.getString("No_Telp");
                data[3] = r.getString("Alamat");
                model.addRow(data);

                ImageIcon imgThisImg = new ImageIcon("src/barc/" + NIK1.getText() + ".png");
                txtbarc.setIcon(imgThisImg);

            }
            s.close();
            r.close();
            int jumlahpelangganl = jTable3.getRowCount();
            jumlah.setText("" + jumlahpelangganl);

        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }
//    public void anggep(){
//    if (Session.getU_datapelanggan().equals(null)) {
//       JOptionPane.showMessageDialog(null, "nik tidak ada");
//    }};

    public void cari() {
        model = (DefaultTableModel) jTable3.getModel();
        model.getDataVector().removeAllElements();
        String nggolek = "select * from pelanggan WHERE NIK like'%" + cari.getText() + "%' || Nama_Pelanggan like'%" + cari.getText() + "%'";

        try {
            s = conn.createStatement();
            r = s.executeQuery(nggolek);

            while (r.next()) {
                data[0] = r.getString("NIK");
                data[1] = r.getString("Nama_Pelanggan");
                data[2] = r.getString("No_Telp");
                data[3] = r.getString("Alamat");
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

    void bersih() {

        NIK1.setText("");
        txtnama.setText("");
        txtno.setText("");
        txtalamat.setText("");
        txtbarc.setText("");
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
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtno = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cari = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        NIK1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtnama = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jumlah = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtalamat = new javax.swing.JTextArea();
        BtnCetak = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle4 = new rojerusan.RSMaterialButtonRectangle();
        lanjut = new rojerusan.RSMaterialButtonRectangle();
        jLabel2 = new javax.swing.JLabel();
        BtnEdit = new rojerusan.RSMaterialButtonRectangle();
        txtbarc = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        Kembali2 = new rojerusan.RSMaterialButtonRectangle();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tgl1 = new javax.swing.JLabel();
        Side_Bar = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Log1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        DatTranSew1 = new javax.swing.JButton();
        TranSew1 = new javax.swing.JButton();
        DatMob1 = new javax.swing.JButton();
        DatPel1 = new javax.swing.JButton();
        Dashboard = new javax.swing.JButton();
        DatMob2 = new javax.swing.JButton();
        txtfoto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Arial Black", 3, 36)); // NOI18N
        jLabel7.setText("Welcome to Re-Mob");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, -1));

        jLabel8.setText(":");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 12, -1));

        jLabel9.setText(":");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 12, -1));

        jLabel10.setText(":");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 500, 12, -1));

        txtno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnoKeyTyped(evt);
            }
        });
        jPanel1.add(txtno, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 245, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "NIK", "Nama", "No Telfon", "Alamat"
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

    jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 220, 666, 260));

    jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jLabel12.setText("No Telpon *");
    jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

    jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jLabel13.setText("Barcode");
    jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, -1));

    jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jLabel5.setText("NIK *");
    jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

    cari.setText("Pencarian");
    cari.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            cariFocusGained(evt);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            cariFocusLost(evt);
        }
    });
    cari.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cariActionPerformed(evt);
        }
    });
    cari.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            cariKeyReleased(evt);
        }
    });
    jPanel1.add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, 300, 23));

    jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jLabel1.setText("Nama Pelanggan * ");
    jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

    NIK1.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            NIK1KeyPressed(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            NIK1KeyTyped(evt);
        }
    });
    jPanel1.add(NIK1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 237, -1));

    jLabel15.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
    jLabel15.setText("Data Pelanggan ");
    jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 96, -1, -1));

    txtnama.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            txtnamaKeyPressed(evt);
        }
    });
    jPanel1.add(txtnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 245, -1));

    jLabel16.setText(":");
    jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 12, -1));

    jLabel17.setText("Jumlah Pelanggan :");
    jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 190, -1, -1));

    jumlah.setText("0");
    jPanel1.add(jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 190, -1, -1));

    jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jScrollPane1KeyPressed(evt);
        }
    });

    txtalamat.setColumns(20);
    txtalamat.setRows(5);
    txtalamat.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            txtalamatKeyPressed(evt);
        }
    });
    jScrollPane1.setViewportView(txtalamat);

    jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 360, 245, -1));

    BtnCetak.setBackground(new java.awt.Color(255, 255, 102));
    BtnCetak.setForeground(new java.awt.Color(0, 0, 0));
    BtnCetak.setText("Cetak ");
    BtnCetak.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BtnCetakActionPerformed(evt);
        }
    });
    jPanel1.add(BtnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 620, 112, 42));

    rSMaterialButtonRectangle2.setBackground(new java.awt.Color(0, 255, 0));
    rSMaterialButtonRectangle2.setForeground(new java.awt.Color(0, 0, 0));
    rSMaterialButtonRectangle2.setText("Tambah");
    rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rSMaterialButtonRectangle2ActionPerformed(evt);
        }
    });
    jPanel1.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 620, 112, 42));

    rSMaterialButtonRectangle3.setBackground(new java.awt.Color(255, 0, 0));
    rSMaterialButtonRectangle3.setForeground(new java.awt.Color(0, 0, 0));
    rSMaterialButtonRectangle3.setText("Hapus");
    rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rSMaterialButtonRectangle3ActionPerformed(evt);
        }
    });
    jPanel1.add(rSMaterialButtonRectangle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 620, 112, 42));

    rSMaterialButtonRectangle4.setBackground(new java.awt.Color(153, 153, 153));
    rSMaterialButtonRectangle4.setForeground(new java.awt.Color(0, 0, 0));
    rSMaterialButtonRectangle4.setText("Kembali");
    rSMaterialButtonRectangle4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rSMaterialButtonRectangle4ActionPerformed(evt);
        }
    });
    jPanel1.add(rSMaterialButtonRectangle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 620, 112, 42));

    lanjut.setBackground(new java.awt.Color(255, 255, 255));
    lanjut.setForeground(new java.awt.Color(0, 0, 0));
    lanjut.setText("Lanjut Transaksi");
    lanjut.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            lanjutActionPerformed(evt);
        }
    });
    jPanel1.add(lanjut, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 680, 341, 42));

    jLabel2.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis.png"))); // NOI18N
    jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 1156, 16));

    BtnEdit.setBackground(new java.awt.Color(102, 102, 255));
    BtnEdit.setForeground(new java.awt.Color(0, 0, 0));
    BtnEdit.setText("Edit");
    BtnEdit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BtnEditActionPerformed(evt);
        }
    });
    jPanel1.add(BtnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 620, 112, 42));

    txtbarc.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jPanel1.add(txtbarc, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 490, 560, 120));

    jLabel18.setText(":");
    jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 12, -1));

    jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
    jLabel19.setText("Alamat *");
    jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

    Kembali2.setForeground(new java.awt.Color(51, 51, 51));
    Kembali2.setText("Print");
    Kembali2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Kembali2ActionPerformed(evt);
        }
    });
    jPanel1.add(Kembali2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 620, 102, 43));

    jPanel2.setBackground(new java.awt.Color(255, 255, 255));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    waktu.setBackground(new java.awt.Color(255, 255, 255));
    waktu.setText("Waktu Sekarang :");
    jPanel2.add(waktu);

    jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 90, 180, 30));

    jPanel3.setBackground(new java.awt.Color(255, 255, 255));
    jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    tgl1.setBackground(new java.awt.Color(255, 255, 255));
    tgl1.setText("Tanggal Sekarang :");
    jPanel3.add(tgl1);

    jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 180, 30));

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 1160, 750));

    Side_Bar.setBackground(new java.awt.Color(105, 105, 105));
    Side_Bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel6.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
    jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis panjang.png"))); // NOI18N
    Side_Bar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 0, -1, 756));

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

    txtfoto.setFont(new java.awt.Font("Arial Black", 3, 18)); // NOI18N
    txtfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    txtfoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/profile.png"))); // NOI18N
    Side_Bar.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

    getContentPane().add(Side_Bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 750));

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        int baris = jTable3.rowAtPoint(evt.getPoint());

        String nikk = jTable3.getValueAt(baris, 0).toString();
        NIK1.setText(nikk);

        Session.setU_datapelanggan(nikk);

        String tnama = jTable3.getValueAt(baris, 1).toString();
        txtnama.setText(tnama);

        String talamat = jTable3.getValueAt(baris, 3).toString();
        txtalamat.setText(talamat);

        String tno = jTable3.getValueAt(baris, 2).toString();
        txtno.setText(tno);

        ImageIcon imgThisImg = new ImageIcon("src/barc/" + NIK1.getText() + ".png");
        txtbarc.setIcon(imgThisImg);


    }//GEN-LAST:event_jTable3MouseClicked

    private void BtnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakActionPerformed
        // TODO add your handling code here:

        try {
            Linear barcod = new Linear();
            barcod.setType(Linear.CODE128);
            barcod.setData(NIK1.getText());

            barcod.setI(11.0f);
            String fname = NIK1.getText();
            barcod.renderBarcode("src/barc/" + fname + ".png");

            ImageIcon imgThisImg = new ImageIcon("src/barc/" + NIK1.getText() + ".png");
            txtbarc.setIcon(imgThisImg);
            JOptionPane.showMessageDialog(null, imgThisImg);

            JOptionPane.showMessageDialog(null, "Berhasil Mencetak Barcode");
//            bersih();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Barcode" + e.getMessage());
        }
//        }
    }//GEN-LAST:event_BtnCetakActionPerformed

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed

        // TODO add your handling code here:
        String NIK = NIK1.getText();
        String nama = txtnama.getText();
        String No = txtno.getText();
        String Alamat = txtalamat.getText();

        try {
            String tampilBarang = "SELECT * FROM `pelanggan` WHERE `NIK` ='" + NIK + "'";
            PreparedStatement ps = conn.prepareStatement(tampilBarang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "ID dan Nama sudah digunakan");
            } else {
                String tambahBarang = "INSERT INTO `pelanggan`(`NIK`, `Nama_Pelanggan`, `No_Telp`, `Alamat`) "
                        + "VALUES (?,?,?,?)";
                ps = conn.prepareStatement(tambahBarang);
                ps.setString(1, NIK);
                ps.setString(2, nama);
                ps.setString(3, No);
                ps.setString(4, Alamat);

                ps.executeUpdate();
                bersih();
                loaddata();
                JOptionPane.showMessageDialog(null, "Data Pelanggan Berhasil DiTambahkan");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Di cek Kembali, " + e + "");
        }

//        try {
//            if (NIK.isEmpty() || nama.isEmpty() || No.isEmpty() || Alamat.isEmpty()) {
//                JOptionPane.showMessageDialog(null, "Harap Terlebih Dahulu");
//            } else {
//
//                sql = "insert into pelanggan values ('" + NIK + "','" + nama + "','" + No + "','" + Alamat + "')";
//
//                s = conn.createStatement();
//                s.executeUpdate(sql);
//
//                loaddata();
//                JOptionPane.showMessageDialog(null, "Data berhasil disimpan ");
//
//                s.close();
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e.getMessage());
//        }
    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        // TODO add your handling code here:
        int row = jTable3.getSelectedRow();
        String idhapus = jTable3.getValueAt(row, 0).toString();

        try {

            sql = "delete from pelanggan where NIK ='" + idhapus + "'";

            s = conn.createStatement();

            s.execute(sql);

            loaddata();
            JOptionPane.showMessageDialog(null, "Data berhasil hapus ");
            bersih();
            s.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void rSMaterialButtonRectangle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle4ActionPerformed
        // TODO add your handling code here:
        Dashboard dash = new Dashboard();
        dash.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonRectangle4ActionPerformed

    private void lanjutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lanjutActionPerformed
        // TODO add your handling code here:
        if (NIK1.getText().isEmpty() || txtnama.getText().isEmpty() || txtno.getText().isEmpty() || txtalamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Harap Pilih Data Terlebih Dahulu");
        } else {
            try {
                new TransaksiSewa().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
                this.dispose();

            }
        }
    }//GEN-LAST:event_lanjutActionPerformed

    private void cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyReleased
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_cariKeyReleased

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        Dashboard menu = new Dashboard();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

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

    private void cariFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cariFocusLost
        // TODO add your handling code here:
        String cari1 = cari.getText();
        if (cari1.equals("") | cari1.equals("Pencarian")) {
            cari.setText("Pencarian");
        }
    }//GEN-LAST:event_cariFocusLost

    private void cariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cariFocusGained
        // TODO add your handling code here:
        String cari1 = cari.getText();
        if (cari1.equals("Pencarian")) {
            cari.setText("");
        }
    }//GEN-LAST:event_cariFocusGained

    private void txtnoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnoKeyTyped
        // TODO add your handling code here:
        // membuat sebuah character yang harus menginputkan angka atau teks tidak boleh lebih dari 13
        if (!Character.isDigit(evt.getKeyChar()) || txtno.getText().length() >= 13) {
            // akan dipanggil untuk mengonsumsi sebuah angka dan mencegah ditampilkan pada textfield
            evt.consume();
        }

    }//GEN-LAST:event_txtnoKeyTyped

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        // TODO add your handling code here:
        int row = jTable3.getSelectedRow();
        String idupdate = jTable3.getValueAt(row, 0).toString();

        String NIK = NIK1.getText();
        String nama = txtnama.getText();
        String No = txtno.getText();
        String Alamat = txtalamat.getText();

        try {
            sql = "update pelanggan set NIK = '" + NIK + "',Nama_Pelanggan ='" + nama + "',No_Telp='" + No + "',Alamat = '" + Alamat + "' where NIK ='" + idupdate + "'";
            s = conn.createStatement();
            s.execute(sql);
            loaddata();
            JOptionPane.showMessageDialog(null, "Data berhasil dirubah ");
            s.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }//GEN-LAST:event_BtnEditActionPerformed

    private void NIK1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NIK1KeyTyped

        if (!Character.isDigit(evt.getKeyChar()) || NIK1.getText().length() >= 15) {
            // akan dipanggil untuk mengonsumsi sebuah angka dan mencegah ditampilkan pada textfield
            evt.consume();
        }


    }//GEN-LAST:event_NIK1KeyTyped

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_cariActionPerformed

    private void NIK1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NIK1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtnama.requestFocus();
        }
    }//GEN-LAST:event_NIK1KeyPressed

    private void txtnamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnamaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtno.requestFocus();
        }
    }//GEN-LAST:event_txtnamaKeyPressed

    private void txtnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtalamat.requestFocus();
        }
    }//GEN-LAST:event_txtnoKeyPressed

    private void Kembali2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Kembali2ActionPerformed
        //
        //try {
        //            String report = "C:\\Users\\ndraa\\Documents\\NetBeansProjects\\aswajatrans\\Project\\src\\Report\\nota.jasper"; // sesuaikan dengan lokasi file
        //            java.sql.Connection conn=Koneksi.Connect.KoneksiDB();
        //            Map<String, Object> parameter = new HashMap<String,Object>();
        //            parameter.put("id_Sewa",kd_nt); // kalau pakai parameter
        //            JasperReport JRpt=JasperCompileManager.compileReport(report);
        //            JasperPrint JPrint=JasperFillManager.fillReport(JRpt, parameter, conn);
        //            JasperViewer.viewReport(JPrint,false);  //view report
        //            //JasperPrintManager.printReport(JPrint, false);  // print report
        //        }catch (Exception e){
        //            System.out.println(e);
        //}
        //        // TODO add your handling code here:
        try {
            String NamaFile = "C:\\Users\\ndraa\\Documents\\NetBeansProjects\\aswajatrans\\Project\\src\\Report\\nota2.jasper";
            String url = "jdbc:mysql://localhost:3306/";
            String dbName = "aswajatrans";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "root";
            String password = "";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection koneksi = DriverManager.getConnection(url + dbName, userName, password);
            HashMap param = new HashMap();

            param.put("NIK", String.valueOf(NIK1.getText()));

            JasperPrint Jprint = JasperFillManager.fillReport(NamaFile, param, koneksi);
            JasperViewer.viewReport(Jprint, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
        ////
        //        Connection con = null;
        //        try {
        //            String jdbcDriver = "com.mysql.jdbc.Driver";
        //            Class.forName(jdbcDriver);
        //
        //            String url = "jdbc:mysql://localhost/aswajatrans";
        //            String user = "root";
        //            String pass = "";
        //
        //            con = DriverManager.getConnection(url, user, pass);
        //            Statement stm = (Statement) con.createStatement();
        //
        //            try {
        //                Map prs = new HashMap();
        //                JasperReport JRpt = JasperCompileManager.compileReport("/src/nota.jrxml");
        //                JasperPrint JPrint = JasperFillManager.fillReport(JRpt, prs, con);
        //                JasperViewer.viewReport(JPrint, false);
        //            } catch (Exception rptexcpt) {
        //                System.out.println("Report Can’t view because : " + rptexcpt);
        //            }
        //        } catch (Exception e) {
        //            System.out.println(e);
        //        }
    }//GEN-LAST:event_Kembali2ActionPerformed

    private void txtalamatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtalamatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtalamatKeyPressed

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NIK1.requestFocus();
        }
    }//GEN-LAST:event_jScrollPane1KeyPressed

    private void DatMob2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatMob2ActionPerformed
        // TODO add your handling code here:

        try {
            new Pengeluaran().setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Pengeluaran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_DatMob2ActionPerformed

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
            java.util.logging.Logger.getLogger(DataPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                    new DataPelanggan().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(DataPelanggan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonRectangle BtnCetak;
    private rojerusan.RSMaterialButtonRectangle BtnEdit;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DatMob1;
    private javax.swing.JButton DatMob2;
    private javax.swing.JButton DatPel1;
    private javax.swing.JButton DatTranSew1;
    private rojerusan.RSMaterialButtonRectangle Kembali2;
    private javax.swing.JButton Log1;
    private javax.swing.JTextField NIK1;
    private javax.swing.JPanel Side_Bar;
    private javax.swing.JButton TranSew1;
    private javax.swing.JTextField cari;
    private javax.swing.JButton jButton11;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel jumlah;
    private rojerusan.RSMaterialButtonRectangle lanjut;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle4;
    private javax.swing.JLabel tgl1;
    private javax.swing.JTextArea txtalamat;
    private javax.swing.JLabel txtbarc;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtno;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
