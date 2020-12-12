/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formkasir;
import db.Koneksi;
import db.Parameter;
import db.SetTable;
import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.Statement;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author HADIATNA
 */
public class Transaksi extends javax.swing.JInternalFrame {
    ResultSet st;
    Koneksi con;
    int old, dec, now;
    String newstock;
    DefaultTableModel tabmodel;
    JasperReport report;
    JasperPrint print;
    Map param = new HashMap();
    JasperDesign desain;
    /**
     * Creates new form Transaksi
     */
    public Transaksi() {
       con = new db.Koneksi(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
        initComponents();
        getTable();
        kdtransaksi();
        getView();
        KodeTransaksi.setEditable(false);
        Cetak.setEnabled(false);
        kondisikd();
    }
    
     public void getTable() {
        String column[] = {"kode_obat", "nama_obat", "kategori_obat", "jenis_obat", "merek_obat", "harga_beli", "harga_jual", "jumlah_obat", "tanggal_masuk", "expired"};
        st = con.querySelect(column, "tb_obat");
        TableObat.setModel(new SetTable(st));
    }

    public void getView() {
        String column[] = {"id_jual","kode_transaksi", "kode_obat", "nama_obat", "merek_obat", "harga_jual", "jumlah_jual", "total_harga"};
        st = con.fcSelectCommand(column, "tb_penjualan", "kode_transaksi='" + KodeTransaksi.getText() + "'");
        TableTransaksi.setModel(new SetTable(st));
    }
    private void kdtransaksi(){ 
       String kode=null;
      String s, s1;
      Integer j;
      Integer panjang = 3;
        try{
            String sql="select max(right(kode_transaksi, 3)) from tb_penjualan";
            st = con.eksekusiQuery(sql);
            if(st.first()==false){
                kode="TRANS001";
            }else{
                st.last();
                s=Integer.toString(st.getInt(1)+1);
                j=s.length();
                s1="";
                for(int i=0; i<=panjang-j; i++){
                    s1=s1+"0";
                    kode="TRANS"+s1+s;
                    KodeTransaksi.setText(kode);
        }
        }
    }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }
    

    
    public void getSearchMedicine() {
        if (TextSearch.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Masukkan yang anda cari !!!");
        } else {
            if (ComboBoxSearch.getSelectedItem().equals("Kode Obat")) {
                st = con.querySelectAll("tb_obat", "kode_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Nama Obat")) {
                st = con.querySelectAll("tb_obat", "nama_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Kategori Obat")) {
                st = con.querySelectAll("tb_obat", "kategori_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Jenis Obat")) {
                st = con.querySelectAll("tb_obat", "jenis_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Merek Obat")) {
                st = con.querySelectAll("tb_obat", "merek_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableObat.setModel(new SetTable(st));
            }
        }
    }
    
    public void getAddCart() {
        if (KodeObat.getText().equals("") || NamaObat.getText().equals("")
                || MerekObat.getText().equals("") || HargaJualObat.getText().equals("") || JumlahBeli.getText().equals("") || LSubTotal.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan lengkapi data terlebih dahulu !!! ");
        } else {
            
            try {
                if (!getCheck_stock()) {
                    JOptionPane.showMessageDialog(this, "Stock tidak mencukupi");
                    } else  {
                    
                    String kd = null;
                try {
                    st = con.querySelectAll("tb_penjualan", "kode_transaksi='" + KodeTransaksi.getText() + "' and kode_obat='" + KodeObat.getText() + "'");
                    while (st.next()) {
                    kd = st.getString("kode_obat");
                        }
                }catch (SQLException ex) {
                Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
                 }
                
                if(kd== null){
                String[] column = {"kode_transaksi", "kode_obat", "nama_obat", "merek_obat", "harga_jual", "jumlah_jual", "total_harga"};
                String[] value = {KodeTransaksi.getText(), KodeObat.getText(), NamaObat.getText(), MerekObat.getText(), HargaJualObat.getText(), JumlahBeli.getText(), LSubTotal.getText()};
                System.out.println(con.queryInsert("tb_penjualan", column, value));
                    getMin();
                    getTable();
                    }   else {
                       kondisiobat();
                    }
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
            }
            getSubTotal();
            getTotal();
            getView();
            getCancel();
           
        }
    }
    
    public boolean getCheck_stock() throws SQLException {
        boolean checkstock;
        st = con.querySelectAll("tb_obat", "kode_obat='" + KodeObat.getText() + "'");
        while (st.next()) {
            old = st.getInt("jumlah_obat");
        }
        dec = Integer.parseInt(JumlahBeli.getText());
        if (old < dec) {
            checkstock = false;
        } else {
            checkstock = true;
        }
        return checkstock;
    }
    
    public void getMin() throws SQLException {
        st = con.querySelectAll("tb_obat", "kode_obat='" + KodeObat.getText() + "'");
        while (st.next()) {
            old = st.getInt("jumlah_obat");
        }
        dec = Integer.parseInt(JumlahBeli.getText());
        now = old - dec;
        newstock = Integer.toString(now);
        String a = String.valueOf(newstock);
        String[] kolom = {"jumlah_obat"};
        String[] isi = {a};
        System.out.println(con.queryUpdate("tb_obat", kolom, isi, "kode_obat='" + KodeObat.getText() + "'"));
    }
    
   
    
    
    public void getMouseClick() {
        
        KodeObat.setText(String.valueOf(TableObat.getValueAt(TableObat.getSelectedRow(), 0)));
        NamaObat.setText(String.valueOf(TableObat.getValueAt(TableObat.getSelectedRow(), 1)));
        MerekObat.setText(String.valueOf(TableObat.getValueAt(TableObat.getSelectedRow(), 4)));
        HargaJualObat.setText(String.valueOf(TableObat.getValueAt(TableObat.getSelectedRow(), 6)));
        JumlahBeli.setText("");
        LSubTotal.setText("");
        JumlahBeli.setEditable(true);
        
    }
    public void getMouseClick1() {
        
        KodeObat.setText(String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 2)));
        NamaObat.setText(String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 3)));
        MerekObat.setText(String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 4)));
        HargaJualObat.setText(String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 5)));
        JumlahBeli.setText(String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 6)));
        LSubTotal.setText(String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 7)));
        JumlahBeli.setEditable(false);
    }
    
    public void getSubTotal() {
        Integer a = Integer.parseInt(HargaJualObat.getText());
        Integer b = Integer.parseInt(JumlahBeli.getText());
        Integer c = a * b;
        LSubTotal.setText(String.valueOf(c));
        
    }
    
    public void getTotal() {
        st = con.eksekusiQuery("SELECT SUM(total_harga) as total_harga FROM tb_penjualan WHERE kode_transaksi = '" + KodeTransaksi.getText() + "'");
        try {
            st.next();
            LSubTotal1.setText(st.getString("total_harga"));
        } catch (SQLException ex) {
            Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void getPaymen() {
        int a = Integer.parseInt(LSubTotal1.getText());
        int b = Integer.parseInt(Tunai.getText());
        int c = b - a;
         
        Kembalian.setText(Integer.toString(c)); 
        }

    public void getCancel() {
        KodeObat.setText(null);
        NamaObat.setText(null);
        MerekObat.setText(null);
        HargaJualObat.setText(null);
        JumlahBeli.setText(null);
        LSubTotal.setText(null);
    }
    
    public void kondisikd(){
        String kd = null;
    try {
            st = con.querySelectAll("tb_penjualan", "kode_transaksi='" + KodeTransaksi.getText() + "'");
               while (st.next()) {
                kd = st.getString("kode_transaksi");
               }
        }catch (SQLException ex) {
                Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
            } 
                if(kd == null){
                Delete1.setEnabled(false);
                Cetak.setEnabled(false);
            }    else {
                 Delete1.setEnabled(true);   
                }
    }
    
    public void kondisiobat(){
    
       st = con.querySelectAll("tb_penjualan", "kode_transaksi='" + KodeTransaksi.getText() + "' and kode_obat='" + KodeObat.getText() + "'");
                        
                    try {
                        while (st.next()) {
                        old = st.getInt("jumlah_jual");
                        int inc = Integer.parseInt(JumlahBeli.getText());
                        now = old + inc;
                        newstock = Integer.toString(now);
                        String a = String.valueOf(newstock);
                        String[] kolom = {"jumlah_jual"};
                        String[] isi = {a};
                        System.out.println(con.queryUpdate("tb_penjualan", kolom, isi, "kode_obat='" + KodeObat.getText() + "'"));
      
                            }
                    } catch (SQLException ex) {
                    Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }    
    
    
     public void getDelete() {
         
        String id = String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 0));
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini", "Peringatan!!!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    getPlus();
                    con.queryDelete("tb_penjualan", "id_jual=" + id);
                } else {
                    return;
                }
                getCancel();
                getTable();
                getView();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                getTotal();               
                
            }   
      public void getPlus()  {
       st = con.querySelectAll("tb_obat", "kode_obat='" + KodeObat.getText() + "'");
    try {
        while (st.next()) {
            old = st.getInt("jumlah_obat");
            int inc = Integer.parseInt(JumlahBeli.getText());
        now = old + inc;
        newstock = Integer.toString(now);
        String a = String.valueOf(newstock);
        String[] kolom = {"jumlah_obat"};
        String[] isi = {a};
        System.out.println(con.queryUpdate("tb_obat", kolom, isi, "kode_obat='" + KodeObat.getText() + "'"));
      
        }
    } catch (SQLException ex) {
        Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }
      public void getNota(){
                    
          try {

            File reprt = new File ("src/formkasir/Nota.jrmxl");      // letak penyimpanan report
            desain = JRXmlLoader.load(reprt);
            param.put("kode_transaksi",KodeTransaksi.getText());
            report = JasperCompileManager.compileReport(desain);
            print = JasperFillManager.fillReport(report, param, con.ConnectionDb());
            JasperViewer.viewReport(print, false);

    } catch (Exception ex) {

  JOptionPane.showMessageDialog(rootPane,"Dokumen Tidak Ada "+ex);
            
        }
      }
      public void getCetak(){
         
          if(Tunai.getText().equals("")){
           JOptionPane.showMessageDialog(null, "Masukan JUmlah Uang Yang Dibayarkan ");   
          } else {
              try{
                int a = Integer.parseInt(Kembalian.getText());
                int b = Integer.parseInt(Tunai.getText());
        
        if (a<0){
            JOptionPane.showMessageDialog(null, "Nominal Yang Harus Dibayarkan Kurang ");
        } else {
            getNota();
            getPaymen();
            kdtransaksi();
            getView();
            LSubTotal1.setText("");
            Tunai.setText("");
            Kembalian.setText("");  
              }
          } catch( Exception e) {
              JOptionPane.showMessageDialog(null, "Error");
          }
      
        
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
        jScrollPane1 = new javax.swing.JScrollPane();
        TableObat = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        ComboBoxSearch = new javax.swing.JComboBox();
        TextSearch = new javax.swing.JTextField();
        Search = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableTransaksi = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        LSubTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        AddCart = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        Delete1 = new javax.swing.JButton();
        Cetak = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        LSubTotal1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        Tunai = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        Kembalian = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        LKodeTransaksi = new javax.swing.JLabel();
        LKodeObat = new javax.swing.JLabel();
        LNamaObat = new javax.swing.JLabel();
        LMerekObat = new javax.swing.JLabel();
        LHargaJualObat = new javax.swing.JLabel();
        LJumlahBeli = new javax.swing.JLabel();
        JumlahBeli = new javax.swing.JTextField();
        HargaJualObat = new javax.swing.JTextField();
        MerekObat = new javax.swing.JTextField();
        NamaObat = new javax.swing.JTextField();
        KodeObat = new javax.swing.JTextField();
        KodeTransaksi = new javax.swing.JTextField();

        setMaximizable(true);

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));

        TableObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Obat", "Nama Obat", "Kategori Obat", "Jenis Obat", "Merek Obat", "Harga Jual Obat", "Stock Obat", "Expired"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TableObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableObatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableObat);

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Search Categories");

        ComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode Obat", "Nama Obat", "Kategori Obat", "Jenis Obat", "Merek Obat" }));

        Search.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Search.setText("Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 0, 153));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Keranjang Belanja");

        TableTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Transaksi", "Kode Obat", "Nama Obat", "Harga Obat", "Jumlah Beli", "Total Harga"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TableTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableTransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableTransaksi);

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Rp.");

        LSubTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText(",-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(LSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        AddCart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        AddCart.setText("AddCart");
        AddCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddCartActionPerformed(evt);
            }
        });

        Cancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(AddCart)
                        .addGap(20, 20, 20)
                        .addComponent(Cancel))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(AddCart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 153));

        Delete1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Delete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Trash Can_48px.png"))); // NOI18N
        Delete1.setText("Hapus");
        Delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Delete1ActionPerformed(evt);
            }
        });

        Cetak.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Cetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Print_48px.png"))); // NOI18N
        Cetak.setText("Cetak");
        Cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakActionPerformed(evt);
            }
        });

        LSubTotal1.setEditable(false);
        LSubTotal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                LSubTotal1KeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Rp.");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText(",-");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(LSubTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jLabel5)
                    .addGap(114, 114, 114)
                    .addComponent(jLabel6)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LSubTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6)
                        .addComponent(jLabel5))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Tunai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TunaiKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Rp.");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText(",-");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jLabel7)
                    .addGap(114, 114, 114)
                    .addComponent(jLabel8)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel8)
                        .addComponent(jLabel7))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Kembalian.setEditable(false);
        Kembalian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KembalianKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Rp.");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText(",-");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(Kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jLabel9)
                    .addGap(114, 114, 114)
                    .addComponent(jLabel11)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addComponent(jLabel9))
                    .addContainerGap(12, Short.MAX_VALUE)))
        );

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("TOTAL");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 202, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(72, 72, 72)
                    .addComponent(jLabel12)
                    .addContainerGap(72, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jLabel12)
                    .addContainerGap(12, Short.MAX_VALUE)))
        );

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setText("TUNAI");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(72, 72, 72)
                    .addComponent(jLabel13)
                    .addContainerGap(72, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jLabel13)
                    .addContainerGap(12, Short.MAX_VALUE)))
        );

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("KEMBALIAN");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jLabel14)
                    .addContainerGap(12, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(Delete1)
                        .addGap(54, 54, 54)
                        .addComponent(Cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(0, 204, 204));

        jLabel2.setBackground(new java.awt.Color(0, 0, 153));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TRANSAKSI");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(0, 102, 153));

        LKodeTransaksi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        LKodeTransaksi.setText("Kode Transaksi");

        LKodeObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeObat.setForeground(new java.awt.Color(255, 255, 255));
        LKodeObat.setText("Kode Obat");

        LNamaObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LNamaObat.setForeground(new java.awt.Color(255, 255, 255));
        LNamaObat.setText("Nama Obat");

        LMerekObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LMerekObat.setForeground(new java.awt.Color(255, 255, 255));
        LMerekObat.setText("Merek Obat");

        LHargaJualObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LHargaJualObat.setForeground(new java.awt.Color(255, 255, 255));
        LHargaJualObat.setText("Harga Jual");

        LJumlahBeli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LJumlahBeli.setForeground(new java.awt.Color(255, 255, 255));
        LJumlahBeli.setText("Jumlah Beli");

        JumlahBeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JumlahBeliKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(LNamaObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(LMerekObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(MerekObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(LHargaJualObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(HargaJualObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(LJumlahBeli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JumlahBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LKodeObat)
                            .addComponent(LKodeTransaksi))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(KodeTransaksi))))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LKodeTransaksi, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(KodeTransaksi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LKodeObat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LNamaObat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LMerekObat)
                    .addComponent(MerekObat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LHargaJualObat)
                    .addComponent(HargaJualObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JumlahBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LJumlahBeli))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(283, 283, 283)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TableObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableObatMouseClicked
        getMouseClick();
    }//GEN-LAST:event_TableObatMouseClicked

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
        getSearchMedicine();
    }//GEN-LAST:event_SearchActionPerformed

    private void TableTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableTransaksiMouseClicked
        // TODO add your handling code here:
        getMouseClick1();
    }//GEN-LAST:event_TableTransaksiMouseClicked

    private void AddCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCartActionPerformed
        // TODO add your handling code here:
        getAddCart();
        Cetak.setEnabled(true);
        Delete1.setEnabled(true);
    }//GEN-LAST:event_AddCartActionPerformed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        // TODO add your handling code here:
        getCancel();// TODO add your handling code here:
        getTable();
    }//GEN-LAST:event_CancelActionPerformed

    private void Delete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete1ActionPerformed
        // TODO add your handling code here:
        getDelete();
        kondisikd();
        

    }//GEN-LAST:event_Delete1ActionPerformed

    private void CetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakActionPerformed
        // TODO add your handling code here:
            getCetak();
            getPaymen();
            kdtransaksi();
            getView();
            LSubTotal1.setText("");
            Tunai.setText("");
            Kembalian.setText("");   
  
    }//GEN-LAST:event_CetakActionPerformed

    private void LSubTotal1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LSubTotal1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_LSubTotal1KeyReleased

    private void TunaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TunaiKeyReleased
        // TODO add your handling code here:
        getPaymen();
    }//GEN-LAST:event_TunaiKeyReleased

    private void KembalianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KembalianKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_KembalianKeyReleased

    private void JumlahBeliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JumlahBeliKeyReleased
        // TODO add your handling code here:
        getSubTotal();
    }//GEN-LAST:event_JumlahBeliKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddCart;
    private javax.swing.JButton Cancel;
    private javax.swing.JButton Cetak;
    private javax.swing.JComboBox ComboBoxSearch;
    private javax.swing.JButton Delete1;
    private javax.swing.JTextField HargaJualObat;
    private javax.swing.JTextField JumlahBeli;
    private javax.swing.JTextField Kembalian;
    private javax.swing.JTextField KodeObat;
    private javax.swing.JTextField KodeTransaksi;
    private javax.swing.JLabel LHargaJualObat;
    private javax.swing.JLabel LJumlahBeli;
    private javax.swing.JLabel LKodeObat;
    private javax.swing.JLabel LKodeTransaksi;
    private javax.swing.JLabel LMerekObat;
    private javax.swing.JLabel LNamaObat;
    private javax.swing.JLabel LSubTotal;
    private javax.swing.JTextField LSubTotal1;
    private javax.swing.JTextField MerekObat;
    private javax.swing.JTextField NamaObat;
    private javax.swing.JButton Search;
    private javax.swing.JTable TableObat;
    private javax.swing.JTable TableTransaksi;
    private javax.swing.JTextField TextSearch;
    private javax.swing.JTextField Tunai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
