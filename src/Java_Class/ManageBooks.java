/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Java_Class;

import java.awt.HeadlessException;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import project.ConnectionProvider;

/**
 *
 * @author NIKIT
 */
public final class ManageBooks extends javax.swing.JFrame {

    /**
     * Creates new form ManageBooks
     */
    String book_name, book_id, course, publication, author, book_shelf_no, book_shelfno, description, quantity;
//    int book_id;
    DefaultTableModel model;
    String newImagePath = "";

    public ManageBooks() {
        initComponents();
        FetchBookDetailsToTable("");
        randomId();

    }

    public void randomId() {

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select count(book_id) from book_details");
            if (rs.first()) {
                int min = 1000000;
                int max = 2050000;

                //String b1 = getAlphaNumericString(2);

                //Generate random int value 
                int b2 = (int) (Math.random() * (max - min + 1) + min);
                //System.out.println(b);

               int b = b2 ;

                //int b = rs.getInt(1);
                //id = id + 1;
                String str = String.valueOf(b);

                txt_book_id.setText(str);
                txt_book_id.setEditable(false);
            } else {
                txt_book_id.setText("1");
            }
        } catch (SQLException e) {
            System.out.println(e);
            JFrame jf = new JFrame();
            jf.setAlwaysOnTop(true);
            JOptionPane.showMessageDialog(jf, e);
        }
    }

    public String getAlphaNumericString(int n) {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    //store database result into ArrayList Method
    public ArrayList<Book_ArrayList> getList(String searchTerm) {
        ArrayList<Book_ArrayList> getList = new ArrayList<>();

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT book_id,book_name,course,publication,author,book_shelf_no,quantity,image_path,image from book_details WHERE concat(book_id,book_name,course,publication,author,book_shelf_no,quantity,image_path,image) LIKE '%" + searchTerm + "%'");

            while (rs.next()) {
                Book_ArrayList bd = new Book_ArrayList();
                bd.setBook_id(rs.getString("book_id"));
                bd.setBook_name(rs.getString("book_name"));
                bd.setCourse(rs.getString("course"));
                bd.setPublication(rs.getString("publication"));
                bd.setAuthor(rs.getString("author"));
                bd.setBook_shelf_no(rs.getString("book_shelf_no"));
                bd.setQuantity(rs.getString("quantity"));
                bd.setImage_path(rs.getString("image_path"));
                bd.setImage(rs.getBytes("image"));
                //bd.setDescription(rs.getString("description"));

                getList.add(bd);

            }

        } catch (SQLException e) {

        }
        return getList;

    }

    //Populate Jtable with data from database
    public void FetchBookDetailsToTable(String searchTermfetch) {

        ArrayList<Book_ArrayList> datArrayList = getList(searchTermfetch);
        model = (DefaultTableModel) tbl_book_details.getModel();
        model.setRowCount(0);

        Object[] rows = new Object[9];
        //loop through the arraylist to populate jtable

        for (int i = 0; i < datArrayList.size(); i++) {
            rows[0] = datArrayList.get(i).getBook_id();
            rows[1] = datArrayList.get(i).getBook_name();
            rows[2] = datArrayList.get(i).getCourse();
            rows[3] = datArrayList.get(i).getPublication();
            rows[4] = datArrayList.get(i).getAuthor();
            rows[5] = datArrayList.get(i).getBook_shelf_no();
            rows[6] = datArrayList.get(i).getQuantity();
            rows[7] = datArrayList.get(i).getImage_path();
            rows[8] = datArrayList.get(i).getImage();
            model.addRow(rows);
        }

    }

    //method to add books
    public boolean AddBook() throws FileNotFoundException {
        boolean isAdded = false;
        book_id = txt_book_id.getText();
        book_name = txt_book_name.getText();
        course = jComboBoxCourseName.getSelectedItem().toString();
        publication = txt_publication.getText();
        author = txt_authorName.getText();
        book_shelf_no = jComboBoxShelfNo.getSelectedItem().toString();
        quantity = txt_quantity.getText();
        newImagePath = txt_imagePath.getText();
        File image = new File(newImagePath);
        Date date = new Date();
        Long l1 = date.getTime();
        java.sql.Date today_date = new java.sql.Date(l1);

        try {
            InputStream is = new FileInputStream(image);
            Connection con = ConnectionProvider.getCon();
            String sql = "Insert into book_details(book_id,book_name,course,publication,author,book_shelf_no,quantity,image_path,image,book_added_date) values (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book_id);
            ps.setString(2, book_name);
            ps.setString(3, course);
            ps.setString(4, publication);
            ps.setString(5, author);
            ps.setString(6, book_shelf_no);
            ps.setString(7, quantity);
            ps.setString(8, newImagePath);
            ps.setBlob(9, is);
            ps.setDate(10, today_date);
            //ps.setString(9, description);

            int updatedRowCount = ps.executeUpdate();

            isAdded = updatedRowCount > 0;

        } catch (HeadlessException | SQLException e) {
            e.getMessage();

        }

        return isAdded;

    }

    public boolean validateAddBook() {

        book_name = txt_book_name.getText();
        publication = txt_publication.getText();
        author = txt_authorName.getText();
        quantity = txt_quantity.getText();
//      newImagePath = txt_imagePath.getText();
//      File image = new File(newImagePath);

        if (book_name.equals("") || !book_name.matches("^[a-zA-Z ]{0,100}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Book Name !");
            return false;
        }

        if (publication.equals("") || !publication.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Publication !");
            return false;
        }
        if (author.equals("") || !author.matches("^[a-zA-Z0-9. ]{0,100}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Author !");
            return false;
        }
        if (quantity.equals("") || !quantity.matches("^[0-9]{0,15}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Quantity !");
            return false;
        } else if (!(quantity.length() < 50)) {
            JOptionPane.showMessageDialog(this, "Please enter valid Quantity !");
            return false;
        }

        return true;

    }

    //Method to Update Book Details
    public boolean UpdateBookDetails() throws FileNotFoundException {
        boolean isUpdated = false;
        book_id = txt_book_id.getText();
        book_name = txt_book_name.getText();
        course = jComboBoxCourseName.getSelectedItem().toString();
        publication = txt_publication.getText();
        author = txt_authorName.getText();
        book_shelf_no = jComboBoxShelfNo.getSelectedItem().toString();
        quantity = txt_quantity.getText();
        newImagePath = txt_imagePath.getText();
        File image = new File(newImagePath);
        int JTableSelectedRow = tbl_book_details.getSelectedRow();

        if (JTableSelectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No Book Selected !");

        } else {
            try {
                InputStream is = new FileInputStream(image);
                Connection con = ConnectionProvider.getCon();
                String sql = "Update book_details set book_name=?,course=?,publication=?,author=?,book_shelf_no=?,quantity=?,image_path=?,image=? where book_Id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, book_name);
                ps.setString(2, course);
                ps.setString(3, publication);
                ps.setString(4, author);
                ps.setString(5, book_shelf_no);
                ps.setString(6, quantity);
                ps.setString(7, newImagePath);
                ps.setBlob(8, is);
                ps.setString(9, book_id);

                int updatedRowCount = ps.executeUpdate();

                isUpdated = updatedRowCount > 0;

            } catch (HeadlessException | SQLException e) {
                e.getMessage();
            }

        }

        return isUpdated;

    }

    //Method to Delete Book Details
    public boolean DeleteBookDetails() {
        boolean isDeleted = false;
        int JTableSelectedRow = tbl_book_details.getSelectedRow();
        book_id = txt_book_id.getText();
        if (JTableSelectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No Book Selected !");

        } else {

            try {
                Connection con = ConnectionProvider.getCon();
                String sql = "Delete from book_details where book_id =?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, book_id);

                int updatedRowCount = ps.executeUpdate();

                if (isDeleted = updatedRowCount > 0) {
                    JOptionPane.showMessageDialog(this, "Book Details Deleted !");
                    ClearTable();
                    FetchBookDetailsToTable("");

                } else {
                    JOptionPane.showMessageDialog(this, "Failed To Delete Book Details !");
                }

            } catch (HeadlessException | SQLException e) {
                e.getMessage();
            }
        }

        return isDeleted;

    }

    //Method to clear table
    public void ClearTable() {
        DefaultTableModel m = (DefaultTableModel) tbl_book_details.getModel();
        m.setRowCount(0);

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
        jLabel1 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_book_id = new app.bolivia.swing.JCTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_book_name = new app.bolivia.swing.JCTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txt_quantity = new app.bolivia.swing.JCTextField();
        btn_add = new necesario.RSMaterialButtonCircle();
        btn_update = new necesario.RSMaterialButtonCircle();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lbl_image = new javax.swing.JLabel();
        btn_image_browse = new rojeru_san.complementos.RSButtonHover();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txt_publication = new app.bolivia.swing.JCTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jComboBoxCourseName = new javax.swing.JComboBox<>();
        jComboBoxShelfNo = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        txt_authorName = new app.bolivia.swing.JCTextField();
        jLabel23 = new javax.swing.JLabel();
        btn_clear = new necesario.RSMaterialButtonCircle();
        btn_delete = new necesario.RSMaterialButtonCircle();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_book_details = new rojeru_san.complementos.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txt_imagePath = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_filtertable = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 204));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back.png"))); // NOI18N
        jLabel1.setText(" Back");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 30));

        jLabel19.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bookid.png"))); // NOI18N
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 50, 50));

        jLabel18.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Book ID :");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 200, 60));

        txt_book_id.setBackground(new java.awt.Color(0, 102, 102));
        txt_book_id.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_book_id.setForeground(new java.awt.Color(255, 255, 255));
        txt_book_id.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_book_id.setPhColor(new java.awt.Color(255, 255, 255));
        txt_book_id.setPlaceholder("Book Id");
        jPanel1.add(txt_book_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 290, -1));

        jLabel20.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/book.png"))); // NOI18N
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 50, 50));

        jLabel21.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Book Name :");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 220, 60));

        txt_book_name.setBackground(new java.awt.Color(0, 102, 102));
        txt_book_name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_book_name.setForeground(new java.awt.Color(255, 255, 255));
        txt_book_name.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_book_name.setPhColor(new java.awt.Color(255, 255, 255));
        txt_book_name.setPlaceholder("Enter Book Name");
        jPanel1.add(txt_book_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 290, -1));

        jLabel24.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/quantity_1.png"))); // NOI18N
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 730, 50, 50));

        jLabel25.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Quantity :");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 680, 160, 60));

        txt_quantity.setBackground(new java.awt.Color(0, 102, 102));
        txt_quantity.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_quantity.setForeground(new java.awt.Color(255, 255, 255));
        txt_quantity.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_quantity.setPhColor(new java.awt.Color(255, 255, 255));
        txt_quantity.setPlaceholder("Enter Quantity");
        jPanel1.add(txt_quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 740, 290, -1));

        btn_add.setBackground(new java.awt.Color(255, 0, 51));
        btn_add.setBorder(null);
        btn_add.setText("ADD");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        jPanel1.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1020, 90, 50));

        btn_update.setBackground(new java.awt.Color(255, 0, 51));
        btn_update.setText("update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel1.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 1020, 90, 50));

        jLabel26.setFont(new java.awt.Font("Sitka Text", 1, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Image :");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 810, 100, 50));

        jLabel27.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/camera.png"))); // NOI18N
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 880, 50, 50));

        lbl_image.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        jPanel1.add(lbl_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 790, 160, 200));

        btn_image_browse.setBackground(new java.awt.Color(0, 102, 102));
        btn_image_browse.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btn_image_browse.setText("Browse");
        btn_image_browse.setColorHover(new java.awt.Color(0, 153, 0));
        btn_image_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_image_browseActionPerformed(evt);
            }
        });
        jPanel1.add(btn_image_browse, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 880, 110, 40));

        jLabel28.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Course Name :");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 200, 60));

        jLabel29.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/faculty.png"))); // NOI18N
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 50, 50));

        jLabel30.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Publication :");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, 200, 60));

        txt_publication.setBackground(new java.awt.Color(0, 102, 102));
        txt_publication.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_publication.setForeground(new java.awt.Color(255, 255, 255));
        txt_publication.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_publication.setPhColor(new java.awt.Color(255, 255, 255));
        txt_publication.setPlaceholder("Enter Publication Name");
        jPanel1.add(txt_publication, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 420, 290, -1));

        jLabel31.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/publication.png"))); // NOI18N
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 50, 50));

        jLabel32.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Book Shelf No :");
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 200, 60));

        jLabel33.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bookshelf_1.png"))); // NOI18N
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 630, 50, 50));

        jComboBoxCourseName.setBackground(new java.awt.Color(0, 102, 102));
        jComboBoxCourseName.setFont(new java.awt.Font("Sitka Text", 0, 20)); // NOI18N
        jComboBoxCourseName.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxCourseName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BCA", "BBM", "BHM", "BASW", "NOVEL", "RESEARCH BOOK", "BIOGRAPHY", " " }));
        jComboBoxCourseName.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel1.add(jComboBoxCourseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 290, -1));

        jComboBoxShelfNo.setBackground(new java.awt.Color(0, 102, 102));
        jComboBoxShelfNo.setFont(new java.awt.Font("Sitka Text", 0, 20)); // NOI18N
        jComboBoxShelfNo.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxShelfNo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Shelf-1", "Shelf-2", "Shelf-3", "Shelf-4", "Shelf-5", "Shelf-6", "Shelf-7", "Shelf-8", "Shelf-9", "Shelf-10", "Shelf-11", "Shelf-12" }));
        jComboBoxShelfNo.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel1.add(jComboBoxShelfNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 640, 290, -1));

        jLabel22.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Author Name :");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 220, 60));

        txt_authorName.setBackground(new java.awt.Color(0, 102, 102));
        txt_authorName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_authorName.setForeground(new java.awt.Color(255, 255, 255));
        txt_authorName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_authorName.setPhColor(new java.awt.Color(255, 255, 255));
        txt_authorName.setPlaceholder("Enter Author Name");
        jPanel1.add(txt_authorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, 290, -1));

        jLabel23.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/author.png"))); // NOI18N
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, 50, 50));

        btn_clear.setBackground(new java.awt.Color(255, 0, 51));
        btn_clear.setText("CLEAR");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });
        jPanel1.add(btn_clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1020, 90, 50));

        btn_delete.setBackground(new java.awt.Color(255, 0, 51));
        btn_delete.setText("DELETE");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel1.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 1020, 90, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 1090));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 0, 40, 50));

        tbl_book_details.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Book Id", "Book Name", "Course Name", "Publication", "Author Name", "Book Shelf No", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_book_details.setColorBackgoundHead(new java.awt.Color(0, 102, 102));
        tbl_book_details.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_book_details.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tbl_book_details.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tbl_book_details.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tbl_book_details.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tbl_book_details.setFuenteFilas(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tbl_book_details.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tbl_book_details.setFuenteHead(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        tbl_book_details.setRowHeight(34);
        tbl_book_details.setRowMargin(3);
        tbl_book_details.setSelectionBackground(new java.awt.Color(102, 102, 255));
        tbl_book_details.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_book_detailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_book_details);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 1440, 550));

        jLabel3.setFont(new java.awt.Font("Sitka Text", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Books_52px_1.png"))); // NOI18N
        jLabel3.setText(" Manage Books");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 360, -1));

        jPanel3.setBackground(new java.awt.Color(0, 102, 51));
        jPanel3.setPreferredSize(new java.awt.Dimension(450, 4));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 160, -1, -1));

        txt_imagePath.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        txt_imagePath.setForeground(new java.awt.Color(255, 255, 255));
        txt_imagePath.setText("jTextField1");
        txt_imagePath.setBorder(null);
        jPanel2.add(txt_imagePath, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 990, 110, 30));

        jLabel4.setFont(new java.awt.Font("Sitka Text", 1, 26)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/searchBook.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 70, 60));

        txt_filtertable.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        txt_filtertable.setForeground(new java.awt.Color(255, 0, 0));
        txt_filtertable.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 102, 0)));
        txt_filtertable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_filtertableKeyReleased(evt);
            }
        });
        jPanel2.add(txt_filtertable, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, 150, -1));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/minimize.png"))); // NOI18N
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(1420, 0, 40, 50));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, 1520, 1080));

        setSize(new java.awt.Dimension(1920, 1080));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        if (validateAddBook()) {

            try {
                if (AddBook() == true) {
                    JOptionPane.showMessageDialog(this, "Book Added !");
                    ClearTable();
                    FetchBookDetailsToTable(" ");

                } else {

                    JOptionPane.showMessageDialog(this, "Failed To Add Book !");

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManageBooks.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        try {
            // TODO add your handling code here:
            if (UpdateBookDetails() == true) {
                JOptionPane.showMessageDialog(this, "Book Updated !");
                ClearTable();
                FetchBookDetailsToTable("");

            } else {

//                JOptionPane.showMessageDialog(this, "Failed To Update Book !");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManageBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Exit ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
            dispose();
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void btn_image_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_image_browseActionPerformed
        // TODO add your handling code here:JFileChooser browseImageFile = new JFileChooser("C:\\Users\\NIKIT\\Desktop\\pictures");

        String currentDirectoryPath = "C:\\Users\\Nikit\\OneDrive\\Desktop\\Project II\\pictures";
        JFileChooser imageFileChooser = new JFileChooser(currentDirectoryPath);
        int imageChooser = imageFileChooser.showOpenDialog(null);
        imageFileChooser.setDialogTitle("Choose Image");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
        imageFileChooser.setFileFilter(fnef);

        if (imageChooser == JFileChooser.APPROVE_OPTION) {
            File imageFile = imageFileChooser.getSelectedFile();
            String imageFilePath = imageFile.getAbsolutePath();
            txt_imagePath.setText(imageFilePath);

            ImageIcon i = new ImageIcon(imageFilePath);
            Image image = i.getImage().getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon resizedImageIcon = new ImageIcon(image);
            lbl_image.setIcon(resizedImageIcon);
        }

    }//GEN-LAST:event_btn_image_browseActionPerformed

    private void tbl_book_detailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_book_detailsMouseClicked
        // TODO add your handling code here:
        int JTableSelectedRow = tbl_book_details.getSelectedRow();

        txt_book_id.setText(getList("").get(JTableSelectedRow).getBook_id());
        txt_book_name.setText(getList("").get(JTableSelectedRow).getBook_name());
        jComboBoxCourseName.setSelectedItem(getList("").get(JTableSelectedRow).getCourse());
        txt_publication.setText(getList("").get(JTableSelectedRow).getPublication());
        txt_authorName.setText(getList("").get(JTableSelectedRow).getAuthor());
        jComboBoxShelfNo.setSelectedItem(getList("").get(JTableSelectedRow).getBook_shelf_no());
        txt_quantity.setText((getList("").get(JTableSelectedRow).getQuantity()));
        txt_imagePath.setText(getList("").get(JTableSelectedRow).getImage_path());
        lbl_image.setIcon(new ImageIcon(new ImageIcon(getList("").get(JTableSelectedRow).getImage()).getImage().getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH)));
    }//GEN-LAST:event_tbl_book_detailsMouseClicked

    private void txt_filtertableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filtertableKeyReleased
        FetchBookDetailsToTable(txt_filtertable.getText());
    }//GEN-LAST:event_txt_filtertableKeyReleased

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        // TODO add your handling code here:
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // TODO add your handling code here:
        randomId();
        txt_book_name.setText(null);
        jComboBoxCourseName.setSelectedItem(null);
        txt_quantity.setText(null);
        txt_publication.setText(null);
        txt_authorName.setText(null);
        txt_imagePath.setText(null);
        lbl_image.setIcon(null);
        jComboBoxShelfNo.setSelectedItem(null);

        //txt_bookshelfno.setText(null);
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // TODO add your handling code here:
        DeleteBookDetails();

    }//GEN-LAST:event_btn_deleteActionPerformed

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
            java.util.logging.Logger.getLogger(ManageBooks.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ManageBooks().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private necesario.RSMaterialButtonCircle btn_add;
    private necesario.RSMaterialButtonCircle btn_clear;
    private necesario.RSMaterialButtonCircle btn_delete;
    private rojeru_san.complementos.RSButtonHover btn_image_browse;
    private necesario.RSMaterialButtonCircle btn_update;
    private javax.swing.JComboBox<String> jComboBoxCourseName;
    private javax.swing.JComboBox<String> jComboBoxShelfNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_image;
    private rojeru_san.complementos.RSTableMetro tbl_book_details;
    private app.bolivia.swing.JCTextField txt_authorName;
    private app.bolivia.swing.JCTextField txt_book_id;
    private app.bolivia.swing.JCTextField txt_book_name;
    private javax.swing.JTextField txt_filtertable;
    private javax.swing.JTextField txt_imagePath;
    private app.bolivia.swing.JCTextField txt_publication;
    private app.bolivia.swing.JCTextField txt_quantity;
    // End of variables declaration//GEN-END:variables

}
