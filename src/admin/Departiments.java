package admin;

import connection.Db;
import static connection.DbConnection.connect;
import static connection.DbConnection.close;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import other_classes.T;
import students.AddDropCourses;

public class Departiments {

    public static void add(Stage stage, String email, String departiment) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            rs = Db.fetch(con, ps, "departiments", new String[]{}, "departiment = ? ", new String[]{departiment});
            if (rs.next()) {
                T.show("Error", "This data already exits, please add a new data", 0);
                return;
            }
            Db.insert("departiments", new String[]{"departiment", "depId"}, new String[]{departiment, (new Date()).getTime() + ""});
            T.show("Success", "Data has been added", 1);
            // update the table
//            stage.close();
//            new DepartimenstUi(email).setVisible(true);
        } catch (SQLException e) {
            T.show("Db Error", e.toString(), 0);
        } finally {
            close(con, ps, rs);
        }

    }

    public static ResultSet loadDepartiments() {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            rs = Db.fetch(con, ps, "departiments", new String[]{}, "", new String[]{});
        } catch (SQLException e) {
            T.show("Database Error", e + "", 0);
        } finally {
            close(con);
            close(ps);
        }
        return rs;
    }

    public static String get(String depId, String field) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String data;
        try {
            rs = Db.fetch(con, ps, "departiments", new String[]{field}, "depId = ? ", new String[]{depId});
            data = rs.getString(1);
        } catch (SQLException e) {
            data = null;
            T.show("Database Error", e + "", 0);
        } finally {
            close(con, ps, rs);
        }

        return data;
    }

    public static void delete(Stage stage, String depId, String email) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sure to delete this departiment?"
                , ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(result)) {
                Db.delete("departiments", "depId =  ?", new String[]{depId});
                T.show("Success", "Data has been deleted", 1);
                stage.close();
                new DepartimenstUi(email).start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }
    }

    public static void edit(Stage stage, String depId, String depName, String email) {
        if (get(depId, "departiment").contentEquals(depName)) {
            T.show("Error", "No change detected, please edit the name and press the edit button, else no need to edit", 0);
            return;
        }
        if (Db.exists("departiments", new String[]{}, "departiment = ? ", new String[]{depName})) {
            T.show("Error", "Seems like that departiment already exists int he system, kindly modify the name", 0);
            return;
        }
        Db.update("departiments", new String[]{"departiment"}, new String[]{depName}, "depId = ? ", new String[]{depId});
        T.show("Success", "departiment has been edited", 1);
        stage.close();
//        new DepartimenstUi(email).setVisible(true);
    }

    public static String[] getDepartiments() {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String departiments[];
        String token = "098745UL";

        try {
            rs = Db.fetch(con, ps, "departiments", new String[]{"departiment"}, "", new String[]{});
            int count = 0;
            if (rs.next()) {
                String dep = "";
                while (rs.next()) {
                    count++;
                    if ((count + 1) == Db.count("departiments", "departiment", "", new String[]{})) {
                        dep = rs.getString(1);
                    } else {
                        dep = rs.getString(1) + token;
                    }

                }
                departiments = dep.split(token);
            } else {
                departiments = new String[]{};
            }
        } catch (SQLException e) {
            T.show("Database Error", e + "");
            departiments = new String[]{};
        } finally {
            close(con, ps, rs);
        }

        return departiments;
    }

    public static List<String> getDepartimentsCombobox() {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> departments = new ArrayList<>();        

        try {
            rs = Db.fetch(con, ps, "departiments", new String[]{"departiment"}, "", new String[]{});
            //rs.first();

            if (rs.next()) {
                do {
                    departments.add(rs.getString(1));

                } while (rs.next());
            }

        } catch (SQLException e) {
            T.show("Database Error", e + "");
        } finally {
            close(con, ps, rs);
        }
        
        return departments;

    }

}
