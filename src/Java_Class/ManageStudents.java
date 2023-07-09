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
public final class ManageStudents extends javax.swing.JFrame {

    public static int open = 0;
    /**
     * Creates new form ManageBooks
     */
    String student_name, address, email, faculty, contact, semester, student_id;
    DefaultTableModel model;
    String newImagePath = "";

    public ManageStudents() {
        initComponents();
        FetchStudentDetailsToTable("");
        randomId();

    }

    public void randomId() {

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select count(Student_id) from student_details");
            if (rs.first()) {
                int min = 10000;
                int max = 35000;

                String b1 = getAlphaNumericString(1);

                //Generate random int value 
                int b2 = (int) (Math.random() * (max - min + 1) + min);
                //System.out.println(b);

                String b = b2 + b1;

                txt_student_id.setText(b);
                txt_student_id.setEditable(false);
            } else {
                txt_student_id.setText("");
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
    public ArrayList<Student_ArrayList> getList(String searchTerm) {
        ArrayList<Student_ArrayList> getList = new ArrayList<>();

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT student_id,student_name,semester,address,email,faculty,contact,image_path,image from student_details WHERE concat(student_id,student_name,semester,address,email,faculty,contact,image_path,image) LIKE '%" + searchTerm + "%'");

            while (rs.next()) {
                Student_ArrayList bd = new Student_ArrayList();
                bd.setStudent_id(rs.getString("student_id"));
                bd.setStudent_name(rs.getString("student_name"));
                bd.setSemester(rs.getString("semester"));
                bd.setAddress(rs.getString("address"));
                bd.setEmail(rs.getString("email"));
                bd.setFaculty(rs.getString("faculty"));
                bd.setContact(rs.getString("contact"));
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
    public void FetchStudentDetailsToTable(String searchTermfetch) {

        ArrayList<Student_ArrayList> datArrayList = getList(searchTermfetch);
        model = (DefaultTableModel) tbl_student_details.getModel();
        model.setRowCount(0);

        Object[] rows = new Object[9];
        //loop through the arraylist to populate jtable

        for (int i = 0; i < datArrayList.size(); i++) {
            rows[0] = datArrayList.get(i).getStudent_id();
            rows[1] = datArrayList.get(i).getStudent_name();
            rows[3] = datArrayList.get(i).getSemester();
            rows[4] = datArrayList.get(i).getAddress();
            rows[5] = datArrayList.get(i).getEmail();
            rows[2] = datArrayList.get(i).getFaculty();
            rows[6] = datArrayList.get(i).getContact();
            rows[7] = datArrayList.get(i).getImage_path();
            rows[8] = datArrayList.get(i).getImage();
            model.addRow(rows);

        }

    }

    //method to add books
    public boolean AddStudent() throws FileNotFoundException {
        boolean isAdded = false;
        student_id = txt_student_id.getText();
        student_name = txt_student_name.getText();
        semester = jComboBoxSemester.getSelectedItem().toString();
        address = txt_address.getText();
        email = txt_email.getText();
        faculty = jComboBoxfaculty.getSelectedItem().toString();
        contact = txt_contact.getText();
        newImagePath = txt_imagePath.getText();
        File image = new File(newImagePath);

        try {
            InputStream is = new FileInputStream(image);
            Connection con = ConnectionProvider.getCon();
            String sql = "Insert into student_details(student_id,student_name,semester,address,email,faculty,contact,image_path,image) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, student_id);
            ps.setString(2, student_name);
            ps.setString(3, semester);
            ps.setString(4, address);
            ps.setString(5, email);
            ps.setString(6, faculty);
            ps.setString(7, contact);
            ps.setString(8, newImagePath);
            ps.setBlob(9, is);

            int updatedRowCount = ps.executeUpdate();

            isAdded = updatedRowCount > 0;

        } catch (HeadlessException | SQLException e) {
            e.getMessage();

        }

        return isAdded;

    }

    // Method to validate fields
    public boolean validateAddStudent() {

        student_name = txt_student_name.getText();
        address = txt_address.getText();
        email = txt_email.getText();
        contact = txt_contact.getText();

        if (student_name.equals("") || !student_name.matches("^[a-zA-Z ]{0,100}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Student Name !");
            return false;
        }

        if (address.equals("") || !address.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Address !");
            return false;
        }
        if (email.equals("") || !email.matches("^.+@.+\\..+$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Email !");
            return false;
        }

        if (contact.equals("") || !contact.matches("^[0-9]{0,15}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Contact !");
            return false;
        } else if (!(contact.length() == 10)) {
            JOptionPane.showMessageDialog(this, "Please enter valid Contact !");
            return false;
        }

        return true;

    }

    //Method to Update Book Details
    public boolean UpdateBookDetails() throws FileNotFoundException {
        boolean isUpdated = false;
        student_id = txt_student_id.getText();
        student_name = txt_student_name.getText();
        semester = jComboBoxSemester.getSelectedItem().toString();
        address = txt_address.getText();
        email = txt_email.getText();
        faculty = jComboBoxfaculty.getSelectedItem().toString();
        contact = txt_contact.getText();
        newImagePath = txt_imagePath.getText();
        File image = new File(newImagePath);
        try {
            InputStream is = new FileInputStream(image);
            Connection con = ConnectionProvider.getCon();
            String sql = "Update student_details set student_name=?,semester=?,address=?,email=?,faculty=?,contact=?,image_path=?,image=? where student_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, student_name);
            ps.setString(2, semester);
            ps.setString(3, address);
            ps.setString(4, email);
            ps.setString(5, faculty);
            ps.setString(6, contact);
            ps.setString(7, newImagePath);
            ps.setBlob(8, is);
            ps.setString(9, student_id);

            int updatedRowCount = ps.executeUpdate();

            isUpdated = updatedRowCount > 0;

        } catch (HeadlessException | SQLException e) {
            e.getMessage();
        }

        return isUpdated;

    }

    //Method to Delete Student Details
    public boolean DeleteStudentDetails() {
        boolean isDeleted = false;
        int JTableSelectedRow = tbl_student_details.getSelectedRow();
        student_id = txt_student_id.getText();
        if (JTableSelectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No Student Selected !");
        } else {
            try {
                Connection con = ConnectionProvider.getCon();
                String sql = "Delete from student_details where student_id =?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, student_id);

                int updatedRowCount = ps.executeUpdate();

                if (isDeleted = updatedRowCount > 0) {
                    JOptionPane.showMessageDialog(this, "Student Details Deleted !");
                    ClearTable();
                    FetchStudentDetailsToTable("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed To Delete Student Details !");

                }

            } catch (HeadlessException | SQLException e) {
                e.getMessage();
            }
        }
        return isDeleted;

    }

    //Method to clear table
    public void ClearTable() {
        DefaultTableModel m = (DefaultTableModel) tbl_student_details.getModel();
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
        txt_student_id = new app.bolivia.swing.JCTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_student_name = new app.bolivia.swing.JCTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txt_contact = new app.bolivia.swing.JCTextField();
        btn_clear = new necesario.RSMaterialButtonCircle();
        btn_add = new necesario.RSMaterialButtonCircle();
        btn_update = new necesario.RSMaterialButtonCircle();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lbl_image = new javax.swing.JLabel();
        btn_image_browse = new rojeru_san.complementos.RSButtonHover();
        btn_delete1 = new necesario.RSMaterialButtonCircle();
        txt_address = new app.bolivia.swing.JCTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jComboBoxfaculty = new javax.swing.JComboBox<>();
        jComboBoxSemester = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_student_details = new rojeru_san.complementos.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txt_imagePath = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_filtertable = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();

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
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/student_id.png"))); // NOI18N
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 50, 50));

        jLabel18.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Student ID :");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 200, 60));

        txt_student_id.setBackground(new java.awt.Color(0, 102, 102));
        txt_student_id.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_student_id.setForeground(new java.awt.Color(255, 255, 255));
        txt_student_id.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_student_id.setPhColor(new java.awt.Color(255, 255, 255));
        txt_student_id.setPlaceholder("Student ID");
        jPanel1.add(txt_student_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 290, -1));

        jLabel20.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/man.png"))); // NOI18N
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 50, 50));

        jLabel21.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Student Name :");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 220, 60));

        txt_student_name.setBackground(new java.awt.Color(0, 102, 102));
        txt_student_name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_student_name.setForeground(new java.awt.Color(255, 255, 255));
        txt_student_name.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_student_name.setPhColor(new java.awt.Color(255, 255, 255));
        txt_student_name.setPlaceholder("Enter Student Name");
        jPanel1.add(txt_student_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 290, -1));

        jLabel24.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contact.png"))); // NOI18N
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 720, 50, 50));

        jLabel25.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Contact :");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 670, 160, 60));

        txt_contact.setBackground(new java.awt.Color(0, 102, 102));
        txt_contact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_contact.setForeground(new java.awt.Color(255, 255, 255));
        txt_contact.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_contact.setPhColor(new java.awt.Color(255, 255, 255));
        txt_contact.setPlaceholder("Enter Contact");
        jPanel1.add(txt_contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 730, 290, -1));

        btn_clear.setBackground(new java.awt.Color(255, 51, 51));
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });
        jPanel1.add(btn_clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 1010, 90, 50));

        btn_add.setBackground(new java.awt.Color(255, 51, 51));
        btn_add.setText("ADD");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        jPanel1.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1010, 90, 50));

        btn_update.setBackground(new java.awt.Color(255, 51, 51));
        btn_update.setText("update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel1.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 1010, 90, 50));

        jLabel26.setFont(new java.awt.Font("Sitka Text", 1, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Image :");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 800, 100, 50));

        jLabel27.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/camera.png"))); // NOI18N
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 870, 50, 50));

        lbl_image.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        jPanel1.add(lbl_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 780, 160, 200));

        btn_image_browse.setBackground(new java.awt.Color(0, 102, 102));
        btn_image_browse.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btn_image_browse.setText("Browse");
        btn_image_browse.setColorHover(new java.awt.Color(0, 153, 0));
        btn_image_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_image_browseActionPerformed(evt);
            }
        });
        jPanel1.add(btn_image_browse, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 870, 110, 40));

        btn_delete1.setBackground(new java.awt.Color(255, 51, 51));
        btn_delete1.setText("Delete");
        btn_delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delete1ActionPerformed(evt);
            }
        });
        jPanel1.add(btn_delete1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 1010, 90, 50));

        txt_address.setBackground(new java.awt.Color(0, 102, 102));
        txt_address.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_address.setForeground(new java.awt.Color(255, 255, 255));
        txt_address.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_address.setPhColor(new java.awt.Color(255, 255, 255));
        txt_address.setPlaceholder("Enter Address");
        jPanel1.add(txt_address, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 290, -1));

        jLabel28.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Address :");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 200, 60));

        jLabel29.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/location.png"))); // NOI18N
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 50, 50));

        jLabel30.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Email :");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 200, 60));

        txt_email.setBackground(new java.awt.Color(0, 102, 102));
        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_email.setForeground(new java.awt.Color(255, 255, 255));
        txt_email.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_email.setPhColor(new java.awt.Color(255, 255, 255));
        txt_email.setPlaceholder("Enter Email");
        jPanel1.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, 290, -1));

        jLabel31.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email.png"))); // NOI18N
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, 50, 50));

        jLabel32.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Faculty :");
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 200, 60));

        jLabel33.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/faculty.png"))); // NOI18N
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 620, 50, 50));

        jComboBoxfaculty.setBackground(new java.awt.Color(0, 102, 102));
        jComboBoxfaculty.setFont(new java.awt.Font("Sitka Text", 0, 20)); // NOI18N
        jComboBoxfaculty.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxfaculty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BCA", "BBM", "BHM", "BASW", "BBS" }));
        jComboBoxfaculty.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel1.add(jComboBoxfaculty, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 630, 290, -1));

        jComboBoxSemester.setBackground(new java.awt.Color(0, 102, 102));
        jComboBoxSemester.setFont(new java.awt.Font("Sitka Text", 0, 20)); // NOI18N
        jComboBoxSemester.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SEM-1", "SEM-2", "SEM-3", "SEM-4", "SEM-5", "SEM-6", "SEM-7", "SEM-8" }));
        jComboBoxSemester.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel1.add(jComboBoxSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 290, -1));

        jLabel34.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/semester.png"))); // NOI18N
        jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 50, 50));

        jLabel35.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Semester :");
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, 200, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 1080));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 0, 40, 50));

        tbl_student_details.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Student Id", "Student Name", "Faculty", "Semester", "Address", "Email", "Contact"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_student_details.setColorBackgoundHead(new java.awt.Color(0, 102, 102));
        tbl_student_details.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_student_details.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tbl_student_details.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tbl_student_details.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tbl_student_details.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tbl_student_details.setFuenteFilas(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tbl_student_details.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        tbl_student_details.setFuenteHead(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        tbl_student_details.setRowHeight(34);
        tbl_student_details.setRowMargin(3);
        tbl_student_details.setSelectionBackground(new java.awt.Color(102, 102, 255));
        tbl_student_details.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_student_detailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_student_details);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 1430, 480));

        jLabel3.setFont(new java.awt.Font("Sitka Text", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Student_Male_100px.png"))); // NOI18N
        jLabel3.setText("  Manage Students");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, 490, -1));

        jPanel3.setBackground(new java.awt.Color(0, 102, 51));
        jPanel3.setPreferredSize(new java.awt.Dimension(550, 4));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, -1, -1));

        txt_imagePath.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        txt_imagePath.setForeground(new java.awt.Color(255, 255, 255));
        txt_imagePath.setText("jTextField1");
        txt_imagePath.setBorder(null);
        jPanel2.add(txt_imagePath, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 760, 20, 30));

        jLabel4.setFont(new java.awt.Font("Sitka Text", 1, 26)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Filter details :");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 200, 60));

        txt_filtertable.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        txt_filtertable.setForeground(new java.awt.Color(255, 0, 0));
        txt_filtertable.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 102, 0)));
        txt_filtertable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_filtertableKeyReleased(evt);
            }
        });
        jPanel2.add(txt_filtertable, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 150, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("        Click Here To Create Membership Card  ");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 1010, 430, 70));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/minimize.png"))); // NOI18N
        jLabel36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel36MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(1420, 0, 40, 50));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 0, 1510, 1080));

        setSize(new java.awt.Dimension(1920, 1080));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // TODO add your handling code here:
        randomId();
        txt_address.setText(null);
        txt_student_name.setText(null);
        jComboBoxSemester.setSelectedItem(null);
        txt_contact.setText(null);
        txt_email.setText(null);
        txt_imagePath.setText(null);
        lbl_image.setIcon(null);
        jComboBoxfaculty.setSelectedItem(null);
        //txt_bookshelfno.setText(null);

    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed

        if (validateAddStudent() == true) {
            try {
                if (AddStudent() == true) {
                    JOptionPane.showMessageDialog(this, "Student Added !");
                    ClearTable();
                    FetchStudentDetailsToTable(" ");

                } else {

                    JOptionPane.showMessageDialog(this, "Failed To Add Student !");

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManageStudents.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        try {
            // TODO add your handling code here:
            if (UpdateBookDetails() == true) {
                JOptionPane.showMessageDialog(this, "Student Updated !");
                ClearTable();
                FetchStudentDetailsToTable(" ");

            } else {

                JOptionPane.showMessageDialog(this, "Failed To Update Student !");

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManageStudents.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        JFrame jf = new JFrame();
        jf.setAlwaysOnTop(true);
        int a = JOptionPane.showConfirmDialog(jf, "Do you really want to Exit ?", "Select", JOptionPane.YES_NO_OPTION);
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

    private void tbl_student_detailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_student_detailsMouseClicked
        // TODO add your handling code here:
        int JTableSelectedRow = tbl_student_details.getSelectedRow();

        txt_student_id.setText(getList("").get(JTableSelectedRow).getStudent_id());
        txt_student_name.setText(getList("").get(JTableSelectedRow).getStudent_name());
        jComboBoxSemester.setSelectedItem(getList("").get(JTableSelectedRow).getSemester());
        txt_address.setText(getList("").get(JTableSelectedRow).getAddress());
        txt_email.setText(getList("").get(JTableSelectedRow).getEmail());
        jComboBoxfaculty.setSelectedItem(getList("").get(JTableSelectedRow).getFaculty());
        txt_contact.setText(getList("").get(JTableSelectedRow).getContact());
        txt_imagePath.setText(getList("").get(JTableSelectedRow).getImage_path());
        lbl_image.setIcon(new ImageIcon(new ImageIcon(getList("").get(JTableSelectedRow).getImage()).getImage().getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH)));
    }//GEN-LAST:event_tbl_student_detailsMouseClicked

    private void btn_delete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_delete1ActionPerformed

        // TODO add your handling code here:
        DeleteStudentDetails();
//        if (DeleteStudentDetails() == true) {
//            JOptionPane.showMessageDialog(this, "Student Details Deleted !");
//            ClearTable();
//            FetchStudentDetailsToTable(" ");
//        } else {
//            JOptionPane.showMessageDialog(this, "Failed To Delete Student Details !");
//        }

    }//GEN-LAST:event_btn_delete1ActionPerformed

    private void txt_filtertableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filtertableKeyReleased
        FetchStudentDetailsToTable(txt_filtertable.getText());
    }//GEN-LAST:event_txt_filtertableKeyReleased

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:

        if (open == 0) {
            new LibraryCard().setVisible(true);
            open = 1;
        } else {
            JFrame jf = new JFrame();
            jf.setAlwaysOnTop(true);
            JOptionPane.showMessageDialog(jf, "Page is Already Open !");
        }
//        setVisible(false);
//        new LibraryCard().setVisible(true);
//        dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel36MouseClicked
        // TODO add your handling code here:
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel36MouseClicked

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
            java.util.logging.Logger.getLogger(ManageStudents.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        java.awt.EventQueue.invokeLater(() -> {
            new ManageStudents().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private necesario.RSMaterialButtonCircle btn_add;
    private necesario.RSMaterialButtonCircle btn_clear;
    private necesario.RSMaterialButtonCircle btn_delete1;
    private rojeru_san.complementos.RSButtonHover btn_image_browse;
    private necesario.RSMaterialButtonCircle btn_update;
    private javax.swing.JComboBox<String> jComboBoxSemester;
    private javax.swing.JComboBox<String> jComboBoxfaculty;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_image;
    private rojeru_san.complementos.RSTableMetro tbl_student_details;
    private app.bolivia.swing.JCTextField txt_address;
    private app.bolivia.swing.JCTextField txt_contact;
    private app.bolivia.swing.JCTextField txt_email;
    private javax.swing.JTextField txt_filtertable;
    private javax.swing.JTextField txt_imagePath;
    private app.bolivia.swing.JCTextField txt_student_id;
    private app.bolivia.swing.JCTextField txt_student_name;
    // End of variables declaration//GEN-END:variables

}
