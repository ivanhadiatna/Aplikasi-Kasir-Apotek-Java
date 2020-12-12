/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formadmin;
import db.Koneksi;
import db.Parameter;
import db.SetTable;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
/**
 *
 * @author HADIATNA
 */
public class StockObat extends javax.swing.JInternalFrame {
ResultSet st;
Koneksi con;

    /**
     * Creates new form StockObat
     */
    public StockObat() {
        con = new Koneksi(Parameter.HOST_DB, Parameter.USERNAME_DB,Parameter.PASSWORD_DB, Parameter.IPHOST, Parameter.PORT);
        initComponents();
        getTable();
        kodeObt();
        Edit.setEnabled(false);
        Delete.setEnabled(false);
        kodesupplier();
     
    }
    public void getTable() {
         String namaKolom[] = {"kode_obat","nama_obat","kategori_obat","jenis_obat","merek_obat","harga_beli","harga_jual","jumlah_obat","tanggal_masuk","expired","kode_supplier"};
        st = con.querySelect(namaKolom, "tb_obat");
        TabelObat.setModel(new SetTable(st));
    }
    
    private void kodeObt(){
      String kode=null;
      String s, s1;
      Integer j;
      Integer panjang = 3;
        try{
            String sql="select max(right(kode_obat, 3)) from tb_obat";
            st = con.eksekusiQuery(sql);
            if(st.first()==false){
                kode="OBAT001";
            }else{
                st.last();
                s=Integer.toString(st.getInt(1)+1);
                j=s.length();
                s1="";
                for(int i=0; i<=panjang-j; i++){
                    s1=s1+"0";
                    kode="OBAT"+s1+s;
                    KodeObat.setText(kode);
        }
        }
    }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }
    
    public void kodesupplier(){
        KodeSupplier.removeAllItems();
        KodeSupplier.addItem("Pilih Supplier");
        try {
           String  sql = "select * from tb_supplier" ;
           st = con.eksekusiQuery(sql);
            while (st.next()) {
                KodeSupplier.addItem(st.getString("kode_supplier"));
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }
     private void getAddMedicine() {
        //java.util.Date tgl=(java.util.Date)this.TanggalMasuk.getDate();
        java.util.Date ex=(java.util.Date)this.Expired.getDate();
        if (NamaObat.getText().equals("")||ComboBoxKategoriObat.getSelectedItem().equals("Kategori Obat") 
                ||ComboBoxJenisObat.getSelectedItem().equals("Jenis Obat")||MerekObat.getText().equals("")||HargaBeli.getText().equals("")
                ||HargaJual.getText().equals("")|| JumlahObat.getText().equals("") || KodeSupplier.getSelectedItem().equals("Pilih Supplier") ){
            //||new java.sql.Date(ex.getTime()).equals("")
            JOptionPane.showMessageDialog(this, "Lengkapi data obat");
        } else {
            String[] colom = {"kode_obat","nama_obat","kategori_obat","jenis_obat","merek_obat","harga_beli","harga_jual","jumlah_obat","expired","kode_supplier"};
            String[] value = {KodeObat.getText(),NamaObat.getText(),ComboBoxKategoriObat.getSelectedItem().toString(),ComboBoxJenisObat.getSelectedItem().toString(),
                MerekObat.getText(),HargaBeli.getText(),HargaJual.getText(),JumlahObat.getText(),new java.sql.Date(ex.getTime()).toString(),KodeSupplier.getSelectedItem().toString()};
            System.out.println(con.queryInsert("tb_obat", colom, value));
            kodeObt();
            getTable();            
            JOptionPane.showMessageDialog(this, "Data berhasil di simpan");
            getRefresh();
        }
}
    private void getEditMedicine(){
        java.util.Date ex=(java.util.Date)this.Expired.getDate();
        if (KodeObat.getText().equals("")||NamaObat.getText().equals("")||ComboBoxKategoriObat.getSelectedItem().equals("Kategori Obat") 
                || ComboBoxJenisObat.getSelectedItem().equals("Jenis Obat")||MerekObat.getText().equals("")||HargaBeli.getText().equals("")
                ||HargaJual.getText().equals("")|| JumlahObat.getText().equals("") || KodeSupplier.getSelectedItem().equals("Pilih Supplier") ){
            JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        } else {
            String[] column = {"nama_obat","kategori_obat","jenis_obat","merek_obat","harga_beli","harga_jual","jumlah_obat","expired","kode_supplier"};
            String[] value = {NamaObat.getText(),ComboBoxKategoriObat.getSelectedItem().toString(),ComboBoxJenisObat.getSelectedItem().toString(),
                MerekObat.getText(),HargaBeli.getText(), HargaJual.getText(),JumlahObat.getText(),new java.sql.Date(ex.getTime()).toString(), KodeSupplier.getSelectedItem().toString()};
            String id = String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 0));
            System.out.println(con.queryUpdate("tb_obat", column, value, "kode_obat='" + KodeObat.getText() + "'"));
            getTable();
            JOptionPane.showMessageDialog(this, "Data berhasil dirubah");
            getRefresh();
            kodeObt();
        }
    }
    public void getDeleteMedicine(){
        
        if (JOptionPane.showConfirmDialog(this, "Anda Yakin Ingin Menghapus Data Obat ???", "Warning!!!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            con.queryDelete("tb_obat", "kode_obat='" + KodeObat.getText() + "'");
        } else {
            return;
        }
        getTable();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
        getRefresh();
        kodeObt();
    }
    public void getSearchMedicine(){
        if (TextSearch.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Masukkan yang anda cari !!!");
        } else {
            if (ComboBoxSearch.getSelectedItem().equals("Kode Obat")) {
                st = con.querySelectAll("tb_obat", "kode_obat LIKE '%" + TextSearch.getText() + "%' ");
                TabelObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Nama Obat")) {
                st = con.querySelectAll("tb_obat", "nama_obat LIKE '%" + TextSearch.getText() + "%' ");
                TabelObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Kategori Obat")) {
                st = con.querySelectAll("tb_obat", "kategori_obat LIKE '%" + TextSearch.getText() + "%' ");
                TabelObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Jenis Obat")) {
                st = con.querySelectAll("tb_obat", "jenis_obat LIKE '%" + TextSearch.getText() + "%' ");
                TabelObat.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Merek Obat")) {
                st = con.querySelectAll("tb_obat", "merek_obat LIKE '%" + TextSearch.getText() + "%' ");
                TabelObat.setModel(new SetTable(st));
            } 
        }
    } 
    
     public void getMouseClick(){
        //java.util.Date ex=(java.util.Date)this.Expired.getDate();
        KodeObat.setText(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 0)));
        NamaObat.setText(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 1)));
        ComboBoxKategoriObat.setSelectedItem(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(),2)));
        ComboBoxJenisObat.setSelectedItem(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(),3)));
        MerekObat.setText(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 4)));
        HargaBeli.setText(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 5)));
        HargaJual.setText(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 6)));
        JumlahObat.setText(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 7)));
        String dateValue = String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 9));
        KodeSupplier.setSelectedItem(String.valueOf(TabelObat.getValueAt(TabelObat.getSelectedRow(), 10)));
        java.util.Date date=null;
        Edit.setEnabled(true);
        Delete.setEnabled(true);
        Add.setEnabled(false);
        try{
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
        }catch(ParseException ex){
            System.out.println(ex);
        }
        Expired.setDate(date);
    }
    public void getRefresh(){
        NamaObat.setText(null);
        ComboBoxKategoriObat.setSelectedItem("Kategori Obat");
        ComboBoxJenisObat.setSelectedItem("Jenis Obat");
        MerekObat.setText(null);
        HargaBeli.setText(null);
        HargaJual.setText(null);
        JumlahObat.setText(null);
        Expired.setDate(null);
        KodeSupplier.setSelectedItem("Pilih Supplier");
        Add.setEnabled(true);
        kodeObt();
        Edit.setEnabled(false);
        Delete.setEnabled(false);
        TextSearch.setText(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelObat = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        ComboBoxSearch = new javax.swing.JComboBox();
        TextSearch = new javax.swing.JTextField();
        Search = new javax.swing.JButton();
        Add = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Refresh = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        LKodeObat1 = new javax.swing.JLabel();
        LNamaObat1 = new javax.swing.JLabel();
        LMerekObat1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ComboBoxJenisObat = new javax.swing.JComboBox<>();
        ComboBoxKategoriObat = new javax.swing.JComboBox<>();
        MerekObat = new javax.swing.JTextField();
        NamaObat = new javax.swing.JTextField();
        KodeObat = new javax.swing.JTextField();
        LHargaJualObat2 = new javax.swing.JLabel();
        LHargaJualObat3 = new javax.swing.JLabel();
        LJumlahBeli1 = new javax.swing.JLabel();
        LExpired2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        KodeSupplier = new javax.swing.JComboBox<>();
        Expired = new com.toedter.calendar.JDateChooser();
        JumlahObat = new javax.swing.JTextField();
        HargaJual = new javax.swing.JTextField();
        HargaBeli = new javax.swing.JTextField();

        setMaximizable(true);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jPanel8.setBackground(new java.awt.Color(0, 204, 204));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DATA STOCK OBAT");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        TabelObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TabelObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelObatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelObat);

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Search Categories");

        ComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode Obat", "Nama Obat", "Kategori Obat", "Jenis Obat", "Merek Obat" }));

        Search.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Search.setText("Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextSearch)
                    .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Add.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Plus_48px.png"))); // NOI18N
        Add.setText("ADD");
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        Edit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/007-edit.png"))); // NOI18N
        Edit.setText("EDIT");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Delete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Trash_48px.png"))); // NOI18N
        Delete.setText("DELETE");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        Refresh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/002-refresh.png"))); // NOI18N
        Refresh.setText("REFRESH");
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(0, 204, 204));

        LKodeObat1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeObat1.setText("Kode Obat");

        LNamaObat1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LNamaObat1.setText("Nama Obat");

        LMerekObat1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LMerekObat1.setText("Merek Obat");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Kategori Obat");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Jenis Obat");

        ComboBoxJenisObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jenis Obat", "Kablet", "Pil", "Kapsul", "Botol", "Sirup", "Salep" }));

        ComboBoxKategoriObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kategori Obat", "Obat Luar", "Obat Dalam" }));

        KodeObat.setEditable(false);

        LHargaJualObat2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LHargaJualObat2.setText("Harga Beli");

        LHargaJualObat3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LHargaJualObat3.setText("Harga Jual");

        LJumlahBeli1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LJumlahBeli1.setText("Jumlah Obat");

        LExpired2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LExpired2.setText("Expired");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Kode Supplier");

        KodeSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        Expired.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ExpiredPropertyChange(evt);
            }
        });

        HargaJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HargaJualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LKodeObat1)
                                .addComponent(LMerekObat1)
                                .addComponent(LNamaObat1))
                            .addGap(33, 33, 33))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(18, 18, 18)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(40, 40, 40)))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ComboBoxJenisObat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ComboBoxKategoriObat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MerekObat)
                    .addComponent(NamaObat)
                    .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(LJumlahBeli1)
                            .addComponent(LExpired2))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Expired, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JumlahObat)
                            .addComponent(KodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LHargaJualObat2)
                            .addComponent(LHargaJualObat3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(HargaJual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HargaBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LHargaJualObat2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(HargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LHargaJualObat3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(JumlahObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LJumlahBeli1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Expired, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(LExpired2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(KodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(NamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LNamaObat1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(MerekObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LMerekObat1)))
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LKodeObat1)
                                .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel5))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ComboBoxKategoriObat, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(ComboBoxJenisObat, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(Add, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Edit, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Delete, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Refresh, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Edit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Delete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Refresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TabelObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelObatMouseClicked
        // TODO add your handling code here:
        getMouseClick();
    }//GEN-LAST:event_TabelObatMouseClicked

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
        getSearchMedicine();
    }//GEN-LAST:event_SearchActionPerformed

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        // TODO add your handling code here:
        getAddMedicine();
    }//GEN-LAST:event_AddActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        // TODO add your handling code here:
        getEditMedicine();
    }//GEN-LAST:event_EditActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        // TODO add your handling code here:
        getDeleteMedicine();
    }//GEN-LAST:event_DeleteActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        // TODO add your handling code here:
        getRefresh();// TODO add your handling code here:
        getTable();
    }//GEN-LAST:event_RefreshActionPerformed

    private void ExpiredPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ExpiredPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_ExpiredPropertyChange

    private void HargaJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HargaJualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HargaJualActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JComboBox<String> ComboBoxJenisObat;
    private javax.swing.JComboBox<String> ComboBoxKategoriObat;
    private javax.swing.JComboBox ComboBoxSearch;
    private javax.swing.JButton Delete;
    private javax.swing.JButton Edit;
    private com.toedter.calendar.JDateChooser Expired;
    private javax.swing.JTextField HargaBeli;
    private javax.swing.JTextField HargaJual;
    private javax.swing.JTextField JumlahObat;
    private javax.swing.JTextField KodeObat;
    private javax.swing.JComboBox<String> KodeSupplier;
    private javax.swing.JLabel LExpired2;
    private javax.swing.JLabel LHargaJualObat2;
    private javax.swing.JLabel LHargaJualObat3;
    private javax.swing.JLabel LJumlahBeli1;
    private javax.swing.JLabel LKodeObat1;
    private javax.swing.JLabel LMerekObat1;
    private javax.swing.JLabel LNamaObat1;
    private javax.swing.JTextField MerekObat;
    private javax.swing.JTextField NamaObat;
    private javax.swing.JButton Refresh;
    private javax.swing.JButton Search;
    private javax.swing.JTable TabelObat;
    private javax.swing.JTextField TextSearch;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
