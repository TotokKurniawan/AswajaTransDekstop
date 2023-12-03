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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class DataAdmin extends javax.swing.JFrame {
        
    /**
     * Creates new form Logout
     */

    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    public DataAdmin() {
        initComponents();
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        conn = Koneksi.Connect.KoneksiDB();
        this.setLocationRelativeTo(null);
        Tampil_Jam();
        Tampil_Tanggal();
//  txtid.setText(Session.getU_id());
  tampil();
        
    }
 public void tampil() {
    try {
        String sql = "SELECT * FROM user WHERE id = ?";
        Connection conn = Koneksi.Connect.KoneksiDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, Session.getU_id());
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String id = rs.getString("id");
            String username = rs.getString("Username");
            String nama = rs.getString("Nama");
            String tempatTanggalLahir = rs.getString("TempatTanggalLahir");
            String tanggalLahir = rs.getString("TanggalLahir");
            String fotoFilename = rs.getString("Foto"); // Kolom yang menyimpan nama file foto
            String noTelpon = rs.getString("NoTelpon");
            String hint = rs.getString("Hint");
            String jawabanHint = rs.getString("JawabanHint");
            String password = rs.getString("Password");

            txtnma.setText(nama);
            txtuser.setText(username);
            txtid.setText(id);
            txttanggal.setText(tanggalLahir);
            txthint.setText(hint);
            txtno.setText(noTelpon);
            txtjawab.setText(jawabanHint);
            txtpassword.setText(password);
            txtempat.setText(tempatTanggalLahir);
            
            // Menampilkan letak file foto pada txtfile
            txtfile.setText(fotoFilename);
            
            // Menampilkan foto jika nilai fotoFilename tidak null
            if (fotoFilename != null) {
                File fotoFile = new File(fotoFilename);

                if (fotoFile.exists()) {
                    ImageIcon imageIcon = new ImageIcon(fotoFile.getAbsolutePath());
                    Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
                    txtfoto.setIcon(new ImageIcon(image));
                } else {
                    // Jika file gambar tidak ditemukan
                    txtfoto.setIcon(null);
                    JOptionPane.showMessageDialog(null, "File gambar tidak ditemukan");
                }
            } else {
                // Jika nilai fotoFilename null, tidak ada foto yang ditampilkan
                txtfoto.setIcon(null);
            }
        }

        rs.close();
        pst.close();
        conn.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam mengakses database: " + e.getMessage());
    }
}





//   public void tampil() {
//    try {
//        String sql = "SELECT * FROM user where id ='"+Session.getU_id();
//        Connection conn = Koneksi.Connect.KoneksiDB();
//        PreparedStatement pst = conn.prepareStatement(sql);
//        ResultSet rs = pst.executeQuery();
//        
//        while (rs.next()) {
//        rs.getString("id");
//        rs.getString("Username");
//        rs.getString("Nama");
//        rs.getString("TempatTanggalLahir");
//        rs.getString("TanggalLahir");
//        rs.getString("Foto");
//        rs.getString("NoTelpon");
//        rs.getString("Hint");
//        rs.getString("JawabanHint");
//        rs.getString("Password");
//        }
//        txtnma.setText(sql);
//        txtuser.setText(sql);
//        txtid.setText(sql);
//        txttanggal.setText(sql);
//        txthint.setText(sql);
//        txtfile.setText(sql);
//        txtno.setText(sql);
//        txtjawab.setText(sql);
//        txtpassword.setText(sql);
//        txtpassword.setText(sql);
//        txtempat.setText(sql);
//        
//        rs.close();
//        pst.close();
//        conn.close();
//    } catch (SQLException e) {
//        JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam mengakses database: " + e.getMessage());
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
    new Timer(1000, taskPerformer).start();
    }   
 
public void Tampil_Tanggal() {
    java.util.Date tglsekarang = new java.util.Date();
    SimpleDateFormat smpdtfmt = new SimpleDateFormat("dd MMMMMMMMM yyyy", Locale.getDefault());
    String tanggal1 = smpdtfmt.format(tglsekarang);
    tgl.setText(tanggal1);}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        Dashboard1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        tgl3 = new javax.swing.JLabel();
        rSMaterialButtonRectangle1 = new rojerusan.RSMaterialButtonRectangle();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        tgl1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        waktu = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tgl = new javax.swing.JLabel();
        txtfoto = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Upload = new rojerusan.RSMaterialButtonRectangle();
        Upload1 = new rojerusan.RSMaterialButtonRectangle();
        txtuser = new javax.swing.JTextField();
        txtpassword = new javax.swing.JTextField();
        txthint = new javax.swing.JTextField();
        txtjawab = new javax.swing.JTextField();
        txtno = new javax.swing.JTextField();
        txttanggal = new javax.swing.JTextField();
        txtempat = new javax.swing.JTextField();
        txtnma = new javax.swing.JTextField();
        txtid = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtfile = new javax.swing.JTextField();
        Dashboard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tgl2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, -1, -1));

        Dashboard1.setBackground(new java.awt.Color(128, 128, 128));
        Dashboard1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setIcon(new javax.swing.ImageIcon("C:\\Users\\ndraa\\Documents\\SEMESTER 2\\Workhshop Pengembangan Aplikasi\\Gambar\\garis.png")); // NOI18N
        Dashboard1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 10));

        tgl3.setBackground(new java.awt.Color(51, 255, 51));
        tgl3.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        tgl3.setForeground(new java.awt.Color(255, 255, 255));
        tgl3.setText("ASWAJA TRANS");
        Dashboard1.add(tgl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, -1, -1));

        rSMaterialButtonRectangle1.setBackground(new java.awt.Color(0, 255, 0));
        rSMaterialButtonRectangle1.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonRectangle1.setText("Next");
        rSMaterialButtonRectangle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle1ActionPerformed(evt);
            }
        });
        Dashboard1.add(rSMaterialButtonRectangle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 20, 170, 50));

        rSMaterialButtonRectangle2.setBackground(new java.awt.Color(255, 51, 51));
        rSMaterialButtonRectangle2.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonRectangle2.setText("Kembali");
        rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle2ActionPerformed(evt);
            }
        });
        Dashboard1.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 170, 50));

        getContentPane().add(Dashboard1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, -1, 90));

        tgl1.setBackground(new java.awt.Color(255, 255, 204));
        tgl1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        waktu.setText("Waktu Sekarang :");
        jPanel2.add(waktu);

        tgl1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 10, 150, 30));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tgl.setText("Tanggal Sekarang :");
        jPanel1.add(tgl);

        tgl1.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, 30));

        txtfoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 3));
        tgl1.add(txtfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 210, 190));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Jawaban Hint");
        tgl1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 490, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Username");
        tgl1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 340, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("No Telpon");
        tgl1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 490, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Tanggal Lahir");
        tgl1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 440, -1, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Tempat Lahir");
        tgl1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 390, -1, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Nama Lengkap");
        tgl1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 340, -1, -1));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Password");
        tgl1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 390, -1, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Pertanyaan Hint");
        tgl1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 440, -1, -1));

        Upload.setBackground(new java.awt.Color(102, 255, 0));
        Upload.setForeground(new java.awt.Color(0, 0, 0));
        Upload.setText("Simpan");
        Upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UploadActionPerformed(evt);
            }
        });
        tgl1.add(Upload, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 220, 100, 40));

        Upload1.setBackground(new java.awt.Color(0, 51, 255));
        Upload1.setForeground(new java.awt.Color(0, 0, 0));
        Upload1.setText("Upload");
        Upload1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Upload1ActionPerformed(evt);
            }
        });
        tgl1.add(Upload1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 220, 110, 40));

        txtuser.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtuser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tgl1.add(txtuser, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 340, 150, -1));

        txtpassword.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtpassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tgl1.add(txtpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 390, 150, -1));

        txthint.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txthint.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txthint.setEnabled(false);
        tgl1.add(txthint, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 440, 150, -1));

        txtjawab.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtjawab.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tgl1.add(txtjawab, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 490, 150, -1));

        txtno.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnoKeyTyped(evt);
            }
        });
        tgl1.add(txtno, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 490, 150, -1));

        txttanggal.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txttanggal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txttanggal.setEnabled(false);
        tgl1.add(txttanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 440, 150, -1));

        txtempat.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtempat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtempat.setEnabled(false);
        tgl1.add(txtempat, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 390, 150, -1));

        txtnma.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtnma.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tgl1.add(txtnma, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 340, 150, -1));

        txtid.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtid.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtid.setEnabled(false);
        tgl1.add(txtid, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 300, 150, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("ID Admin");
        tgl1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 300, -1, -1));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Nama File");
        tgl1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 300, -1, -1));

        txtfile.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtfile.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtfile.setEnabled(false);
        tgl1.add(txtfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 300, 150, -1));

        getContentPane().add(tgl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1370, 570));

        Dashboard.setBackground(new java.awt.Color(153, 153, 153));
        Dashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\ndraa\\Documents\\SEMESTER 2\\Workhshop Pengembangan Aplikasi\\Gambar\\garis.png")); // NOI18N
        Dashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, -1, 20));

        tgl2.setBackground(new java.awt.Color(51, 255, 51));
        tgl2.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        tgl2.setForeground(new java.awt.Color(255, 255, 255));
        tgl2.setText("DATA ADMIN");
        Dashboard.add(tgl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        getContentPane().add(Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 90));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rSMaterialButtonRectangle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle1ActionPerformed
        // TODO add your handling code here:
        Dashboard das = new Dashboard();
        das.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonRectangle1ActionPerformed

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed
        // TODO add your handling code here:
               int option = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            Login log = new Login();
            log.setVisible(true);
                    this.dispose();

        }
    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void UploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UploadActionPerformed
try {
    java.sql.Connection conn = Koneksi.Connect.KoneksiDB();
    String sql = "UPDATE user SET Nama= ?, NoTelpon= ?, Username = ?, JawabanHint= ?, Password=?, Foto=? WHERE id= ?";
    PreparedStatement pst1 = conn.prepareStatement(sql);
    
    // Mendapatkan nilai dari input
    String nama = txtnma.getText();
    String noTelpon = txtno.getText();
    String username = txtuser.getText();
    String jawabanHint = txtjawab.getText();
    String password = txtpassword.getText();
    String fotoFilename = txtfile.getText();
    String id = txtid.getText();
    
    // Mengatur nilai parameter pada PreparedStatement
    pst1.setString(1, nama);
    pst1.setString(2, noTelpon);
    pst1.setString(3, username);
    pst1.setString(4, jawabanHint);
    pst1.setString(5, password);
    pst1.setString(6, fotoFilename);  // Mengatur nilai parameter 'foto' sebagai nama file atau path file gambar
    pst1.setString(7, id);
    
    pst1.executeUpdate();
    pst1.close();

    JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
    
    // Menampilkan kembali foto setelah pembaruan data
    
} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Data Gagal Disimpan: " + e.getMessage());
}

        
    }//GEN-LAST:event_UploadActionPerformed

    private void Upload1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Upload1ActionPerformed
        // TODO add your handling code here:
     JFileChooser fileChooser = new JFileChooser();
int result = fileChooser.showOpenDialog(this);

if (result == JFileChooser.APPROVE_OPTION) {
    File selectedFile = fileChooser.getSelectedFile();

    try {
        // Mengonversi file gambar menjadi tipe data byte[]
        FileInputStream fis = new FileInputStream(selectedFile);
        byte[] imageData = new byte[(int) selectedFile.length()];
        fis.read(imageData);
        fis.close();

        // Menyimpan data gambar dalam variabel foto untuk digunakan dalam penyimpanan ke basis data
        txtfile.setText(selectedFile.getAbsolutePath());

        // Menampilkan gambar di JLabel
        ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        txtfoto.setIcon(new ImageIcon(image));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat gambar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}




    }//GEN-LAST:event_Upload1ActionPerformed

    private void txtnoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnoKeyTyped
        // TODO add your handling code here:
        if (!Character.isDigit(evt.getKeyChar()) || txtno.getText().length() >= 13) {
            // akan dipanggil untuk mengonsumsi sebuah angka dan mencegah ditampilkan pada textfield
            evt.consume();
        }
    }//GEN-LAST:event_txtnoKeyTyped

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
            java.util.logging.Logger.getLogger(DataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Dashboard;
    private javax.swing.JPanel Dashboard1;
    private rojerusan.RSMaterialButtonRectangle Upload;
    private rojerusan.RSMaterialButtonRectangle Upload1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private javax.swing.JLabel tgl;
    private javax.swing.JPanel tgl1;
    private javax.swing.JLabel tgl2;
    private javax.swing.JLabel tgl3;
    private javax.swing.JTextField txtempat;
    private javax.swing.JTextField txtfile;
    private javax.swing.JLabel txtfoto;
    private javax.swing.JTextField txthint;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtjawab;
    private javax.swing.JTextField txtnma;
    private javax.swing.JTextField txtno;
    private javax.swing.JTextField txtpassword;
    private javax.swing.JTextField txttanggal;
    private javax.swing.JTextField txtuser;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
   
