/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Java_Class;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import project.ConnectionProvider;

/**
 *
 * @author NIKIT
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form HomePage
     */
    Color mouseEnterColor = new Color(0, 0, 0);
    Color mouseExitColor = new Color(51, 51, 51);

    public Dashboard() {

        initComponents();
        //showPieChart();
        showBarChart();
        getImage();
        SetDataToCards();
    }

    public final void SetDataToCards() {
        Statement st = null;
        ResultSet rs = null;
        Long l = System.currentTimeMillis();
        Date todaysdate = new Date(l);
        try {
            Connection con = ConnectionProvider.getCon();
            st = con.createStatement();
            rs = st.executeQuery("Select * from book_details");
            rs.last();
            book_count.setText(Integer.toString(rs.getRow()));
            rs = st.executeQuery("Select * from student_details");
            rs.last();
            student_count.setText(Integer.toString(rs.getRow()));
            rs = st.executeQuery("Select * from issue_book_details where status ='" + "Pending" + "'");
            rs.last();
            issued_book_count.setText(Integer.toString(rs.getRow()));
            rs = st.executeQuery("Select * from issue_book_details where due_date < '" + todaysdate + "' and status = '" + "Pending" + "'");
            rs.last();
            pending_book_count.setText(Integer.toString(rs.getRow()));

        } catch (SQLException e) {
        }
    }

//    public void showPieChart() {
//
//        //create dataset
//        DefaultPieDataset barDataset = new DefaultPieDataset();
//        try {
//            Connection con = ConnectionProvider.getCon();
//            String sql = "Select book_name ,count(*) as issue_count  from issue_book_details GROUP BY book_id";
//            Statement s = con.createStatement();
//            ResultSet rs = s.executeQuery(sql);
//            while (rs.next()) {
//                barDataset.setValue(rs.getString("book_name"), new Double(rs.getDouble("issue_count")));
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //create chart
//        JFreeChart piechart = ChartFactory.createPieChart("Issued Books", barDataset, false, true, false);
//
//        PiePlot piePlot = (PiePlot) piechart.getPlot();
//
//        //changing pie chart blocks colors
////        piePlot.setSectionPaint("IPhone 5s", new Color(255, 255, 102));
////        piePlot.setSectionPaint("SamSung Grand", new Color(102, 255, 102));
////        piePlot.setSectionPaint("MotoG", new Color(255, 102, 153));
////        piePlot.setSectionPaint("Nokia Lumia", new Color(0, 204, 204));
//        piePlot.setBackgroundPaint(Color.white);
//
//        //create chartPanel to display chart(graph)
//        ChartPanel barChartPanel = new ChartPanel(piechart);
//        panel_piechart.removeAll();
//        panel_piechart.add(barChartPanel, BorderLayout.CENTER);
//        panel_piechart.validate();
//    }
    public void showBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            Connection con = ConnectionProvider.getCon();
            String sql = "SELECT count(issue_book_id) as book_num, MONTH(issue_date) as month_code, year(issue_date)as year_code \n"
                    + "FROM library_ms.issue_book_details group by MONTH(issue_date), year(issue_date)";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int bookNum = rs.getInt("book_num");
                int monthCode = rs.getInt("month_code");
                int year = rs.getInt("year_code");
                String month = getMonthName(monthCode);

                dataset.setValue(bookNum, "Book Count", month);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart("contribution", "monthly", "book count", dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        //categoryPlot.setRangeGridlinePaint(Color.BLUE);
        categoryPlot.setBackgroundPaint(Color.WHITE);
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        Color clr3 = new Color(204, 0, 51);
        renderer.setSeriesPaint(0, clr3);

        ChartPanel barpChartPanel = new ChartPanel(chart);
        panel_piechart.removeAll();
        panel_piechart.add(barpChartPanel, BorderLayout.CENTER);
        panel_piechart.validate();

    }

    private String getMonthName(int monthCode) {
        switch (monthCode) {
            case 1:
                return "January";

            case 2:
                return "February";

            case 3:
                return "March";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "August";

            case 9:
                return "September";

            case 10:
                return "October";

            case 11:
                return "November";

            case 12:
                return "December";

            default:
                return "";

        }

    }

    public final void getImage() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT book_name,image from book_details ORDER BY book_added_date DESC LIMIT 6");

            int i = 1;
            while (rs.next()) {
                switch (i) {
                    case 1:
                        showBookDetail(book_name1, image1, rs.getString("book_name"), rs.getBytes("image"));
                        break;
                    case 2:
                        showBookDetail(book_name2, image2, rs.getString("book_name"), rs.getBytes("image"));
                        break;
                    case 3:
                        showBookDetail(book_name3, image3, rs.getString("book_name"), rs.getBytes("image"));
                        break;
                    case 4:
                        showBookDetail(book_name4, image4, rs.getString("book_name"), rs.getBytes("image"));
                        break;
                    case 5:
                        showBookDetail(book_name5, image5, rs.getString("book_name"), rs.getBytes("image"));
                        break;
                    case 6:
                        showBookDetail(book_name6, image6, rs.getString("book_name"), rs.getBytes("image"));
                        break;

                    default:
                        break;
                }

                i++;

            }

        } catch (SQLException e) {

        }

    }

    public void showBookDetail(JLabel nameLabel, JLabel imgLabel, String bookName, byte[] img) {
        nameLabel.setText(bookName);
        ImageIcon image = new ImageIcon(img);
        Image im = image.getImage();
        Image myimage = im.getScaledInstance(210, 260, Image.SCALE_SMOOTH);
        ImageIcon newImage = new ImageIcon(myimage);
        imgLabel.setIcon(newImage);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        student_image = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        student_count = new javax.swing.JLabel();
        kGradientPanel6 = new com.k33ptoo.components.KGradientPanel();
        book_image = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        book_count = new javax.swing.JLabel();
        kGradientPanel7 = new com.k33ptoo.components.KGradientPanel();
        issue_book_image = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        issued_book_count = new javax.swing.JLabel();
        kGradientPanel8 = new com.k33ptoo.components.KGradientPanel();
        pending_book_image = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pending_book_count = new javax.swing.JLabel();
        panel_piechart = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        image6 = new javax.swing.JLabel();
        image2 = new javax.swing.JLabel();
        image1 = new javax.swing.JLabel();
        image3 = new javax.swing.JLabel();
        image4 = new javax.swing.JLabel();
        image5 = new javax.swing.JLabel();
        book_name1 = new javax.swing.JLabel();
        book_name2 = new javax.swing.JLabel();
        book_name3 = new javax.swing.JLabel();
        book_name4 = new javax.swing.JLabel();
        book_name5 = new javax.swing.JLabel();
        book_name6 = new javax.swing.JLabel();
        kGradientPanel3 = new com.k33ptoo.components.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Script MT Bold", 1, 23)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Exit_26px_2.png"))); // NOI18N
        jLabel6.setText("  LogOut");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 880, 140, 40));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 25)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Features");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 180, 40));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        jLabel15.setText("  View Pending Books");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel15MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 700, -1, -1));

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        jPanel10.setPreferredSize(new java.awt.Dimension(350, 70));
        jPanel10.setRequestFocusEnabled(false);

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Book_26px.png"))); // NOI18N
        jLabel16.setText("  Manage Books");
        jLabel16.setPreferredSize(new java.awt.Dimension(199, 39));
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel16MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel16MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        jPanel12.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Sell_26px.png"))); // NOI18N
        jLabel11.setText("  Issue Books");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, -1, -1));

        jPanel17.setBackground(new java.awt.Color(51, 51, 51));
        jPanel17.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Read_Online_26px.png"))); // NOI18N
        jLabel21.setText("  Manage Students");
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel21MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel21MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, -1, -1));

        jPanel19.setBackground(new java.awt.Color(51, 51, 51));
        jPanel19.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_View_Details_26px.png"))); // NOI18N
        jLabel23.setText("  View All Records");
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel23MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel23MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, -1, -1));

        jPanel20.setBackground(new java.awt.Color(51, 51, 51));
        jPanel20.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel33.setBackground(new java.awt.Color(255, 255, 255));
        jLabel33.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel33.setText(" Renew Book");
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel33MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel33MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, -1, -1));

        jPanel18.setBackground(new java.awt.Color(51, 51, 51));
        jPanel18.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Return_Purchase_26px.png"))); // NOI18N
        jLabel22.setText("  Return Book");
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel22MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel22MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, -1, -1));

        jPanel21.setBackground(new java.awt.Color(51, 51, 51));
        jPanel21.setPreferredSize(new java.awt.Dimension(350, 70));

        jLabel34.setBackground(new java.awt.Color(255, 255, 255));
        jLabel34.setFont(new java.awt.Font("Sitka Text", 1, 22)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel34.setText("  View Issued Books");
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel34MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel34MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 620, -1, -1));

        jPanel4.setPreferredSize(new java.awt.Dimension(330, 5));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 51, 51));
        jPanel5.setPreferredSize(new java.awt.Dimension(340, 60));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Script MT Bold", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/library.png"))); // NOI18N
        jLabel10.setText("  LMS Dashboard");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 290, 80));

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 310, 80));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 350, 1030));

        kGradientPanel1.setkBorderRadius(2);
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel1.setkStartColor(new java.awt.Color(255, 153, 153));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel2.setkBorderRadius(50);
        kGradientPanel2.setkEndColor(new java.awt.Color(241, 92, 59));
        kGradientPanel2.setkStartColor(new java.awt.Color(153, 153, 255));
        kGradientPanel2.setPreferredSize(new java.awt.Dimension(300, 148));

        student_image.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        student_image.setForeground(new java.awt.Color(102, 102, 102));
        student_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_People_50px.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Sitka Text", 1, 26)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("No of Students");

        student_count.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        student_count.setForeground(new java.awt.Color(102, 102, 102));
        student_count.setText("1");

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(student_image)
                        .addGap(60, 60, 60)
                        .addComponent(student_count))
                    .addComponent(jLabel5))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(student_count)
                    .addComponent(student_image))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        kGradientPanel1.add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, -1, -1));

        kGradientPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel6.setkBorderRadius(50);
        kGradientPanel6.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel6.setkStartColor(new java.awt.Color(153, 153, 255));
        kGradientPanel6.setPreferredSize(new java.awt.Dimension(300, 148));

        book_image.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        book_image.setForeground(new java.awt.Color(102, 102, 102));
        book_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Book_Shelf_50px.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Sitka Text", 1, 26)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("No of Books");

        book_count.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        book_count.setForeground(new java.awt.Color(102, 102, 102));
        book_count.setText("1");

        javax.swing.GroupLayout kGradientPanel6Layout = new javax.swing.GroupLayout(kGradientPanel6);
        kGradientPanel6.setLayout(kGradientPanel6Layout);
        kGradientPanel6Layout.setHorizontalGroup(
            kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel6Layout.createSequentialGroup()
                .addGroup(kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel6Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel2))
                    .addGroup(kGradientPanel6Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(book_image)
                        .addGap(51, 51, 51)
                        .addComponent(book_count)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        kGradientPanel6Layout.setVerticalGroup(
            kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(book_image)
                    .addComponent(book_count))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        kGradientPanel1.add(kGradientPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));

        kGradientPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel7.setkBorderRadius(50);
        kGradientPanel7.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel7.setkStartColor(new java.awt.Color(153, 153, 255));
        kGradientPanel7.setPreferredSize(new java.awt.Dimension(300, 148));

        issue_book_image.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        issue_book_image.setForeground(new java.awt.Color(102, 102, 102));
        issue_book_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Sell_50px.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Sitka Text", 1, 26)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Issued Books");

        issued_book_count.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        issued_book_count.setForeground(new java.awt.Color(102, 102, 102));
        issued_book_count.setText("1");

        javax.swing.GroupLayout kGradientPanel7Layout = new javax.swing.GroupLayout(kGradientPanel7);
        kGradientPanel7.setLayout(kGradientPanel7Layout);
        kGradientPanel7Layout.setHorizontalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel7Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel7Layout.createSequentialGroup()
                        .addComponent(issue_book_image)
                        .addGap(60, 60, 60)
                        .addComponent(issued_book_count))
                    .addComponent(jLabel8))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        kGradientPanel7Layout.setVerticalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(issue_book_image)
                    .addComponent(issued_book_count))
                .addGap(27, 27, 27))
        );

        kGradientPanel1.add(kGradientPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, -1, -1));

        kGradientPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel8.setkBorderRadius(50);
        kGradientPanel8.setkEndColor(new java.awt.Color(241, 92, 59));
        kGradientPanel8.setkStartColor(new java.awt.Color(153, 153, 255));
        kGradientPanel8.setPreferredSize(new java.awt.Dimension(300, 148));

        pending_book_image.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        pending_book_image.setForeground(new java.awt.Color(102, 102, 102));
        pending_book_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_List_of_Thumbnails_50px.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Sitka Text", 1, 26)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Pending Books");

        pending_book_count.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
        pending_book_count.setForeground(new java.awt.Color(102, 102, 102));
        pending_book_count.setText("1");

        javax.swing.GroupLayout kGradientPanel8Layout = new javax.swing.GroupLayout(kGradientPanel8);
        kGradientPanel8.setLayout(kGradientPanel8Layout);
        kGradientPanel8Layout.setHorizontalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel8Layout.createSequentialGroup()
                .addGroup(kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel8Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel9))
                    .addGroup(kGradientPanel8Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(pending_book_image)
                        .addGap(60, 60, 60)
                        .addComponent(pending_book_count)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        kGradientPanel8Layout.setVerticalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pending_book_image, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pending_book_count, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(23, 23, 23))
        );

        kGradientPanel1.add(kGradientPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 60, -1, -1));

        panel_piechart.setLayout(new java.awt.BorderLayout());
        kGradientPanel1.add(panel_piechart, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 330, 600, 600));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("RECENT BOOK`S");
        kGradientPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, -1, -1));

        image6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        kGradientPanel1.add(image6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 680, 210, 260));

        image2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        kGradientPanel1.add(image2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, 210, 260));

        image1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        kGradientPanel1.add(image1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 210, 260));

        image3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        kGradientPanel1.add(image3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 330, 210, 260));

        image4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        kGradientPanel1.add(image4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 680, 210, 260));

        image5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        kGradientPanel1.add(image5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, 210, 260));

        book_name1.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        book_name1.setForeground(new java.awt.Color(102, 102, 102));
        book_name1.setText("jLabel1");
        kGradientPanel1.add(book_name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 620, 200, -1));

        book_name2.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        book_name2.setForeground(new java.awt.Color(102, 102, 102));
        book_name2.setText("jLabel1");
        kGradientPanel1.add(book_name2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 620, 210, -1));

        book_name3.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        book_name3.setForeground(new java.awt.Color(102, 102, 102));
        book_name3.setText("jLabel1");
        kGradientPanel1.add(book_name3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 620, 210, -1));

        book_name4.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        book_name4.setForeground(new java.awt.Color(102, 102, 102));
        book_name4.setText("jLabel1");
        kGradientPanel1.add(book_name4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 980, 210, -1));

        book_name5.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        book_name5.setForeground(new java.awt.Color(102, 102, 102));
        book_name5.setText("jLabel1");
        kGradientPanel1.add(book_name5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 980, 220, -1));

        book_name6.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        book_name6.setForeground(new java.awt.Color(102, 102, 102));
        book_name6.setText("jLabel1");
        kGradientPanel1.add(book_name6, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 980, 200, -1));

        getContentPane().add(kGradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 1570, 1030));

        kGradientPanel3.setkBorderRadius(2);
        kGradientPanel3.setkEndColor(new java.awt.Color(0, 102, 102));
        kGradientPanel3.setkStartColor(new java.awt.Color(0, 102, 102));

        jLabel3.setFont(new java.awt.Font("Rockwell", 1, 28)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Library Management System");

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_menu_48px_1.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Script MT Bold", 1, 22)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gif/userWelcome1.png"))); // NOI18N
        jLabel19.setText("  Welcome ");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/minimize.png"))); // NOI18N
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel18)
                .addGap(33, 33, 33)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1061, Short.MAX_VALUE)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(kGradientPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 50));

        setSize(new java.awt.Dimension(1920, 1080));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseExited
        // TODO add your handling code here:
        jPanel17.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel21MouseExited

    private void jLabel21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseEntered
        // TODO add your handling code here:
        jPanel17.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel21MouseEntered

    private void jLabel16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseExited
        // TODO add your handling code here:
        jPanel10.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel16MouseExited

    private void jLabel16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseEntered
        // TODO add your handling code here:
        jPanel10.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel16MouseEntered

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new ManageBooks().setVisible(true);
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to Exit ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        // TODO add your handling code here:
        jPanel12.setBackground(mouseEnterColor);

    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        // TODO add your handling code here:
        jPanel12.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        try {
            // TODO add your handling code here:
            setVisible(false);
            dispose();
            new IssueBook().setVisible(true);
        } catch (ParseException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new ManageStudents().setVisible(true);
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseEntered
        // TODO add your handling code here:
        jPanel18.setBackground(mouseEnterColor);

    }//GEN-LAST:event_jLabel22MouseEntered

    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new ReturnBook().setVisible(true);
    }//GEN-LAST:event_jLabel22MouseClicked

    private void jLabel22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseExited
        // TODO add your handling code here:
        jPanel18.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel22MouseExited

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        // TODO add your handling code here:
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel33MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseEntered
        // TODO add your handling code here:
        jPanel20.setBackground(mouseEnterColor);

    }//GEN-LAST:event_jLabel33MouseEntered

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new RenewBook().setVisible(true);

    }//GEN-LAST:event_jLabel33MouseClicked

    private void jLabel33MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseExited
        // TODO add your handling code here:
        jPanel20.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel33MouseExited

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new ViewAllRecord().setVisible(true);

    }//GEN-LAST:event_jLabel23MouseClicked

    private void jLabel23MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseEntered
        // TODO add your handling code here:
        jPanel19.setBackground(mouseEnterColor);

    }//GEN-LAST:event_jLabel23MouseEntered

    private void jLabel23MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseExited
        // TODO add your handling code here:
        jPanel19.setBackground(mouseExitColor);

    }//GEN-LAST:event_jLabel23MouseExited

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new IssueBookDetails().setVisible(true);

    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel34MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseEntered
        // TODO add your handling code here:
        jPanel21.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel34MouseEntered

    private void jLabel34MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseExited
        // TODO add your handling code here:
        jPanel21.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel34MouseExited

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        // TODO add your handling code here:
        setVisible(false);
        dispose();
        new PendingBooks().setVisible(true);

    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseEntered
        // TODO add your handling code here:
        jPanel9.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel15MouseEntered

    private void jLabel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseExited
        // TODO add your handling code here:
        jPanel9.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel15MouseExited

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to logout?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            setVisible(false);
            dispose();
            new Login().setVisible(true);
        }
    }//GEN-LAST:event_jLabel6MouseClicked

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel book_count;
    private javax.swing.JLabel book_image;
    private javax.swing.JLabel book_name1;
    private javax.swing.JLabel book_name2;
    private javax.swing.JLabel book_name3;
    private javax.swing.JLabel book_name4;
    private javax.swing.JLabel book_name5;
    private javax.swing.JLabel book_name6;
    private javax.swing.JLabel image1;
    private javax.swing.JLabel image2;
    private javax.swing.JLabel image3;
    private javax.swing.JLabel image4;
    private javax.swing.JLabel image5;
    private javax.swing.JLabel image6;
    private javax.swing.JLabel issue_book_image;
    private javax.swing.JLabel issued_book_count;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private com.k33ptoo.components.KGradientPanel kGradientPanel3;
    private com.k33ptoo.components.KGradientPanel kGradientPanel6;
    private com.k33ptoo.components.KGradientPanel kGradientPanel7;
    private com.k33ptoo.components.KGradientPanel kGradientPanel8;
    private javax.swing.JPanel panel_piechart;
    private javax.swing.JLabel pending_book_count;
    private javax.swing.JLabel pending_book_image;
    private javax.swing.JLabel student_count;
    private javax.swing.JLabel student_image;
    // End of variables declaration//GEN-END:variables
}
