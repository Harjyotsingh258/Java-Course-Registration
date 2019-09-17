package students;

import admin.AdminInfo;
import connection.Db;
import connection.DbConnection;
import static connection.DbConnection.close;
import static connection.DbConnection.connect;
import other_classes.T;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


public class Student {

    public static void login(Stage window, String studentId, String password) {
        //ResultSet rs = Db.fetch("students", new String[]{"count(*)"}, "student_id = ? AND password = ? ", new String[]{studentId, password}, null, null, null);
        //Messages.show("JuST a dialog", studentId + "\n" + password, 1);
        Connection con = DbConnection.connect();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement("SELECT * FROM students WHERE student_id = ? AND password = ? ");
            ps.setString(1, studentId);
            ps.setString(2, password);

            rs = ps.executeQuery();

            //rs = Db.fetch("students", new String[]{}, "student_id = ? AND password = ? ", new String[]{studentId, password});
            if (rs.next()) {
                window.close();
                new StudentHomePage(studentId).start();
                return;
            }
            T.show("Login Error", "student id or password is not correct! Create a new Account by clicking menu on TOP LEFT", 0);
        } catch (SQLException e) {
            T.show("Db Error", e + "", 0);
        } finally {
            DbConnection.close(rs);
            close(ps);
            close(con);
        }
    }

    public static void register(Stage stage, String firstName, String secondName, String lastName, String email, String dep, String course, String password) {
        String studentIdData = (new Date()).getTime() + ""; // we are generating a new student id
        String studentId = studentIdData.substring(8, 13);
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = DbConnection.connect();

        try {
            ps = con.prepareStatement("SELECT * FROM students WHERE email = ? ");
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                T.show("Reg Error", "This user already exists in the system", 0);
            } else {
                Db.insert("students",
                        new String[]{"firstName", "SecondName", "email", "password", "lastName", "student_id", "departiment", "course", "regDate"},
                        new String[]{firstName, secondName, email, password, lastName, studentId, dep, course, getDate()}
                );

//                T.show("Succeeded", "Your account has been created! Proceed to login in the menu bar", 1);
                stage.close();
                new RegistrationCompletion(studentId).start();
            }
        } catch (SQLException e) {
            T.show("Database Error on Registration", e.toString(), 0);
            DbConnection.close(rs);
        } finally {
            close(rs);
            close(ps);
            close(con);
        }
    }

    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(calendar.getTime());
    }

    public static void edit(Stage stage, String studentId, String fn, String sn, String ls, String email) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            rs = Db.fetch(con, ps, "students", new String[]{}, "email = ? AND student_id != ? ", new String[]{email, studentId});
            if (rs.next()) {
                T.show("Error", "Someone else has this email, your account was not edited");
                return;
            }

            Db.update("students",
                    new String[]{"firstName", "secondName", "email", "lastName"},
                    new String[]{fn, sn, email, ls}, "student_id = ? ", new String[]{studentId});
            T.show("Data has been updated");
            stage.close();
            new StudentProfile(studentId).start();
        } catch (SQLException e) {
            T.show("", e.toString());
        } finally {
            close(con, ps, rs);
        }
    }

    public static void changePassword(String studentId, String op, String np) {
        String oldpasswod = Db.get("students", "password", "student_id = ?", new String[]{studentId});
        if (!oldpasswod.contentEquals(op)) {
            T.show("Passwod Change Error", "Your old password is incorrect!");
            return;
        }

        if (np.contentEquals(op)) {
            T.show("Passwod Change Error", "Your new password cannot be the same as the old password. If that the case, then no need to change!");
            return;
        }

        Db.update("students", new String[]{"password"}, new String[]{np}, "student_id = ? ", new String[]{studentId});
        T.show("Password has been changed");
    }

    public static String get(String studentId, String field) {

        return Db.get("students", field, "student_id = ? ", new String[] {studentId});
    }
    
    public static void delete(Stage stage, String id,String adminEmail) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sure to delete this account?",
                ButtonType.YES, ButtonType.NO);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

        if (ButtonType.YES.equals(result)) {
            Db.delete("students", "student_id = ?", new String[]{id});
            // remove the students assigned to this course
            // also remove instructors assigned to this course
            T.show("Success", "Student has been removed from the system", 1);
            stage.close();
            new AdminInfo(adminEmail).start();
        } else if (ButtonType.NO.equals(result)) {
            alert.close();
        }
    }

}
