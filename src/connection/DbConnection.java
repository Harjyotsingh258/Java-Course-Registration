package connection;

import other_classes.T;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//private Conn
public class DbConnection {

    private static Connection conn;

    public static Connection connect() {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            //Messages.show("Nice", "Worked nice", 1);
        } catch (ClassNotFoundException | SQLException e) {
            T.show("Db Error", e.toString(), 0);
        }

        return conn;
    }

    // method overloading, four methods with name close
    public static void close(Connection con) {
        // this closes entire database connection
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // now we can attempt to close transactions in the connections also for database memory purposes
    // it also prevents you from your databae bing locked
    public static void close(ResultSet rs) {
        //close ResultSet transactions 
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                T.show("Db Error - Closing Rs", e + "", 0);
            }
        }
    }

    public static void close(PreparedStatement ps) {
        //close ResultSet transactions 
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                T.show("Db Error - Closing Rs", e + "", 0);
            }
        }
    }

    public static void close(Connection con, PreparedStatement ps, ResultSet rs) {
        //close ResultSet transactions 
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                T.show("Db Error - Closing Rs", e + "", 0);
            }
        }

        // close the prepared statement
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                T.show("Db Error - Closing Rs", e + "", 0);
            }
        }

        // this closes entire database connection
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                T.show("Db Error - Closing Rs", e + "", 0);
            }
        }
    }

}
