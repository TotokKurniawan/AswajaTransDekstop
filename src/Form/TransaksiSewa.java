/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form;

/**
 *
 * @author Totok Kurniawan
 */
import Koneksi.Connect;
import com.barcodelib.barcode.Linear;
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
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
//import net.sourceforge.barbecue.linear.LinearBarcode;

public class TransaksiSewa extends javax.swing.JFrame {

    Statement s;
    ResultSet r;

    Connection conn;
    String sql;

    private DefaultTableModel model;
    private DefaultTableModel model1;

    public int tot_brg;
    public int thotal;

    /**
     * Creates new form Logout
     */
    ArrayList<String> idList = new ArrayList<String>();

    public TransaksiSewa() throws SQLException {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
        model = new DefaultTableModel();
        //digunakan untuk memberi heading / judul pada kolom di tabel buku
        tbBarang.setModel(model); // "t sesuaikan dengan variabel name"
        model.addColumn("Nopol");
        model.addColumn("MerkMobil");
        model.addColumn("TypeMobil");
        model.addColumn("Harga");

        kd_transaksi();
        getData();
        tampilkanBarangBeli();
//        setTotalHarga();

        nik_trs.setText(Session.getU_datapelanggan());
        Tampil_Jam();
        Tampil_Tanggal();
        saiki();
        this.getRootPane().setDefaultButton(simpan);
//anggep();
lama_pinjam.requestFocus();
TampilFoto();
    }
    void anggep(){
        if (Session.getU_datapelanggan() == null ) {
            JOptionPane.showMessageDialog(null, "Harap input data pelanggan terlebih dahulu");
            try {
            DataPelanggan dataPelanggan = new DataPelanggan();
            dataPelanggan.setVisible(true); // Membuka form DataPelanggan
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
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


//}

//public void anggep(){
//    if (Session.getU_datapelanggan().equals(null)) {
//       JOptionPane.showMessageDialog(null, "nik tidak ada");
//        try {
//            new DataPelanggan().setVisible(true);
//        } catch (SQLException ex) {
//            Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
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

    public void saiki() {
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date skrg = new Date();
        tgl_rental.setText("" + sfDate.format(skrg));
    }

    public void tampilkanBarangBeli() {
        String[] judul = {"Nopol", "MerkMobil", "Lama Pinjam", "Subtotal", "Tanggal Kembali"};
        model1 = new DefaultTableModel(judul, 0);
        tbKeranjang.setModel(model1);
    }

    public void getData() {

        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            //membuat statemen untuk memanggil data table tabel_buku
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
            Statement stat = conn.createStatement();
            String sql = "CALL TampilBeberapaMobil(); ";
            ResultSet res = stat.executeQuery(sql);

            //pengecekan terhadap data tabel_buku
            while (res.next()) {
                Object[] obj = new Object[4];
                obj[0] = res.getString("Nopol");
                obj[1] = res.getString("MerkMobil");
                obj[2] = res.getString("TypeMobil");
                obj[3] = res.getString("Harga");
//                obj[4] = res.getString("Status");
//                ;
                model.addRow(obj);
            }
            int jumlahmobil = tbBarang.getRowCount();
            jumlah.setText("" + jumlahmobil);
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }

    void Bersih() {
        txtHargaBarang.setText("");
        lama_pinjam.setText("");
        txtSubTotal.setText("");
    }

    private void setTotalHarga() {
        int totalBarang = 0;
        int row = tbKeranjang.getRowCount();
        int total = 0;
        for (int i = 0; i < row; i++) {
            int sb = Integer.parseInt(tbKeranjang.getValueAt(i, 3).toString());
            int tot = Integer.parseInt(tbKeranjang.getValueAt(i, 2).toString());
            total = total + (sb);
            totalBarang = totalBarang + tot;
        }

        this.tot_brg = totalBarang;

        this.thotal = total;
        txtTotalHarga.setText(String.valueOf(this.thotal));
    }

    public void resetkeranjang() {
        model = (DefaultTableModel) tbBarang.getModel();
        model.getDataVector().removeAllElements();
    }

    public void cari() {
        model = (DefaultTableModel) tbBarang.getModel();
        model.getDataVector().removeAllElements();
        String nggolek = "select * from mobil WHERE Nopol like'%" + cari.getText()
                + "%' || MerkMobil like'%" + cari.getText()
                + "%' || TypeMobil like'%" + cari.getText()
                + "%' || Harga like'%" + cari.getText() + "'";
        try {
            //membuat statemen untuk memanggil data table tabel_buku
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
            Statement stat = conn.createStatement();

            ResultSet res = stat.executeQuery(nggolek);

            //pengecekan terhadap data tabel_buku
            while (res.next()) {
                Object[] obj = new Object[5];
                obj[0] = res.getString("Nopol");
                obj[1] = res.getString("MerkMobil");
                obj[2] = res.getString("TypeMobil");
                obj[3] = res.getString("Harga");
                obj[4] = res.getString("Status");
                ;
                model.addRow(obj);
            }
            int jumlahmobil = tbBarang.getRowCount();
            jumlah.setText("" + jumlahmobil);
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }

    public void kd_transaksi() {
        try {

            String sql3 = "SELECT * from sewa order by id_Sewa DESC ";
            Connection conn = Koneksi.Connect.KoneksiDB();
            Statement stat = (Statement) conn.createStatement();
            ResultSet resti = stat.executeQuery(sql3);
            if (resti.next()) {
                String NoJual = resti.getString("id_Sewa").substring(2);
                String AN = "" + (Integer.parseInt(NoJual) + 1);
//                String AN = "" + (Integer.parseInt(txt_idsupplier.getText()) + 1);
                String Nol = "";

                if (AN.length() == 1) {
                    Nol = "000";
                } else if (AN.length() == 2) {
                    Nol = "00";
                } else if (AN.length() == 3) {
                    Nol = "0";
                } else if (AN.length() == 4) {
                    Nol = "";
                }
                kd_nt.setText("TR" + Nol + AN);//sesuaikan dengan variable namenya
            } else {
                kd_nt.setText("TR0001");//sesuaikan dengan variable namenya
            }
            resti.close();
//            con.close();
        } catch (Exception e) {
            //penanganan masalah
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void tanggal_kmbl() {
        int lama = Integer.parseInt(lama_pinjam.getText());
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +lama);
        Date selesai = cal.getTime();

        tgl_sl.setText("" + sfDate.format(selesai));

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
        rSMaterialButtonRectangle4 = new rojerusan.RSMaterialButtonRectangle();
        jLabel2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbKeranjang = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        kd_nt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        nik_trs = new javax.swing.JTextField();
        bayar = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtTotalHarga = new javax.swing.JTextField();
        sis_byr = new javax.swing.JTextField();
        TXT_Kembalian = new javax.swing.JTextField();
        TXT_STBYR = new javax.swing.JTextField();
        simpan = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbBarang = new javax.swing.JTable();
        cari = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jumlah = new javax.swing.JLabel();
        txtHargaBarang = new javax.swing.JLabel();
        lama_pinjam = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        tgl_sl = new javax.swing.JTextField();
        tgl_rental = new javax.swing.JTextField();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        jLabel7 = new javax.swing.JLabel();
        tot_sewa = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tgl1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        Side_Bar = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Log1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        DatTranSew1 = new javax.swing.JButton();
        TranSew1 = new javax.swing.JButton();
        DatMob2 = new javax.swing.JButton();
        DatPel1 = new javax.swing.JButton();
        DatMob1 = new javax.swing.JButton();
        Dashboard = new javax.swing.JButton();
        txtfoto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSMaterialButtonRectangle4.setBackground(new java.awt.Color(255, 255, 0));
        rSMaterialButtonRectangle4.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonRectangle4.setText("Tambah");
        rSMaterialButtonRectangle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle4ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonRectangle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 250, 110, 40));

        jLabel2.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 77, 1156, -1));

        jLabel19.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
        jLabel19.setText("Sewa Mobil");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, -1, -1));

        jLabel1.setText("Lama Pinjam");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, -1, -1));

        jLabel4.setText("Sub Total");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 230, 63, -1));

        txtSubTotal.setEnabled(false);
        txtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubTotalActionPerformed(evt);
            }
        });
        txtSubTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSubTotalKeyReleased(evt);
            }
        });
        jPanel1.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 230, 170, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Harga Per Sewa :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 210, -1, -1));

        jLabel8.setText("Kode Struk");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 350, -1, -1));

        jLabel10.setText("Tanggal Rental");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 410, -1, -1));

        tbKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        }
    );
    tbKeranjang.addAncestorListener(new javax.swing.event.AncestorListener() {
        public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
        }
        public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            tbKeranjangAncestorAdded(evt);
        }
        public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
        }
    });
    jScrollPane1.setViewportView(tbKeranjang);

    jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 530, 188));

    jLabel11.setText("Tanggal Kembali");
    jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 260, -1, -1));

    jLabel12.setText("Bayar");
    jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 440, -1, -1));

    jLabel13.setText("Status Bayar");
    jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 530, -1, -1));

    jLabel14.setText("NIK");
    jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 380, -1, -1));

    jLabel15.setText("Sisa yang harus dibayar");
    jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 470, -1, -1));

    jLabel16.setText("Kembalian");
    jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 500, -1, -1));

    jLabel17.setText(":");
    jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 350, -1, -1));

    jLabel20.setText(":");
    jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 410, -1, -1));

    jLabel21.setText(":");
    jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 380, 3, -1));

    kd_nt.setEnabled(false);
    kd_nt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            kd_ntActionPerformed(evt);
        }
    });
    jPanel1.add(kd_nt, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 340, 149, 30));

    jLabel23.setText(":");
    jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 430, -1, 30));

    jLabel26.setText("Total Harga");
    jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 610, -1, 10));

    nik_trs.setEnabled(false);
    nik_trs.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            nik_trsActionPerformed(evt);
        }
    });
    jPanel1.add(nik_trs, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 380, 149, 30));

    bayar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            bayarActionPerformed(evt);
        }
    });
    bayar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            bayarKeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            bayarKeyTyped(evt);
        }
    });
    jPanel1.add(bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 440, 149, -1));

    jLabel27.setText(":");
    jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 460, -1, 30));

    jLabel28.setText(":");
    jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 520, -1, 30));

    jLabel29.setText(":");
    jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 490, -1, 30));

    txtTotalHarga.setEnabled(false);
    txtTotalHarga.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtTotalHargaActionPerformed(evt);
        }
    });
    jPanel1.add(txtTotalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 600, 149, -1));

    sis_byr.setEnabled(false);
    sis_byr.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sis_byrActionPerformed(evt);
        }
    });
    jPanel1.add(sis_byr, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 470, 149, -1));

    TXT_Kembalian.setEnabled(false);
    TXT_Kembalian.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            TXT_KembalianActionPerformed(evt);
        }
    });
    jPanel1.add(TXT_Kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 500, 149, -1));

    TXT_STBYR.setEnabled(false);
    TXT_STBYR.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            TXT_STBYRActionPerformed(evt);
        }
    });
    jPanel1.add(TXT_STBYR, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 530, 149, -1));

    simpan.setBackground(new java.awt.Color(51, 255, 51));
    simpan.setForeground(new java.awt.Color(0, 0, 0));
    simpan.setText("Simpan");
    simpan.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            simpanActionPerformed(evt);
        }
    });
    jPanel1.add(simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 640, 101, 47));

    rSMaterialButtonRectangle2.setBackground(new java.awt.Color(153, 153, 153));
    rSMaterialButtonRectangle2.setForeground(new java.awt.Color(0, 0, 0));
    rSMaterialButtonRectangle2.setText("Kembali");
    rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rSMaterialButtonRectangle2ActionPerformed(evt);
        }
    });
    jPanel1.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 640, 111, 44));

    tbBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    tbBarang.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ){
        public boolean isCellEditable(int row, int column){
            return false;
        }
    }
    );
    tbBarang.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tbBarangMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(tbBarang);

    jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 530, 250));

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
    jPanel1.add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 240, -1));

    jLabel32.setText("Jumlah Mobil   :");
    jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, -1, -1));

    jumlah.setText("0");
    jPanel1.add(jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 210, -1, -1));

    txtHargaBarang.setText("0");
    jPanel1.add(txtHargaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 210, -1, -1));

    lama_pinjam.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            lama_pinjamActionPerformed(evt);
        }
    });
    lama_pinjam.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            lama_pinjamKeyReleased(evt);
        }
    });
    jPanel1.add(lama_pinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 200, 170, -1));

    jLabel33.setText(":");
    jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 600, -1, 30));

    tgl_sl.setEnabled(false);
    tgl_sl.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tgl_slActionPerformed(evt);
        }
    });
    tgl_sl.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            tgl_slKeyReleased(evt);
        }
    });
    jPanel1.add(tgl_sl, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 260, 170, -1));

    tgl_rental.setEnabled(false);
    tgl_rental.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tgl_rentalActionPerformed(evt);
        }
    });
    jPanel1.add(tgl_rental, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 410, 149, -1));

    rSMaterialButtonRectangle3.setBackground(new java.awt.Color(255, 0, 0));
    rSMaterialButtonRectangle3.setForeground(new java.awt.Color(0, 0, 0));
    rSMaterialButtonRectangle3.setText("Hapus");
    rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rSMaterialButtonRectangle3ActionPerformed(evt);
        }
    });
    jPanel1.add(rSMaterialButtonRectangle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 250, 110, 40));

    jLabel7.setBackground(new java.awt.Color(255, 255, 255));
    jLabel7.setFont(new java.awt.Font("Arial Black", 3, 36)); // NOI18N
    jLabel7.setText("Welcome to Re-Mob");
    jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

    tot_sewa.setEnabled(false);
    tot_sewa.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tot_sewaActionPerformed(evt);
        }
    });
    jPanel1.add(tot_sewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 560, 149, 30));

    jLabel34.setText(":");
    jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 550, -1, 30));

    jLabel30.setText("Total Sewa");
    jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 560, -1, -1));

    jPanel3.setBackground(new java.awt.Color(255, 255, 255));
    jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    tgl1.setBackground(new java.awt.Color(255, 255, 255));
    tgl1.setText("Tanggal Sekarang :");
    jPanel3.add(tgl1);

    jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 180, 30));

    jPanel2.setBackground(new java.awt.Color(255, 255, 255));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    waktu.setBackground(new java.awt.Color(255, 255, 255));
    waktu.setText("Waktu Sekarang :");
    jPanel2.add(waktu);

    jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 100, 180, 30));

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 1150, 750));

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

    txtfoto.setFont(new java.awt.Font("Arial Black", 3, 18)); // NOI18N
    txtfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    txtfoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/profile.png"))); // NOI18N
    Side_Bar.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

    getContentPane().add(Side_Bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 750));

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tot_sewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tot_sewaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tot_sewaActionPerformed

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        // TODO add your handling code here:
        int row = tbKeranjang.getSelectedRow();
        idList.remove(row);
        model1.removeRow(row);
        setTotalHarga();
    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void tgl_rentalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_rentalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tgl_rentalActionPerformed

    private void tgl_slKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tgl_slKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tgl_slKeyReleased

    private void tgl_slActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_slActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tgl_slActionPerformed

    private void cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyReleased
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_cariKeyReleased

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariActionPerformed

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

    private void tbBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBarangMouseClicked
        // TODO add your handling code here:
        int baris = tbBarang.rowAtPoint(evt.getPoint());
        tbBarang.rowAtPoint(evt.getPoint());
        tbBarang.getValueAt(baris, 0).toString();

        String nm = tbBarang.getValueAt(baris, 1).toString();

        tbBarang.getValueAt(baris, 2).toString();

        String pr = tbBarang.getValueAt(baris, 3).toString();
        txtHargaBarang.setText(pr);
    }//GEN-LAST:event_tbBarangMouseClicked

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed
        // TODO add your handling code here:
        DataPelanggan DatPel = null;
        try {
            DatPel = new DataPelanggan();
        } catch (SQLException ex) {
            Logger.getLogger(DataPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        DatPel.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        // TODO add your handling code here:
        int rows = tbKeranjang.getRowCount();
        //        String statuss_mb = "mobil sedang disewa";
        String keterangan = " ";
        String pengembalian = null;
        String kodeNota = kd_nt.getText();
        int denda = 0;
        try {
            Connection conn = Koneksi.Connect.KoneksiDB();

            String sql = "INSERT INTO sewa VALUES('"
            + kodeNota
            + "',now(),'"
            + bayar.getText()
            + "','" + sis_byr.getText()
            + "','" + TXT_STBYR.getText()
            + "','" + txtTotalHarga.getText()
            + "','" + tot_sewa.getText()
            + "','" + Session.getU_datapelanggan()
            + "','" + Session.getU_id() + "')";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            pst.execute();

            for (int i = 0; i < rows; i++) {
                String sql1 = "INSERT INTO detail_sewa (id_Sewa, Nopol, Tgl_Kembali, Lama_Pinjam, tanggal_pengembalian, subtotal, Denda, Keterangan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst1 = conn.prepareStatement(sql1);
                pst1.setString(1, kd_nt.getText());
                pst1.setString(2, tbKeranjang.getValueAt(i, 0).toString());
                pst1.setString(3, tbKeranjang.getValueAt(i, 4).toString());
                pst1.setString(4, tbKeranjang.getValueAt(i, 2).toString());
                //                pst1.setString(5, statuss_mb);
                pst1.setNull(5, Types.DATE); // Set tanggal_pengembalian menjadi NULL
                pst1.setString(6, tbKeranjang.getValueAt(i, 3).toString());
                pst1.setString(7, "0");
                pst1.setString(8, keterangan);
                pst1.executeUpdate();
                //                String sql1 = "INSERT INTO detail_sewa VALUES('"
                //                        + kd_nt.getText() +
                //                        "','" + tbKeranjang.getValueAt(i, 0).toString()+
                //                        "','" + tbKeranjang.getValueAt(i, 4).toString() +
                //                        "','" + tbKeranjang.getValueAt(i, 2).toString()+
                //                        "','" + statuss_mb +
                //                        "','" + Type.Date +
                //                        "','" + tbKeranjang.getValueAt(i, 3).toString() +
                //                        "','" + denda +
                //                        "','" + keterangan + "')";
                //
                //                PreparedStatement pst1 = (PreparedStatement) conn.prepareStatement(sql1);
                //                pst1.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Data berhasil di tambah");

                        try{
            String NamaFile = "C:\\Users\\ndraa\\Documents\\NetBeansProjects\\Project\\src\\Report\\NotaSewa_1.jasper";
            String url = "jdbc:mysql://localhost:3306/";
            String dbName = "aswajatrans";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "root";
            String password = "";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection koneksi = DriverManager.getConnection(url+dbName,userName,password);
            HashMap param = new HashMap();
            
            param.put("Id_Sewa",String.valueOf(kd_nt.getText()));
            
            JasperPrint Jprint = JasperFillManager.fillReport(NamaFile,param,koneksi);
            JasperViewer.viewReport(Jprint,false);
//            JasperPrintManager.printReport(Jprint, false);
        
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
                getData();
                

            }

            kd_transaksi();
            Bersih();

            resetkeranjang();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + "Anda Harus Login Terlebih Dahulu");
            Login lo = new Login();
            lo.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_simpanActionPerformed

    private void TXT_STBYRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXT_STBYRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXT_STBYRActionPerformed

    private void TXT_KembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXT_KembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXT_KembalianActionPerformed

    private void sis_byrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sis_byrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sis_byrActionPerformed

    private void txtTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalHargaActionPerformed

    private void bayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_bayarKeyTyped

    private void bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarKeyReleased
        // TODO add your handling code here:
        try {
            //            int totsew = Integer.parseInt(lama_pinjam.getText());

            int byr = Integer.parseInt(bayar.getText());
            int tot = Integer.parseInt(txtTotalHarga.getText());

            //            int total_sewa = 0;
            //            total_sewa = totsew + totsew;
            //                        tot_sewa.setText(""+total_sewa);
            int fungsi = tot - byr;
            int lain = byr - tot;

            if (fungsi == 0) {
                TXT_STBYR.setText("lunas");
                sis_byr.setText("" + fungsi);
                TXT_Kembalian.setText("" + fungsi);
                //                            tot_sewa.setText(""+total_sewa);

            } else if (byr > tot) {
                TXT_STBYR.setText("lunas");
                sis_byr.setText("" + 0);
                TXT_Kembalian.setText("" + lain);

            } else if (fungsi == tot) {
                TXT_STBYR.setText("belum bayar");
                sis_byr.setText("" + fungsi);
                TXT_Kembalian.setText("0");

            } else if (fungsi < tot) {
                TXT_STBYR.setText("kurang");
                sis_byr.setText("" + fungsi);
                TXT_Kembalian.setText("" + 0);

            }
            //                        tot_sewa.setText(total_sewa);

        } catch (Exception e) {
            bayar.setText("");

        }
    }//GEN-LAST:event_bayarKeyReleased

    private void bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bayarActionPerformed

    private void nik_trsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nik_trsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nik_trsActionPerformed

    private void kd_ntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_ntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_ntActionPerformed

    private void tbKeranjangAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tbKeranjangAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tbKeranjangAncestorAdded

    private void txtSubTotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubTotalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubTotalKeyReleased

    private void txtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubTotalActionPerformed

    private void rSMaterialButtonRectangle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle4ActionPerformed
        // TODO add your handling code here:
        int jumlah = Integer.parseInt(lama_pinjam.getText());
        try {
            int roww = tbBarang.getSelectedRow();
            if ((jumlah == 0) && (lama_pinjam.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "Harap masukan jumlah barang dengan benar!");
            } else {
                boolean result = idList.contains(tbBarang.getValueAt(roww, 0).toString());

                if (result == false) {
                    idList.add(tbBarang.getValueAt(roww, 0).toString());
                    String[] bismillah = {tbBarang.getValueAt(roww, 0).toString(),
                        tbBarang.getValueAt(roww, 1).toString(),
                        lama_pinjam.getText(), txtSubTotal.getText(), tgl_sl.getText()
                    };
                    model1.addRow(bismillah);
                    int sew = tbKeranjang.getRowCount();
                    tot_sewa.setText("" + sew);
                    setTotalHarga();
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_rSMaterialButtonRectangle4ActionPerformed

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

    private void lama_pinjamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lama_pinjamKeyReleased
        // TODO add your handling code here:
        try {
            Integer.parseInt(lama_pinjam.getText());
        } catch (Exception e) {
            lama_pinjam.setText("");
        }

        int jml = Integer.parseInt(lama_pinjam.getText());
        int hrg = Integer.parseInt(txtHargaBarang.getText());
        int sb = 0;

        sb = jml * hrg;

        txtSubTotal.setText("" + sb);

        tanggal_kmbl();
    }//GEN-LAST:event_lama_pinjamKeyReleased

    private void lama_pinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lama_pinjamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lama_pinjamActionPerformed

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
            java.util.logging.Logger.getLogger(TransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new TransaksiSewa().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(TransaksiSewa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DatMob1;
    private javax.swing.JButton DatMob2;
    private javax.swing.JButton DatPel1;
    private javax.swing.JButton DatTranSew1;
    private javax.swing.JButton Log1;
    private javax.swing.JPanel Side_Bar;
    private javax.swing.JTextField TXT_Kembalian;
    private javax.swing.JTextField TXT_STBYR;
    private javax.swing.JButton TranSew1;
    private javax.swing.JTextField bayar;
    private javax.swing.JTextField cari;
    private javax.swing.JButton jButton11;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jumlah;
    private javax.swing.JTextField kd_nt;
    private javax.swing.JTextField lama_pinjam;
    private javax.swing.JTextField nik_trs;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle4;
    private rojerusan.RSMaterialButtonRectangle simpan;
    private javax.swing.JTextField sis_byr;
    private javax.swing.JTable tbBarang;
    private javax.swing.JTable tbKeranjang;
    private javax.swing.JLabel tgl1;
    private javax.swing.JTextField tgl_rental;
    private javax.swing.JTextField tgl_sl;
    private javax.swing.JTextField tot_sewa;
    private javax.swing.JLabel txtHargaBarang;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotalHarga;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
