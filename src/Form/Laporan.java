/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Form;

/**
 *
 * @author Totok Kurniawan
 */
import com.toedter.calendar.JMonthChooser;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class Laporan extends javax.swing.JFrame {

    /**
     * Creates new form Logout
     */
    private int totalSewa;
    private int totalPengeluaran;
    private int totalBayar;
    DefaultTableModel model1;

    public Laporan() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        loaddata();
        Tampil_Jam();
        Tampil_Tanggal();
//        TotalPendapatan();
//        TotalSewa();
//        TotalPengeluaran();
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
void loaddata() {
    try {
        String tampil = "SELECT sewa.*, mobil.MerkMobil, detail_sewa.Tgl_Kembali, pengeluaran.Nominal "
                + "FROM sewa "
                + "JOIN detail_sewa ON sewa.id_Sewa = detail_sewa.id_Sewa "
                + "JOIN mobil ON mobil.Nopol = detail_sewa.Nopol "
                + "JOIN pengeluaran ON mobil.Nopol = pengeluaran.Nopol";
        PreparedStatement ps = Koneksi.Connect.KoneksiDB().prepareStatement(tampil);
        ResultSet rs = ps.executeQuery();
        
        DefaultTableModel model1 = new DefaultTableModel();
        model1.addColumn("Kode Struk");
        model1.addColumn("Merk Mobil");
        model1.addColumn("Tgl Sewa");
        model1.addColumn("Tgl Kembali");
        model1.addColumn("Bayar");
        model1.addColumn("Total Harga");
        
        HashSet<String> kodeStrukSet = new HashSet<>(); // HashSet to store unique Kode Struk
        
        while (rs.next()) {
            String kodeStruk = rs.getString("id_Sewa");
            
            if (!kodeStrukSet.contains(kodeStruk)) {
                model1.addRow(new Object[]{
                    kodeStruk,
                    rs.getString("MerkMobil"),
                    rs.getDate("Tgl_sewa"),
                    rs.getDate("Tgl_Kembali"),
                    rs.getInt("bayar"),
                    rs.getInt("Total_Harga")
                });
                
                kodeStrukSet.add(kodeStruk); // Add Kode Struk to the HashSet
            }
        }
        
        tabellaporan.setModel(model1);
        int jumlahsew = tabellaporan.getRowCount();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Cek Kembali, " + e + "");
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

    public void TotalPendapatan() {
        int selectedMonth = bulannn.getMonth() + 1; // Mendapatkan bulan yang dipilih (dimulai dari 0)
        int selectedYear = jYearChooser1.getYear(); // Mendapatkan tahun yang dipilih

        try {
            String sql = "SELECT SUM(bayar) AS totalpemasukan, MONTH(Tgl_sewa) AS bulan FROM sewa WHERE MONTH(Tgl_sewa) = ? AND YEAR(Tgl_sewa) = ? GROUP BY bulan";
            Connection conn = Koneksi.Connect.KoneksiDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedMonth);
            pst.setInt(2, selectedYear);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String totalPemasukan = rs.getString("totalpemasukan");
                txtpendapatan.setText(totalPemasukan);
            } else {
                txtpendapatan.setText("0");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
public void TotalPengeluaran() {
    int selectedMonth = bulannn.getMonth() + 1; // January is represented by 0, so adding 1 to get the actual month
    int selectedYear = jYearChooser1.getYear();
    
    try {
        String sql = "SELECT SUM(Nominal) AS totalpengeluaran FROM pengeluaran WHERE MONTH(Tgl_Pengeluaran) = ? AND YEAR(Tgl_Pengeluaran) = ?";

        Connection conn = Koneksi.Connect.KoneksiDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, selectedMonth);
        pst.setInt(2, selectedYear);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("totalpengeluaran");
            txtpengeluaran.setText(Integer.toString(total));
        } else {
            txtpengeluaran.setText("0");
        }

        rs.close();
        pst.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}



    public void TotalSewa() {
    int selectedMonth = bulannn.getMonth() + 1; // January is represented by 0, so adding 1 to get the actual month
    int selectedYear = jYearChooser1.getYear();
    
    try {
        String sql = "SELECT COUNT(Id_sewa) AS totalsewa FROM sewa WHERE MONTH(Tgl_sewa) = ? AND YEAR(Tgl_sewa) = ?";

        Connection conn = Koneksi.Connect.KoneksiDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, selectedMonth);
        pst.setInt(2, selectedYear);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            String tot = rs.getString("totalsewa");
            txtsewa.setText(tot);
        } else {
            txtsewa.setText("0");
        }

        rs.close();
        pst.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void cari() {
    DefaultTableModel model2 = (DefaultTableModel) tabellaporan.getModel();
    model2.getDataVector().removeAllElements();
    String nggolek = "SELECT sewa.*, mobil.MerkMobil,detail_sewa.Tgl_Kembali,pengeluaran.Nominal "
            + "FROM sewa "
            + "JOIN detail_sewa ON sewa.id_Sewa = detail_sewa.id_Sewa "
            + "JOIN mobil ON mobil.Nopol = detail_sewa.Nopol "
            + "JOIN pengeluaran ON mobil.Nopol = pengeluaran.Nopol "
            + "WHERE sewa.id_Sewa LIKE '%" + cari.getText() + "%' "
            + "OR sewa.bayar LIKE '%" + cari.getText() + "%' "
            + "OR sewa.StatusBayar LIKE '%" + cari.getText() + "%' "
            + "OR sewa.NIK LIKE '%" + cari.getText() + "%' "
            + "OR pengeluaran.Nominal LIKE '%" + cari.getText() + "%'";

    try {
        Connection conn = Koneksi.Connect.KoneksiDB();
        Statement s = conn.createStatement();
        ResultSet res = s.executeQuery(nggolek);
        while (res.next()) {
            Object[] obj = new Object[8];
            obj[0] = res.getString("id_Sewa");
            obj[1] = res.getString("MerkMobil");
            obj[2] = res.getDate("Tgl_sewa");
            obj[3] = res.getDate("Tgl_Kembali");
            obj[4] = res.getInt("bayar");
            obj[5] = res.getInt("Total_Harga");
            obj[6] = res.getInt("Total_Sewa");
            obj[7] = res.getString("NIK");
            
            model2.addRow(obj);
        }
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
        jLabel8 = new javax.swing.JLabel();
        rSMaterialButtonRectangle1 = new rojerusan.RSMaterialButtonRectangle();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaporan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cari = new javax.swing.JTextField();
        bulannn = new com.toedter.calendar.JMonthChooser();
        jPanel3 = new javax.swing.JPanel();
        tgl1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        txtpendapatan = new javax.swing.JLabel();
        txtsewa = new javax.swing.JLabel();
        txtpengeluaran = new javax.swing.JLabel();
        Side_Bar = new javax.swing.JPanel();
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

        jLabel7.setBackground(java.awt.SystemColor.controlLtHighlight);
        jLabel7.setFont(new java.awt.Font("Arial Black", 3, 36)); // NOI18N
        jLabel7.setText("Welcome to Re-Mob");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

        jLabel8.setBackground(java.awt.SystemColor.controlLtHighlight);
        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
        jLabel8.setText("Laporan");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(487, 91, -1, -1));

        rSMaterialButtonRectangle1.setBackground(new java.awt.Color(153, 153, 153));
        rSMaterialButtonRectangle1.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonRectangle1.setText("Kembali");
        rSMaterialButtonRectangle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonRectangle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 660, 160, 50));

        tabellaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabellaporan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabellaporan.setRequestFocusEnabled(false);
        tabellaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaporanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabellaporan);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 1100, 270));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel9.setText("Total Pendapatan ");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, -1, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel11.setText("Total Sewa");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 180, -1, -1));

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cariKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariKeyReleased(evt);
            }
        });
        jPanel1.add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, 240, 30));

        bulannn.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                bulannnPropertyChange(evt);
            }
        });
        jPanel1.add(bulannn, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 270, -1, 30));

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

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel13.setText("Total Pengeluaran");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 180, -1, -1));

        jYearChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jYearChooser1PropertyChange(evt);
            }
        });
        jPanel1.add(jYearChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 270, 90, 30));

        txtpendapatan.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtpendapatan.setText("0");
        jPanel1.add(txtpendapatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, -1, -1));

        txtsewa.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtsewa.setText("0");
        jPanel1.add(txtsewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 220, -1, -1));

        txtpengeluaran.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        txtpengeluaran.setText("0");
        jPanel1.add(txtpengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 220, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 1150, 750));

        Side_Bar.setBackground(new java.awt.Color(105, 105, 105));
        Side_Bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jLabel6.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/garis panjang.png"))); // NOI18N
        Side_Bar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 0, -1, 756));

        txtfoto.setFont(new java.awt.Font("Arial Black", 3, 18)); // NOI18N
        txtfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtfoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/profile.png"))); // NOI18N
        Side_Bar.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

        getContentPane().add(Side_Bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Log1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Log1ActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
                    this.dispose();

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
            DatPel.setVisible(true);
        this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(DataPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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

    private void bulannnPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_bulannnPropertyChange
if (evt.getPropertyName().equals("month") || evt.getPropertyName().equals("year")) {
    if (bulannn.getMonth() != -1 && jYearChooser1.getYear() != -1) {
        DefaultTableModel detail = new DefaultTableModel();
        detail.addColumn("Kode Struk");
        detail.addColumn("Merk Mobil");
        detail.addColumn("Tgl Sewa");
        detail.addColumn("Tgl Kembali");
        detail.addColumn("Bayar");
        detail.addColumn("Total Harga");

        try {
            int selectedMonth = bulannn.getMonth() + 1; // January is represented by 0, so adding 1 to get the actual month
            int selectedYear = jYearChooser1.getYear();
            String cari = "SELECT sewa.id_Sewa, mobil.MerkMobil, sewa.Tgl_sewa, detail_sewa.Tgl_Kembali, sewa.bayar, sewa.Total_Harga FROM sewa JOIN detail_sewa ON sewa.id_Sewa = detail_sewa.id_Sewa JOIN mobil ON mobil.Nopol = detail_sewa.Nopol WHERE MONTH(Tgl_sewa) = ? AND YEAR(Tgl_sewa) = ? ORDER BY Tgl_sewa ASC;";
            PreparedStatement ps = Koneksi.Connect.KoneksiDB().prepareStatement(cari);
            ps.setInt(1, selectedMonth);
            ps.setInt(2, selectedYear);
            ResultSet rs = ps.executeQuery();

            // Reset total values
            totalBayar = 0;
            totalSewa = 0;
            totalPengeluaran = 0;

            while (rs.next()) {
                detail.addRow(new Object[]{
                    rs.getString("id_Sewa"),
                    rs.getString("MerkMobil"),
                    rs.getDate("Tgl_sewa"),
                    rs.getDate("Tgl_Kembali"),
                    rs.getInt("bayar"),
                    rs.getInt("Total_Harga")
                });
            }
            
            tabellaporan.setModel(detail);
            TotalPendapatan();
            TotalPengeluaran();
            TotalSewa();
//                        loaddata();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cek kembali: " + e.getMessage());
        }
    }}
//loaddata();


        

//    if (evt.getPropertyName().equals("month")) {
//        if (bulannn.getMonth() != -1) { // MonthChooser returns -1 for uninitialized month
//            DefaultTableModel detail = new DefaultTableModel();
//            detail.addColumn("Tanggal");
//            detail.addColumn("Supplier");
//            detail.addColumn("Nama Barang");
//            detail.addColumn("Jumlah");
//            detail.addColumn("Harga Beli");
//            detail.addColumn("subTotal");
//            detail.addColumn("Garansi");
//            try {
//                int selectedMonth = bulannn.getMonth() + 1; // January is represented by 0, so adding 1 to get the actual month
//                String cari = "SELECT * FROM sewa WHERE MONTH(Tgl_sewa) = ? ORDER BY Tgl_sewa ASC";
//                PreparedStatement ps = Koneksi.Connect.KoneksiDB().prepareStatement(cari);
//                ps.setInt(1, selectedMonth);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    detail.addRow(new Object[]{
//                        rs.getString("id_Sewa"),
//                        rs.getDate("Tgl_sewa"),
//                        rs.getInt("bayar"),
//                        rs.getInt("Sisa yang harus dibayar"),
//                        rs.getString("StatusBayar"),
//                        rs.getInt("Total_Harga"),
//                        rs.getInt("Total_Sewa"),
//                        rs.getString("NIK"),
//                        rs.getInt("id")
//                    });
//                    totalBayar += rs.getInt("bayar");
//                    totalSewa += rs.getInt("Total_Sewa");
//                    totalPengeluaran += rs.getInt("Total_Harga");
//                }
//                tabellaporan.setModel(detail);
//                txtpendapatan.setText(String.valueOf(totalBayar));
//                jumlahsewa.setText(String.valueOf(totalSewa));
//                txtpengeluaran.setText(String.valueOf(totalPengeluaran));
//                   
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "Cek kembali: " + e.getMessage());
//            }}}
//        
//        if(bulannn.getMonth()+1 != 0){
//            DefaultTableModel detail = new DefaultTableModel();
//            detail.addColumn("Tanggal");
//            detail.addColumn("Supplier");
//            detail.addColumn("Nama Barang");
//            detail.addColumn("Jumlah");
//            detail.addColumn("Harga Beli");
//            detail.addColumn("subTotal");
//            detail.addColumn("Garansi");
//            try{
//                String cari = "SELECT * from sewa WHERE Month(Tgl_sewa) = '"+(bulannn.getMonth() + 1)+"' ORDER BY Tgl_sewa ASC;";
//                PreparedStatement                ps = Koneksi.Connect.KoneksiDB().prepareStatement(cari);
//                ResultSet rs = ps.executeQuery();
//                while(rs.next()){
//                    detail.addRow(new Object[]{
//                        rs.getString("id_Sewa"),
//                        rs.getDate("Tgl_sewa"),
//                        rs.getInt("bayar"),
//                        rs.getInt("Sisa yang harus dibayar"),
//                        rs.getString("StatusBayar"),
//                        rs.getInt("Total_Harga"),
//                        rs.getInt("Total_Sewa"),
//                        rs.getString("NIK"),
//                        rs.getInt("id")
//                    });
//                    tabellaporan.setModel(detail);
//                }
//            }catch(Exception e){
//                JOptionPane.showMessageDialog(null, "cek Kembali "+e+"");
//            }
//        }
    }//GEN-LAST:event_bulannnPropertyChange

    private void cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyReleased
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_cariKeyReleased

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariActionPerformed

    private void tabellaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaporanMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabellaporanMouseClicked

    private void rSMaterialButtonRectangle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle1ActionPerformed
        // TODO add your handling code here:
        Dashboard dash = new Dashboard();
        dash.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonRectangle1ActionPerformed

    private void cariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariKeyPressed

    private void cariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cariFocusGained
        // TODO add your handling code here:
        String cari1 = cari.getText();
        if (cari1.equals("Pencarian")) {
            cari.setText("");
        }
    }//GEN-LAST:event_cariFocusGained

    private void cariFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cariFocusLost
        // TODO add your handling code here:
        String cari1 = cari.getText();
        if (cari1.equals("") | cari1.equals("Pencarian")) {
            cari.setText("Pencarian");
        }
    }//GEN-LAST:event_cariFocusLost

    private void jYearChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jYearChooser1PropertyChange
        // TODO add your handling code here:
        // TODO add your handling code here:
        DefaultTableModel detail = new DefaultTableModel();

        detail.addColumn("Kode Struk");
        detail.addColumn("Merk Mobil");
        detail.addColumn("Tgl Sewa");
        detail.addColumn("Tgl Kembali");
        detail.addColumn("Bayar");
        detail.addColumn("Total Harga");

        try {
            String cari = "SELECT sewa.id_Sewa, mobil.MerkMobil, sewa.Tgl_sewa, detail_sewa.Tgl_Kembali, sewa.bayar, sewa.Total_Harga FROM sewa JOIN detail_sewa ON sewa.id_Sewa = detail_sewa.id_Sewa JOIN mobil ON mobil.Nopol = detail_sewa.Nopol WHERE YEAR(sewa.Tgl_sewa) = ? AND MONTH(sewa.Tgl_sewa) = ? ORDER BY sewa.Tgl_sewa ASC;";
            PreparedStatement ps = Koneksi.Connect.KoneksiDB().prepareStatement(cari);
            ps.setInt(1, jYearChooser1.getYear());
            ps.setInt(2, bulannn.getMonth() + 1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                detail.addRow(new Object[]{
                    rs.getString("id_Sewa"),
                    rs.getString("MerkMobil"),
                    rs.getDate("Tgl_sewa"),
                    rs.getDate("Tgl_Kembali"),
                    rs.getInt("bayar"),
                    rs.getInt("Total_Harga")
                });
            }
            tabellaporan.setModel(detail);
            TotalPendapatan();
            TotalPengeluaran();
            TotalSewa();
//                        loaddata();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cek kembali " + e.getMessage());
        }
    }//GEN-LAST:event_jYearChooser1PropertyChange

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
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Laporan().setVisible(true);
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
    private javax.swing.JButton TranSew1;
    private com.toedter.calendar.JMonthChooser bulannn;
    private javax.swing.JTextField cari;
    private javax.swing.JButton jButton11;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle1;
    private javax.swing.JTable tabellaporan;
    private javax.swing.JLabel tgl1;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JLabel txtpendapatan;
    private javax.swing.JLabel txtpengeluaran;
    private javax.swing.JLabel txtsewa;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
