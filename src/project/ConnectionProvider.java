package project;

import java.sql.*;

public class ConnectionProvider {

    public static Connection getCon() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_ms", "root", "9823449060");
            return con;
        } catch (Exception e) {
            return null;
        }

    }
}
