package admin;

import connection.Db;
import other_classes.T;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static connection.DbConnection.close;
import static connection.DbConnection.connect;import javafx.stage.Stage;
;

public class Admin {

    public static void login(Stage stage, String email, String password) {

        ResultSet rs = null;
        Connection con = connect();
        PreparedStatement ps = null;
        try {

            rs = Db.fetch(con, ps, "admin", new String[]{}, "email = ? AND password = ? ", new String[]{email, password});
            //System.out.println("Data from Login Class\nEmail: " + email + " and Password " + password + "\n\n");
            if (rs.next()) {
                // user has logged in weel
                stage.close();
                new AdminInfo(email).start();
                return;
            }
            T.show("Admin Login Error", "Either your email or password is incorrect", 0);

        } catch (SQLException e) {
            T.show("Db Error", e + "", 0);
        } finally {
            close(rs);
            close(ps);
            close(con);
        }

    }

    public static void addAdmin(String firstName, String secondName, String email, String password) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = connect();

        try {
            rs = Db.fetch(con, ps, "admin", new String[]{}, "email = ? ", new String[]{email});
            if (rs.next()) {
                T.show("Error", "This user already exists in the system!", 0);
                return;
            }

            Db.insert("admin",
                    new String[]{"firstName", "secondName", "email", "password"},
                    new String[]{firstName, secondName, email, password}
            );
            T.show("Success", "new admin has been added", 1);

        } catch (SQLException e) {
            T.show("Database Error", e + "", 0);
        } finally {
            close(rs);
            close(ps);
            close(con);
        }
    }

    public static String get(String email, String col) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String data;
        try {
            rs = Db.fetch(con, ps, "admin", new String[]{col}, "email = ? ", new String[]{email});
            data = rs.getString(1);
        } catch (SQLException e) {
            data = null;
            T.show("Database Error", e + "", 0);
        } finally {
            close(con, ps, rs);
        }

        return data;
    }

    public static void changePassword(String email, String oldPassword, String newPassword) {
        if (!get(email, "password").contentEquals(oldPassword)) {
            T.show("Error", "The old password provided does not match any of the system's records", 0);
            return;
        }

        if (newPassword.contentEquals(oldPassword)) {
            T.show("Error", "System detects your old password is the same as the new password. NO NEED TO CHANGE!\nProcess terminated", 0);
            return;
        }

        Db.update("admin", new String[]{"password"}, new String[]{newPassword}, "email = ? ", new String[]{email});
        T.show("Success!", "Password has been changed. Make sure you momorize your new password", 1);
    }

    public static void edit(Stage stage, String mainEmail, String firstName, String secondName, String email) {
        // first we need to check if ths email already exists in the system with another user
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String token = get(mainEmail, "token");
        try {
            rs = Db.fetch(con, ps, "admin", new String[]{}, "email = ? and token != ? ", new String[]{email, token});
            if (rs.next()) {
                // there is actally an existing email that this user has provied
                T.show("Error", "The email you have provided already exists in the system", 0);
                return;
            }

            close(con, ps, rs);
            Db.update("admin",
                    new String[]{"firstName", "secondName", "email"},
                    new String[]{firstName, secondName, email},
                    "email = ? ", new String[]{mainEmail}
            );

            T.show("Success", "You have edited your profile", 1);
            stage.close();
            new AdminInfo(mainEmail).start();
            //dialog.setVisible(false);
            //new AdminInfo(email).dispose();
            //dialog.dispose();
            //new AdminInfo( email).setVisible(true);
        } catch (SQLException e) {
            T.show("Error", e + "", 0);
        } finally {
            close(con, ps, rs);

        }
    }

    // dashboard counts
    public static int getDepartimentCount() {
        return Db.count("departiments", "departiment", "", new String[]{});
    }

    public static int getCoursesCount(String depName) {
        if (depName == null) {
            return Db.count("courses", "course", "", new String[]{});
        }
        return Db.count("courses", "course", "departiment = ?", new String[]{depName});
    }

    public static int getInstructorsCount(String depName) {
        if (depName == null) {
            return Db.count("instructors", "firstName", "", new String[]{});
        }
        return Db.count("instructors", "firstName", "departiment = ?", new String[]{depName});
    }

    public static int getStudentsCount(String depName) {
        if (depName == null) {
            return Db.count("students", "firstName", "", new String[]{});
        }
        return Db.count("students", "firstName", "departiment = ?", new String[]{depName});
    }

    public static int getAdminsCount() {
        return Db.count("admin", "firstName", "", new String[]{}); 
    }

}
