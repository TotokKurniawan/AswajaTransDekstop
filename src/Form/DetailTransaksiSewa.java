/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form;

/**
 *
 * @author Totok Kurniawan
 */
import com.mysql.jdbc.Connection;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class DetailTransaksiSewa extends javax.swing.JFrame {

    /**
     * Creates new form Logout
     */
    DefaultTableModel model1;

    public DetailTransaksiSewa() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//

        model1 = (DefaultTableModel) tabledetail.getModel();
        model1.addColumn("id_Sewa");
        model1.addColumn("Nopol");
        model1.addColumn("NIK");
        model1.addColumn("Nama_Pelanggan");
        model1.addColumn("Tgl_Kembali");
        model1.addColumn("Lama_Pinjam");
        model1.addColumn("StatusMobil");
        model1.addColumn("tanggal_pengembalian");
        model1.addColumn("subtotal");
        model1.addColumn("Denda");
        model1.addColumn("Keterangan");

        getData();
        Tampil_Jam();
        Tampil_Tanggal();
        TampilFoto();
    }
public void TampilFoto() {
    try {
        String sql = "SELECT Foto FROM user WHERE id = ?";
        java.sql.Connection conn = Koneksi.Connect.KoneksiDB();
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
     public void Tampil_Jam() {
        ActionListener taskPerformer = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                String nol_jam = "", nol_menit = "", nol_detik = "";

                java.util.Date dateTime = new java.util.Date();
                int nilai_jam = dateTime.getHours();
                int nilai_menit = dateTime.getMinutes();
                int nilai_detik = dateTime.getSeconds();

                if (nilai_jam <= 9) {
                    nol_jam = "0";
                }
                if (nilai_menit <= 9) {
                    nol_menit = "0";
                }
                if (nilai_detik <= 9) {
                    nol_detik = "0";
                }

                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);

                waktu.setText(jam + ":" + menit + ":" + detik + "");
            }
        };
        new Timer(1000, taskPerformer).start();
    }

    public void Tampil_Tanggal() {
        java.util.Date tglsekarang = new java.util.Date();
        SimpleDateFormat smpdtfmt = new SimpleDateFormat("dd MMMMMMMMM yyyy", Locale.getDefault());
        String tanggal1 = smpdtfmt.format(tglsekarang);
        tgl1.setText(tanggal1);
    }

    public void getData() {

        model1.getDataVector().removeAllElements();

        try {
            //membuat statemen untuk memanggil data table tabel_buku
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
            Statement stat = conn.createStatement();
            String sql = "SELECT detail_sewa.*, pelanggan.NIK, pelanggan.Nama_Pelanggan, mobil.Nopol, mobil.MerkMobil,mobil.Status\n"
                    + "FROM detail_sewa\n"
                    + "JOIN sewa ON detail_sewa.id_sewa = sewa.id_sewa\n"
                    + "JOIN pelanggan ON pelanggan.NIK = sewa.NIK\n"
                    + "JOIN mobil ON mobil.Nopol = detail_sewa.Nopol where sewa.id_Sewa='"+Session.getU_KodeStruk()+"'";
            ResultSet res = stat.executeQuery(sql);

            //pengecekan terhadap data tabel_buku
            while (res.next()) {
                Object[] obj = new Object[11];
                obj[0] = res.getString("id_Sewa");
                obj[1] = res.getString("Nopol");
                obj[2] = res.getString("NIK");
                obj[3] = res.getString("Nama_Pelanggan");
                obj[4] = res.getString("Tgl_Kembali");
                obj[5] = res.getString("Lama_Pinjam");
                obj[6] = res.getString("Status");
                obj[7] = res.getString("tanggal_pengembalian");
                obj[8] = res.getString("subtotal");
                obj[9] = res.getString("Denda");
                obj[10] = res.getString("Keterangan");
//                ;
                model1.addRow(obj);
            }

        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage());
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
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabledetail = new javax.swing.JTable();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        kd_nt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        kd_nt2 = new javax.swing.JTextField();
        den = new javax.swing.JTextField();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        tgl_pgl = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        ket = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        kd_nt1 = new javax.swing.JLabel();
        tgl_kbl2 = new javax.swing.JTextField();
        Nop = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tgl1 = new javax.swing.JLabel();
        tgl_sl1 = new javax.swing.JTextField();
        tgl_pb1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
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
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 69, 1156, 16));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Arial Black", 3, 36)); // NOI18N
        jLabel7.setText("Welcome to Re-Mob");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel19.setText("Detail Sewa Mobil");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, -1, -1));

        tabledetail.setModel(new javax.swing.table.DefaultTableModel(
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
        });
        tabledetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabledetailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabledetail);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 144, 1064, 310));

        rSMaterialButtonRectangle2.setBackground(new java.awt.Color(0, 255, 0));
        rSMaterialButtonRectangle2.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonRectangle2.setText("Simpan");
        rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 670, 115, 50));

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setText("Keterangan");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 570, -1, -1));

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Tanggal Pengembalian  ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 570, -1, -1));

        kd_nt.setBackground(new java.awt.Color(0, 0, 0));
        kd_nt.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        kd_nt.setText("Tanggal Kembali ");
        jPanel1.add(kd_nt, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 530, -1, -1));

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Denda");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, -1, -1));

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setText(":");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 490, -1, -1));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setText(":");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, -1, -1));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setText(":");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 530, -1, -1));

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setText(":");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 570, -1, -1));

        kd_nt2.setEnabled(false);
        kd_nt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_nt2ActionPerformed(evt);
            }
        });
        jPanel1.add(kd_nt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 490, 180, -1));

        den.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                denActionPerformed(evt);
            }
        });
        jPanel1.add(den, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 530, 220, -1));

        rSMaterialButtonRectangle3.setBackground(new java.awt.Color(204, 204, 204));
        rSMaterialButtonRectangle3.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonRectangle3.setText("Kembali");
        rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonRectangle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 670, 115, 50));
        jPanel1.add(tgl_pgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 570, 180, -1));

        ket.setColumns(20);
        ket.setRows(5);
        jScrollPane2.setViewportView(ket);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 560, 220, 110));

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setText(":");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 530, -1, -1));

        kd_nt1.setBackground(new java.awt.Color(0, 0, 0));
        kd_nt1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        kd_nt1.setText("Kode Struk");
        jPanel1.add(kd_nt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 490, -1, -1));

        tgl_kbl2.setEnabled(false);
        tgl_kbl2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgl_kbl2ActionPerformed(evt);
            }
        });
        jPanel1.add(tgl_kbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 530, 180, -1));

        Nop.setEnabled(false);
        jPanel1.add(Nop, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 490, 220, -1));

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setText("Nopol");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 490, -1, -1));

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText(":");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 490, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        waktu.setBackground(new java.awt.Color(255, 255, 255));
        waktu.setText("Waktu Sekarang :");
        jPanel2.add(waktu);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 180, 30));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tgl1.setBackground(new java.awt.Color(255, 255, 255));
        tgl1.setText("Tanggal Sekarang :");
        jPanel3.add(tgl1);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 90, 180, 30));

        tgl_sl1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgl_sl1ActionPerformed(evt);
            }
        });
        jPanel1.add(tgl_sl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 530, 180, -1));
        jPanel1.add(tgl_pb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 570, 180, -1));

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setText(":");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 530, -1, -1));

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setText(":");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, -1, -1));

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel20.setText("Tanggal Pengembalian  ");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 570, -1, -1));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(220, 0, 1150, 750);

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
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis panjang.png"))); // NOI18N
        Side_Bar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 0, -1, 756));

        txtfoto.setFont(new java.awt.Font("Arial Black", 3, 18)); // NOI18N
        txtfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtfoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/profile.png"))); // NOI18N
        Side_Bar.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

        getContentPane().add(Side_Bar);
        Side_Bar.setBounds(0, 0, 220, 750);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed
        String KD = kd_nt2.getText();
        String tanggal_selesai = tgl_kbl2.getText();
        Date tanggal_Pengembalian = tgl_pgl.getDate();
//        String status = (String) status2.getSelectedItem();
        int denda = Integer.parseInt(den.getText());
        String Keterangan = ket.getText();
        String nopok = Nop.getText();

        try {
            java.sql.Connection conn = Koneksi.Connect.KoneksiDB();
            String sql = "UPDATE detail_sewa SET Tgl_Kembali = ?, tanggal_pengembalian = ?, denda = ?, Keterangan = ? WHERE Nopol = ?";
            PreparedStatement pst1 = conn.prepareStatement(sql);
            pst1.setString(1, tanggal_selesai);
//            pst1.setString(2, status);
            pst1.setDate(2, new java.sql.Date(tanggal_Pengembalian.getTime()));
            pst1.setInt(3, denda);
            pst1.setString(4, Keterangan);
            pst1.setString(5, nopok);
            
            pst1.executeUpdate();
            
            getData();
            pst1.close();

            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan" + e.getMessage());
        }

    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void denActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_denActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_denActionPerformed

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        // TODO add your handling code here:
        DataSewa DatTranSew = new DataSewa();
        DatTranSew.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void kd_nt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_nt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_nt2ActionPerformed

    private void tgl_sl1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_sl1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tgl_sl1ActionPerformed

    private void tabledetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledetailMouseClicked
        // TODO add your handling code here:
        int baris = tabledetail.rowAtPoint(evt.getPoint());

        String tanggal_kembali = tabledetail.getValueAt(baris, 4).toString();
        tgl_kbl2.setText(tanggal_kembali);

        String kode = tabledetail.getValueAt(baris, 0).toString();
        kd_nt2.setText(kode);

        String nopol = tabledetail.getValueAt(baris, 1).toString();
        Nop.setText(nopol);

//        String tanggal_pengembalian = tabledetail.getValueAt(baris, 1).toString();
//        tgl_pgl.setText(tanggal_pengembalian);
//
//        String status = tabledetail.getValueAt(baris, 6).toString();
//        status.(status);
        String denda = tabledetail.getValueAt(baris, 9).toString();
        den.setText(denda);

        String keterangan = tabledetail.getValueAt(baris, 10).toString();
        ket.setText(keterangan);

    }//GEN-LAST:event_tabledetailMouseClicked

    private void tgl_kbl2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_kbl2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tgl_kbl2ActionPerformed

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
            dispose();
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
            java.util.logging.Logger.getLogger(DetailTransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetailTransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetailTransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetailTransaksiSewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetailTransaksiSewa().setVisible(true);
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
    private javax.swing.JTextField Nop;
    private javax.swing.JPanel Side_Bar;
    private javax.swing.JButton TranSew1;
    private javax.swing.JTextField den;
    private javax.swing.JButton jButton11;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel kd_nt;
    private javax.swing.JLabel kd_nt1;
    private javax.swing.JTextField kd_nt2;
    private javax.swing.JTextArea ket;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private javax.swing.JTable tabledetail;
    private javax.swing.JLabel tgl1;
    private javax.swing.JTextField tgl_kbl2;
    private javax.swing.JTextField tgl_pb1;
    private com.toedter.calendar.JDateChooser tgl_pgl;
    private javax.swing.JTextField tgl_sl1;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
