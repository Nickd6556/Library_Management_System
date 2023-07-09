/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Java_Class;

import java.awt.HeadlessException;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import project.ConnectionProvider;

/*
 *
 * @author Nikit
 */
public class IssueBook extends javax.swing.JFrame {

    /**
     * Creates new form IssueBook
     *
     * @throws java.text.ParseException
     */
    public IssueBook() throws ParseException {
        initComponents();
        //btn_issue_book.setEnabled(false);
        //Date
        //SimpleDateFormat dFormat = new SimpleDateFormat("MMM dd,yyyy");
        Date date = new Date();
        //lbl_date.setText(dFormat.format(date));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +0);
        Date d = c.getTime();
        date_issue_date.setDate(d);
        date_issue_date.getDateEditor().setEnabled(false);
        date_issue_date.getJCalendar().setMinSelectableDate(date);
        date_issue_date.getJCalendar().setMaxSelectableDate(d);

        //c.setTime(new Date());
        //System.out.println("Current Date = " + c.getTime());
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, +7);
        Date d1 = c1.getTime();
        date_due_date.setDate(d1);

        //txt_due_date.setText("" + dFormat.format(d1));
        //System.out.println("Updated Date = " + c.getTime());
        //Date d = dFormat.parse(d2);
        //System.out.println(d);
        date_due_date.getDateEditor().setEnabled(false);
        date_due_date.getJCalendar().setMinSelectableDate(d1);
        date_due_date.getJCalendar().setMaxSelectableDate(d1);

    }

    public boolean getBookDetails() {
        boolean bookdetails = false;

        String book_id = txt_book_id.getText();
        try {
            Connection con = ConnectionProvider.getCon();

            String sql = "Select * from book_details where book_id =?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bookdetails = true;

                txt_id_book.setText(rs.getString("book_id"));
                txt_book_name.setText(rs.getString("book_name"));
                txt_course_name.setText(rs.getString("course"));
                txt_publication.setText(rs.getString("publication"));
                txt_author.setText(rs.getString("author"));
                txt_book_shelf_no.setText(rs.getString("book_shelf_no"));
                txt_quantity.setText(rs.getString("quantity"));
                byte[] img = rs.getBytes("image");
                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myimage = im.getScaledInstance(lbl_book_image.getWidth(), lbl_book_image.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon newImage = new ImageIcon(myimage);
                lbl_book_image.setIcon(newImage);

            } else {
                bookdetails = false;
                JFrame jf = new JFrame();
                jf.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(jf, "Book Id doesnot Exist !");

            }
        } catch (HeadlessException | SecurityException | SQLException e) {
            JFrame jf = new JFrame();
            jf.setAlwaysOnTop(true);
            JOptionPane.showMessageDialog(jf, e);

        }
        return bookdetails;
    }

    public boolean getStudentDetails() {
        boolean studentdetails = false;
       String student_id = txt_student_id.getText();
        try {
            Connection con = ConnectionProvider.getCon();

            String sql = "Select * from student_details where student_id =?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, student_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                studentdetails = true;
                txt_id_student.setText(rs.getString("student_id"));
                txt_student_name.setText(rs.getString("student_name"));
                txt_semester.setText(rs.getString("semester"));
                txt_address.setText(rs.getString("address"));
                txt_email.setText(rs.getString("email"));
                txt_faculty.setText(rs.getString("faculty"));
                txt_contact.setText(rs.getString("contact"));
                byte[] img = rs.getBytes("image");
                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myimage = im.getScaledInstance(lbl_student_image.getWidth(), lbl_student_image.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon newImage = new ImageIcon(myimage);
                lbl_student_image.setIcon(newImage);

                //btn_issue_book.setEnabled(true);
            } else {
                studentdetails = false;
                JFrame jf = new JFrame();
                jf.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(jf, "Student Id doesnot Exist !");

            }
        } catch (HeadlessException | SecurityException | SQLException e) {
            JFrame jf = new JFrame();
            jf.setAlwaysOnTop(true);
            JOptionPane.showMessageDialog(jf, e);

        }
        return studentdetails;
    }
    //code to insert issue book details

    public boolean issueBook() {
        boolean isAdded = false;
        String book_id = txt_book_id.getText();
        String book_name = txt_book_name.getText();
        String student_id = txt_student_id.getText();
        String student_name = txt_student_name.getText();
//          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//          String issue_date = dateFormat.format(date_issue_date.getDate());
//          String due_date = dateFormat.format(date_due_date.getDate());
        java.util.Date uissue_date = date_issue_date.getDate();
        java.util.Date udue_date = date_due_date.getDate();
        Long l1 = uissue_date.getTime();
        Long l2 = udue_date.getTime();
        java.sql.Date sissue_date = new java.sql.Date(l1);
        java.sql.Date sdue_date = new java.sql.Date(l2);
        try {
            Connection con = ConnectionProvider.getCon();
            String sql = "Insert into issue_book_details(book_id,book_name,student_id,student_name,issue_date,due_date,status) values (?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book_id);
            ps.setString(2, book_name);
            ps.setString(3, student_id);
            ps.setString(4, student_name);
            ps.setDate(5, sissue_date);
            ps.setDate(6, sdue_date);
            ps.setString(7, "Pending");

            int updatedRowCount = ps.executeUpdate();

            if (isAdded = updatedRowCount > 0) {
                JOptionPane.showMessageDialog(this, "Book Issued !");

            } else {
                JOptionPane.showMessageDialog(this, "Failed To Issue Book !");
            }
        } catch (SQLException e) {

            e.printStackTrace();

        }
        return isAdded;
    }

    //Method to Checking whether book is already alocated or not
    public boolean isAlreadyIssued() {
        boolean isAlreadyIssued = false;
        String book_id = txt_book_id.getText() ;
       String student_id = txt_student_id.getText();

        try {
            Connection con = ConnectionProvider.getCon();
            String sql1 = "Select * from issue_book_details where book_id=? and student_id=? and status =?";
            PreparedStatement ps = con.prepareStatement(sql1);
            ps.setString(1, book_id);
            ps.setString(2, student_id);
            ps.setString(3, "pending");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                isAlreadyIssued = true;

            } else {
                isAlreadyIssued = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAlreadyIssued;
    }

    // Method to update book Count after issueing book
    public void updateBookCount() {
        String book_id = txt_book_id.getText();
        try {
            Connection con = ConnectionProvider.getCon();
            String sql = "Update book_details set quantity = quantity-1 where book_id =?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book_id);

            int rowCount = ps.executeUpdate();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Book Count Updated !");
                int initialCount = Integer.parseInt(txt_quantity.getText());
                txt_quantity.setText(Integer.toString(initialCount - 1));

            } else {
                JOptionPane.showMessageDialog(this, "Cant Update Book Count !");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbl_book_image = new javax.swing.JLabel();
        txt_id_book = new javax.swing.JLabel();
        txt_course_name = new javax.swing.JLabel();
        txt_publication = new javax.swing.JLabel();
        txt_id_book3 = new javax.swing.JLabel();
        txt_id_book4 = new javax.swing.JLabel();
        txt_author = new javax.swing.JLabel();
        txt_book_shelf_no = new javax.swing.JLabel();
        txt_book_name = new javax.swing.JLabel();
        txt_quantity = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbl_student_image = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txt_id_student = new javax.swing.JLabel();
        txt_student_name = new javax.swing.JLabel();
        txt_contact = new javax.swing.JLabel();
        txt_semester = new javax.swing.JLabel();
        txt_address = new javax.swing.JLabel();
        txt_email = new javax.swing.JLabel();
        txt_faculty = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txt_student_id = new app.bolivia.swing.JCTextField();
        btn_issue_book = new rojerusan.RSButtonHover();
        date_due_date = new com.toedter.calendar.JDateChooser();
        date_issue_date = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        txt_book_id = new app.bolivia.swing.JCTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1920, 1080));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel1.setkBorderRadius(2);
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 102, 102));
        kGradientPanel1.setkStartColor(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 204));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back.png"))); // NOI18N
        jLabel1.setText(" Back");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Sitka Heading", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Literature_100px_1.png"))); // NOI18N
        jLabel3.setText("  Book Details");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(380, 4));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Course Name    :");

        jLabel6.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Book Id              :");

        jLabel7.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Book Name        :");

        jLabel8.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Publication       :");

        jLabel9.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Author               :");

        jLabel10.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Book Shelf No  :");

        jLabel11.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Quantity            :");

        jLabel12.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Cover                :");

        lbl_book_image.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));

        txt_id_book.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_id_book.setForeground(new java.awt.Color(255, 255, 255));

        txt_course_name.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_course_name.setForeground(new java.awt.Color(255, 255, 255));

        txt_publication.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_publication.setForeground(new java.awt.Color(255, 255, 255));

        txt_id_book3.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_id_book3.setForeground(new java.awt.Color(255, 255, 255));

        txt_id_book4.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_id_book4.setForeground(new java.awt.Color(255, 255, 255));

        txt_author.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_author.setForeground(new java.awt.Color(255, 255, 255));

        txt_book_shelf_no.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_book_shelf_no.setForeground(new java.awt.Color(255, 255, 255));

        txt_book_name.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_book_name.setForeground(new java.awt.Color(255, 255, 255));

        txt_quantity.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_quantity.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_publication, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_course_name, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txt_author, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txt_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txt_book_shelf_no, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_id_book3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(lbl_book_image, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_id_book, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_book_name, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel3))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                    .addContainerGap(450, Short.MAX_VALUE)
                    .addComponent(txt_id_book4, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(27, 27, 27)))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel6))
                    .addComponent(txt_id_book, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txt_book_name, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txt_course_name, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(txt_publication, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel9))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_author, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txt_id_book3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txt_book_shelf_no, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(txt_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jLabel12))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(lbl_book_image, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(kGradientPanel1Layout.createSequentialGroup()
                    .addGap(371, 371, 371)
                    .addComponent(txt_id_book4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(490, Short.MAX_VALUE)))
        );

        jPanel1.add(kGradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 890));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1510, 0, 40, 50));

        kGradientPanel2.setForeground(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setkBorderRadius(2);
        kGradientPanel2.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 153, 153));
        kGradientPanel2.setPreferredSize(new java.awt.Dimension(500, 960));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(380, 4));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Sitka Heading", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Student_Registration_100px_2.png"))); // NOI18N
        jLabel4.setText("  Student Details");

        lbl_student_image.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));

        jLabel15.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Image                 :");

        jLabel16.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Contact              :");

        jLabel17.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Faculty              :");

        jLabel18.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Email                 :");

        jLabel19.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Address             :");

        jLabel20.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Semester           :");

        jLabel21.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Student Name  :");

        jLabel22.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Student Id        :");

        txt_id_student.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_id_student.setForeground(new java.awt.Color(255, 255, 255));

        txt_student_name.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_student_name.setForeground(new java.awt.Color(255, 255, 255));

        txt_contact.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_contact.setForeground(new java.awt.Color(255, 255, 255));

        txt_semester.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_semester.setForeground(new java.awt.Color(255, 255, 255));

        txt_address.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_address.setForeground(new java.awt.Color(255, 255, 255));

        txt_email.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_email.setForeground(new java.awt.Color(255, 255, 255));

        txt_faculty.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_faculty.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_semester, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_faculty, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(lbl_student_image, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_id_student, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_student_name, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(txt_id_student, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_student_name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(txt_semester, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(txt_address, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(txt_faculty, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(txt_contact, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jLabel15))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lbl_student_image, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel1.add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 0, 520, 890));

        jPanel5.setBackground(new java.awt.Color(102, 51, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 4));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 170, -1, -1));

        jLabel23.setFont(new java.awt.Font("Sitka Text", 1, 36)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 0, 0));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Books_52px_1.png"))); // NOI18N
        jLabel23.setText("  Issue Book");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 100, 300, -1));

        jLabel25.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel25.setText("Book Id       :");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 340, -1, 30));

        jLabel26.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel26.setText("Student Id  :");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 420, -1, 30));

        jLabel27.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel27.setText("Issue Date  :");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 500, -1, 30));

        txt_student_id.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 204)));
        txt_student_id.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txt_student_id.setPlaceholder("Enter Student Id");
        txt_student_id.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_student_idFocusLost(evt);
            }
        });
        jPanel1.add(txt_student_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 410, 220, 30));

        btn_issue_book.setText("ISSUE BOOK");
        btn_issue_book.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_issue_bookActionPerformed(evt);
            }
        });
        jPanel1.add(btn_issue_book, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 700, 330, -1));

        date_due_date.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 102, 204)));
        date_due_date.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        date_due_date.setMaxSelectableDate(new java.util.Date(253370747809000L));
        date_due_date.setMinSelectableDate(new java.util.Date(-62135786591000L));
        jPanel1.add(date_due_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 580, 260, 30));

        date_issue_date.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 102, 204)));
        date_issue_date.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        date_issue_date.setMaxSelectableDate(new java.util.Date(253370747770000L));
        date_issue_date.setMinSelectableDate(new java.util.Date(-62135786630000L));
        jPanel1.add(date_issue_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 500, 260, 30));

        jLabel28.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel28.setText("Due Date    :");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 580, -1, 30));

        txt_book_id.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 204)));
        txt_book_id.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txt_book_id.setPlaceholder("Enter Book Id");
        txt_book_id.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_book_idFocusLost(evt);
            }
        });
        jPanel1.add(txt_book_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 340, 220, 30));

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 210, 500, 670));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/minimize.png"))); // NOI18N
        jLabel24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel24MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 0, 40, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1550, 890));

        setSize(new java.awt.Dimension(1550, 893));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Exit ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
            dispose();
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void btn_issue_bookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_issue_bookActionPerformed
        if (txt_quantity.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Book Is Not Available !");
        } else {
            if (isAlreadyIssued() == false) {

                if ((getBookDetails() == false) || (getStudentDetails() == false)) {

                } else {
                    if (issueBook() == true) {
                        updateBookCount();
                        //clearing the Fields
                        txt_student_id.setText(null);
                        txt_book_id.setText(null);
                    }

                }

            } else {
                JOptionPane.showMessageDialog(this, "Student Already Has This Book !");
            }
        }

    }//GEN-LAST:event_btn_issue_bookActionPerformed

    private void txt_book_idFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_book_idFocusLost
        // TODO add your handling code here:

        if (!txt_book_id.getText().equals("")) {
            getBookDetails();
        }
    }//GEN-LAST:event_txt_book_idFocusLost

    private void txt_student_idFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_student_idFocusLost
        // TODO add your handling code here:
        if (!txt_student_id.getText().equals("")) {
            getStudentDetails();
        }
    }//GEN-LAST:event_txt_student_idFocusLost

    private void jLabel24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseClicked
        // TODO add your handling code here:
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel24MouseClicked

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
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new IssueBook().setVisible(true);
            } catch (ParseException ex) {
                Logger.getLogger(IssueBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSButtonHover btn_issue_book;
    private com.toedter.calendar.JDateChooser date_due_date;
    private com.toedter.calendar.JDateChooser date_issue_date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField jTextField1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel lbl_book_image;
    private javax.swing.JLabel lbl_student_image;
    private javax.swing.JLabel txt_address;
    private javax.swing.JLabel txt_author;
    private app.bolivia.swing.JCTextField txt_book_id;
    private javax.swing.JLabel txt_book_name;
    private javax.swing.JLabel txt_book_shelf_no;
    private javax.swing.JLabel txt_contact;
    private javax.swing.JLabel txt_course_name;
    private javax.swing.JLabel txt_email;
    private javax.swing.JLabel txt_faculty;
    private javax.swing.JLabel txt_id_book;
    private javax.swing.JLabel txt_id_book3;
    private javax.swing.JLabel txt_id_book4;
    private javax.swing.JLabel txt_id_student;
    private javax.swing.JLabel txt_publication;
    private javax.swing.JLabel txt_quantity;
    private javax.swing.JLabel txt_semester;
    private app.bolivia.swing.JCTextField txt_student_id;
    private javax.swing.JLabel txt_student_name;
    // End of variables declaration//GEN-END:variables
}
