package Java_Class;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import project.ConnectionProvider;

/**
 *
 * @author NIKIT
 */
public class Signup extends javax.swing.JFrame {

    public Signup() {
        initComponents();
    }

    //method to insert values into table users
    public void InsertSignupDetails() throws NoSuchPaddingException, SQLException {

        String fullname = txt_fullname.getText();
        String address = txt_address.getText();
        String email = txt_email.getText();
        String password = txt_password.getText();
        String contact = txt_contact.getText();
        try {
            Connection con = ConnectionProvider.getCon();
            String sql = "Insert into users(full_name,address,email,password,contact) values (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fullname);
            ps.setString(2, address);
            ps.setString(3, email);

            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            ps.setString(4, hashtext);
            ps.setString(5, contact);

            int updatedRowCount = ps.executeUpdate();

            if (updatedRowCount > 0) {
                JOptionPane.showMessageDialog(this, "Successfully Registered !");

            } else {
                JOptionPane.showMessageDialog(this, "Can`t Register !");

            }

            //System.out.println(generatedPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //validation

    public boolean validateSignup() {
        String fullname = txt_fullname.getText();
        String address = txt_address.getText();
        String email = txt_email.getText();
        String password = txt_password.getText();
        String contact = txt_contact.getText();

        if (fullname.equals("") || !fullname.matches("^[a-zA-Z ]{0,30}$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Name !");
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
        if (password.equals("") || password.contains("*")) {
            JOptionPane.showMessageDialog(this, "Please enter Password !");
            return false;
        }

        return true;

    }

    public boolean CheckDuplicateUser() {
        String fullname = txt_fullname.getText();
        boolean isExist = false;
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("Select * from users where full_name=?");
            ps.setString(1, fullname);
            ps.executeQuery();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;

            } else {
                isExist = false;
            }

        } catch (SQLException e) {
        }
        return isExist;
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_fullname = new app.bolivia.swing.JCTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jCTextField2 = new app.bolivia.swing.JCTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        btn_login = new necesario.RSMaterialButtonCircle();
        btn_signup = new necesario.RSMaterialButtonCircle();
        jLabel2 = new javax.swing.JLabel();
        txt_address = new app.bolivia.swing.JCTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        jCheckBox1 = new javax.swing.JCheckBox();
        txt_contact = new app.bolivia.swing.JCTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Sitka Text", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 204));
        jLabel1.setText("Advance Library");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, 400, 70));

        jLabel4.setFont(new java.awt.Font("Sitka Text", 1, 48)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setText("Welcome To");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 300, 50));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/signup-library-icon.png"))); // NOI18N
        jLabel5.setText(" ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 850, 650));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 890));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/man.png"))); // NOI18N
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 50, 50));

        jLabel7.setFont(new java.awt.Font("Sitka Text", 1, 50)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Signup.png"))); // NOI18N
        jLabel7.setText(" Sign Up ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 330, 90));

        jLabel8.setFont(new java.awt.Font("Sitka Text", 1, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Already have account ?");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 830, 180, 40));

        txt_fullname.setBackground(new java.awt.Color(0, 102, 102));
        txt_fullname.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_fullname.setForeground(new java.awt.Color(255, 255, 255));
        txt_fullname.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_fullname.setPhColor(new java.awt.Color(255, 255, 255));
        txt_fullname.setPlaceholder("Enter Username");
        jPanel2.add(txt_fullname, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 310, -1));

        jLabel9.setFont(new java.awt.Font("Sitka Text", 1, 27)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Full Name:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 180, 60));

        jPanel3.setBackground(new java.awt.Color(51, 51, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Account_50px.png"))); // NOI18N
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 50, 50));

        jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 48)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Sign Up");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 180, 60));

        jLabel12.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Create new account");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 160, 60));

        jCTextField2.setBackground(new java.awt.Color(102, 102, 255));
        jCTextField2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        jCTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jCTextField2.setPlaceholder("Enter username");
        jPanel3.add(jCTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 310, -1));

        jLabel13.setFont(new java.awt.Font("Script MT Bold", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("UserName:");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 160, 60));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 0, 560, 850));

        jLabel14.setFont(new java.awt.Font("Sitka Text", 1, 27)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Password :");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 550, 160, 70));

        jLabel15.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/padlock_1.png"))); // NOI18N
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 610, 50, 50));

        jLabel16.setFont(new java.awt.Font("Sitka Text", 1, 27)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Email :");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, 160, 60));

        jLabel17.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email.png"))); // NOI18N
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 50, 50));

        txt_email.setBackground(new java.awt.Color(0, 102, 102));
        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_email.setForeground(new java.awt.Color(255, 255, 255));
        txt_email.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_email.setPhColor(new java.awt.Color(255, 255, 255));
        txt_email.setPlaceholder("Enter Email");
        jPanel2.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 400, 310, -1));

        btn_login.setBackground(new java.awt.Color(0, 102, 102));
        btn_login.setText("LOGIN");
        btn_login.setFont(new java.awt.Font("Sitka Text", 1, 16)); // NOI18N
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });
        jPanel2.add(btn_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 820, 80, 60));

        btn_signup.setBackground(new java.awt.Color(255, 51, 51));
        btn_signup.setText("SignUp");
        btn_signup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signupActionPerformed(evt);
            }
        });
        jPanel2.add(btn_signup, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 730, 290, 80));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 30, 40));

        txt_address.setBackground(new java.awt.Color(0, 102, 102));
        txt_address.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_address.setForeground(new java.awt.Color(255, 255, 255));
        txt_address.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_address.setPhColor(new java.awt.Color(255, 255, 255));
        txt_address.setPlaceholder("Enter Address");
        jPanel2.add(txt_address, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 310, -1));

        jLabel20.setFont(new java.awt.Font("Sitka Text", 1, 27)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Address :");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 180, 60));

        jLabel21.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/location.png"))); // NOI18N
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 50, 50));

        txt_password.setBackground(new java.awt.Color(0, 102, 102));
        txt_password.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_password.setForeground(new java.awt.Color(255, 255, 255));
        txt_password.setText("*************");
        txt_password.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        jPanel2.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 620, 310, 30));

        jCheckBox1.setBackground(new java.awt.Color(0, 102, 102));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("  Show Password");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 670, -1, -1));

        txt_contact.setBackground(new java.awt.Color(0, 102, 102));
        txt_contact.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_contact.setForeground(new java.awt.Color(255, 255, 255));
        txt_contact.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_contact.setPhColor(new java.awt.Color(255, 255, 255));
        txt_contact.setPlaceholder("Enter Contact");
        jPanel2.add(txt_contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 510, 310, -1));

        jLabel19.setFont(new java.awt.Font("Script MT Bold", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contact.png"))); // NOI18N
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, 50, 50));

        jLabel18.setFont(new java.awt.Font("Sitka Text", 1, 27)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Contact :");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 450, 160, 60));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 480, 890));

        setSize(new java.awt.Dimension(1376, 890));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_signupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_signupActionPerformed
        // TODO add your handling code here:
        if (validateSignup() == true) {
            if (CheckDuplicateUser() == false) {
                try {
                    InsertSignupDetails();
                    setVisible(false);
                    new Login().setVisible(true);
                } catch (NoSuchPaddingException | SQLException ex) {
                    Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "User already Exist !");

            }
        }
    }//GEN-LAST:event_btn_signupActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Exit ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        // TODO add your handling code here:
        Signup s = new Signup();
        s.setVisible(false);
        new Login().setVisible(true);
        dispose();
    }//GEN-LAST:event_btn_loginActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            txt_password.setEchoChar((char) 0);
        } else {
            txt_password.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(Signup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Signup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Signup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Signup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Signup().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private necesario.RSMaterialButtonCircle btn_login;
    private necesario.RSMaterialButtonCircle btn_signup;
    private app.bolivia.swing.JCTextField jCTextField2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private app.bolivia.swing.JCTextField txt_address;
    private app.bolivia.swing.JCTextField txt_contact;
    private app.bolivia.swing.JCTextField txt_email;
    private app.bolivia.swing.JCTextField txt_fullname;
    private javax.swing.JPasswordField txt_password;
    // End of variables declaration//GEN-END:variables
}