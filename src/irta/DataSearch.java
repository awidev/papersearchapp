/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irta;

import dataprocess.Detail;
import dataprocess.Extract;
import dataprocess.ListPosting;
import dataprocess.RemoveTandaBaca;
import dataprocess.Stemming;
import dataprocess.Stopword;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author Hafiz & Aditya Gunawan
 */
public class DataSearch extends javax.swing.JPanel {

    /**
     * Creates new form DataSearch
     */
    
    /*
    List Proses :
    1. convert PDF into text/string
    2. every text/string tokenization
    3. stoplist filtering
    4. stemming words
    5. searching method
    6. calculate method
    7. information retrieval
    */
    
    Extract ExtractData = new Extract();
    ListPosting ListPosting = new ListPosting();
    Detail DetailInfo;
    StringTokenizer stringToken;

    private Stopword Stopword;
    private RemoveTandaBaca RemoveTandaBaca = new RemoveTandaBaca();
    private Stemming Stemming;

    private String Folder;
    private File FolderPath = null;
    private File ListFilex[];
    private DefaultListModel Model;
    private int FileTotal = 0;
    private int dochasil = 0;

    Parser parser = new AutoDetectParser();
    InputStream inStream = null;
    BodyContentHandler handler = null;

    //constructor
    public DataSearch(String namafolder, File pathfolder, File[] ListFile, DefaultListModel modelx, int banyakfile) throws IOException {
        initComponents();
        Stopword = new Stopword();
        Folder = namafolder;
        FolderPath = pathfolder;
        ListFilex = ListFile;
        Model = modelx;
        FileTotal = banyakfile;
        System.out.println(FolderPath);
        System.out.println(FileTotal);
        JOptionPane.showMessageDialog(null, "Program Start!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public double Savoy(){
        double nidf;
        nidf = Math.log(FileTotal / dochasil);
        return nidf;
    
    }
    
    /*memilih metode pencarian yang akan digunakan*/
    public void SearchMetode() {
        if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih Ulang!!!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        //single search
        else if (jComboBox1.getSelectedIndex() == 1) {
            jTextField1.setEnabled(true);
            jTextField2.setEnabled(false);
            jTextField3.setEnabled(false);
            jComboBox2.setEnabled(false);
            jLabel1.setEnabled(false);
            
        } 
        //boolean search
        else if (jComboBox1.getSelectedIndex() == 2) {
            jTextField1.setEnabled(true);
            jTextField2.setEnabled(true);
            jTextField3.setEnabled(false);
            jComboBox2.setEnabled(true);
            jLabel1.setEnabled(false);
            BooleanSearch();
            
        //boolean and savoy    
        } else if (jComboBox1.getSelectedIndex() == 3) {
            jTextField1.setEnabled(true);
            jTextField2.setEnabled(true);
            jTextField3.setEnabled(true);
            jComboBox2.setEnabled(true);
        }
    }
    
    public void CheckData(){
        Extract();          //text-extractor from PDF into string
        Tokenisasi();       //tokenization
        Stoplist();         //stoplist filtering
        Stemming();
        Posisition();
        TableView();
    }

    public void BooleanSearch() {
        dochasil = 0;
        if (!"".equals(jTextField1.getText()) && !"".equals(jTextField2.getText())) {
            String key1 = jTextField1.getText();
            String key2 = jTextField2.getText();
            key1 = key1.toLowerCase();
            key2 = key2.toLowerCase();

            for (int i = 0; i < Stopword.size(); i++) {
                if (key1.equals(Stopword.getStopword(i))) {
                    key1 = null;
                }

                if (key2.equals(Stopword.getStopword(i))) {
                    key2 = null;
                }
            }

            int indexKey1 = 0;
            int indexKey2 = 0;

            if (key1 != null) {
                indexKey1 = cekIndex(key1);
            }
            if (key2 != null) {
                indexKey2 = cekIndex(key2);
            }

//            areaHasil.setText(indexKey1 +" " +indexKey2);
            String hasil = "";
            for (int i = 0; i < ExtractData.getSize(); i++) {
                int a = DetailInfo.getListDetail(indexKey1, i);
                int b = DetailInfo.getListDetail(indexKey2, i);
                if (jComboBox2.getSelectedIndex() == 1) {
                    if ((a & b) == 1) {                        hasil = hasil.concat(

                                "Nama File\t: " + ExtractData.getDocTitle(i) + "\n"
                                //+ "URL File\t: " + lblLokasi.getText() + "/" + ExtractData.getDocTitle(i) + "\n\n"
                        );
                    dochasil++;
                    }
                }

                if (jComboBox2.getSelectedIndex() == 2) {
                    if ((a | b) == 1) {
                        hasil = hasil.concat(
                                "Nama File\t: " + ExtractData.getDocTitle(i) + "\n"
                                //+ "URL File\t: " + lblLokasi.getText() + "/" + ExtractData.getDocTitle(i) + "\n\n"
                        );
                        dochasil++;
                    }
                }
            }

            if (hasil == "") {
                jTextArea2.setText("Not Match !!!");
            } else {
                jTextArea2.setText(hasil);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Keyword ....", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*Extract document on local memori*/
    public void Extract() {
        JOptionPane.showMessageDialog(null, "Extraction!", "Message", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(FileTotal);
        for (int i = 0; i < FileTotal; i++) {
            if (ListFilex[i].isFile()) {
                String PathFile = FolderPath + "\\" + ListFilex[i].getName();
                try {
                    inStream = new FileInputStream(PathFile);
                    handler = new BodyContentHandler();
                    parser.parse(inStream, handler, new Metadata(), new ParseContext());
                    ExtractData.setDatadoc(ListFilex[i].getName().toString(), handler.toString());
                    
                } catch (IOException | SAXException | TikaException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error!", "Message", JOptionPane.INFORMATION_MESSAGE);
                } finally {
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error!", "Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
            System.out.println("Doc " + (i + 1));
        }
    }

    private void Tokenisasi() {

        int index = 0;
        for (int i = 0; i < ExtractData.getSize(); i++) {
            String Line = ExtractData.getDocContent(i);
            Line = RemoveTandaBaca.replace(Line);
            int count = 0, loop = 0;
            stringToken = new StringTokenizer(Line);
            ListPosting.setTemp(String.valueOf(i));
            while (stringToken.hasMoreTokens()) {
                count = 1;
                String kt = stringToken.nextToken();
                kt = kt.toLowerCase();

                if (ListPosting.isEmpty()) {
                    ListPosting.addTerm(kt);
                    ListPosting.addFreq(loop, 1);
                } else {
                    for (int j = 0; j < ListPosting.Size(); j++) {
                        if (kt.equalsIgnoreCase(ListPosting.getTerm(j))) {
                            count = ListPosting.getFreq(j);
                            count++;
                            ListPosting.setFreq(j, count);
                        }
                    }
                    if (count == 1) {
                        ListPosting.addTerm(kt);
                        loop++;
                        ListPosting.addFreq(1);
                    }
                }
            }

        }
        JOptionPane.showMessageDialog(null, "Tokenization Done!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Stoplist() {
        for (int i = 0; i < Stopword.size(); i++) {
            for (int j = 0; j < ListPosting.Size(); j++) {
                if (Stopword.getStopword(i).equals(ListPosting.getTerm(j))) {
                    ListPosting.remove(j);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Stoplisting Done!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Stemming() {
        Stemming = new Stemming();
        for (int i = 0; i < ListPosting.Size(); i++) {
            ListPosting.addStem(Stemming.proses(ListPosting.getTerm(i)));
        }
        JOptionPane.showMessageDialog(null, "Stemming Done!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Posisition() {
        DetailInfo = new Detail(ListPosting.Size(), ExtractData.getSize());
        for (int i = 0; i < ExtractData.getSize(); i++) {
            String Line = ExtractData.getDocContent(i);
            Line = RemoveTandaBaca.replace(Line);
            stringToken = new StringTokenizer(Line);
            while (stringToken.hasMoreTokens()) {
                String kt = stringToken.nextToken();
                kt = kt.toLowerCase();
                for (int j = 0; j < ListPosting.Size(); j++) {
                    if (kt.equals(ListPosting.getTerm(j))) {
                        DetailInfo.setListDetail(j, i, 1);
                    } else if (DetailInfo.getListDetail(j, i) != 1) {
                        DetailInfo.setListDetail(j, i, 0);
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "... Done!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }


    public void setTable(JTable table, int row) {
        DefaultTableModel tablemodel = new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null}
                },
                new String[]{
                    "NO", "KATA", "JUMLAH KATA"//, "Info"
                }
        );
        table.setModel(tablemodel);
        String[] data = {null};

        for (int i = 0; i < row; i++) {
            ((DefaultTableModel) table.getModel()).addRow(data);
        }
    }

    private void TableView() {
        setTable(jTable1, ListPosting.Size() - 1);
        for (int i = 0; i < ListPosting.Size(); i++) {
            jTable1.setValueAt(i + 1, i, 0);
            jTable1.setValueAt(ListPosting.getTerm(i), i, 1);
            //jTable1.setValueAt(ListPosting.getStem(i), i, 2);
            jTable1.setValueAt(ListPosting.getFreq(i), i, 2);
            String sumber = "";
            for (int j = 0; j < ExtractData.getSize(); j++) {
                sumber = sumber + DetailInfo.getListDetail(i, j);
            }
        }
        JOptionPane.showMessageDialog(null, "Insert Table Done!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private int cekIndex(String value) {
        int index = 0;
        for (int i = 0; i < ListPosting.Size(); i++) {
            if (value.equals(ListPosting.getTerm(i))) {
                index = i;
            }
        }

        return index;
    }

    /*menload lokasi path dari lokal file*/
    public void getPathPropertise() {
        File configFile = new File("path.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String path = props.getProperty("Path");

            System.out.println("Path: " + path);

            reader.close();

        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
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

        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Search by", "Single Search", "Boolean Search", "Boolean and Savoy" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField1.setText("word1");
        jTextField1.setToolTipText("");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Operator", "AND", "OR" }));

        jTextField2.setText("word2");

        jLabel1.setText("Nilai P");

        jButton1.setText("Check Data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Cancel");

        jLabel2.setText("Hasil Search");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CheckData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTextArea2.setText("");
        SearchMetode();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
