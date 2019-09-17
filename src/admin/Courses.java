package admin;

import connection.Db;
import static connection.DbConnection.close;
import static connection.DbConnection.connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import other_classes.T;
import students.AddDropCourses;

public class Courses {
    
    //students
    public static void add(Stage stage, String studentId, String course) {
        if (Db.exists("student_courses", new String[]{}, "studentId = ? AND course = ? ", new String[]{studentId, course})) {
            T.show("Error", "You already have this course in the system");
            return;
        }

        Db.insert("student_courses", new String[]{"course", "studentId"}, new String[]{course, studentId});

        if (Db.exists("student_courses_with_departiments", new String[]{}, "course = ? AND studentId = ? ", new String[]{course, studentId})) {
            return;
        }

        Db.insert("student_courses_with_departiments", new String[]{"course", "studentId", "departiment"}, new String[]{course, studentId, Db.get("courses", "departiment", "course = ? ", new String[]{course})});

        T.show("You have registed for this course, you can drop it any time");
        stage.close();
        new AddDropCourses(studentId).start();
    }

    public static void drop(Stage stage, String studentId, String course) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sure to drop the course (" + course + ")?"
                , ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(result)) {
                Db.delete("student_courses", "studentId = ? AND course = ? ", new String[]{studentId, course});
                stage.close();
                new AddDropCourses(studentId).start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }
    }
    
    //admin

    public static void add(Stage stage, String email, String course, String departiment) {
        if (Db.exists("courses", new String[]{}, "course = ? ", new String[]{course})) {
            T.show("Error", "This course already exist int the system", 0);
            return;
        }

        Db.insert("courses", new String[]{"course", "departiment"}, new String[]{course, departiment});
        T.show("Success", "Data has been inserted!", 1);
        stage.close();
        new CoursesUi(email).start();
    }

    public static void edit(Stage stage, String email, String oldCourse, String newCourse, String departiment) {

        if (Db.exists("courses", new String[]{}, "course = ? ", new String[]{newCourse})) {
            T.show("Error", "This course already exists in the system. Each course must be unique. Kindly modify the name", 0);
            return;
        }

        if (newCourse.contentEquals("")) {
            // update the departiment
            Db.update("courses", new String[]{"departiment"}, new String[]{departiment}, "course = ? ", new String[]{oldCourse});
        } else if (departiment.contentEquals("Select Departiment")) {
            // update the course
            Db.update("courses", new String[]{"course"}, new String[]{newCourse}, "course = ? ", new String[]{oldCourse});
        } else {

            Db.update("courses", new String[]{"course", "departiment"}, new String[]{newCourse, departiment}, "course = ? ", new String[]{oldCourse});
        }
        T.show("Success!", "Course has been modified", 1);
        stage.close();
        new CoursesUi(email).start();

    }

    public static void delete(Stage stage, String email, String course) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sre you want to delete "
                + course + "?\nAlso instructors who had been assigned to this course "
                        + "will also be removed from the course\nStudents who were doing this "
                        + "course will be forced to add a new course, if they had only this course"
            // delete the data 
                , ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(result)) {
                Db.delete("courses", "course = ?", new String[]{course});
                // remove the students assigned to this course
                // also remove instructors assigned to this course
                T.show("Success", "Course has been removed from the system", 1);
                stage.close();
                new CoursesUi(email).start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }
    }

    public static List<String> loadSelectableCourses() {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> courses = new ArrayList<>();

        try {
            rs = Db.fetch(con, ps, "courses", new String[]{"course"}, "", new String[]{});
            //rs.first();

            if (rs.next()) {
                do {
                    courses.add(rs.getString(1));

                } while (rs.next());
            }

        } catch (SQLException e) {
            T.show("Database Error", e + "");
        } finally {
            close(con, ps, rs);
        }
        return courses;
    }

    static void assignCourse(Stage stage,String theEmail, String course) {
        if (Db.exists("instructor_courses", new String[]{}, "instructor = ? AND course = ? ", new String[]{theEmail, course})) {
            T.show("Error", "You already have this course in the system");
            return;
        }

        Db.insert("instructor_courses", new String[]{"course", "instructor"}, new String[]{course, theEmail});

        if (Db.exists("instructor_courses_with_departiments", new String[]{}, "course = ? AND instructor = ? ", new String[]{course, theEmail})) {
            return;
        }

        Db.insert("instructor_courses_with_departiments", new String[]{"course", "instructor", "departiment"}, new String[]{course, theEmail, Db.get("courses", "departiment", "course = ? ", new String[]{course})});

        T.show("You have registed for this course, you can drop it any time");

        stage.close();
        new AssignCourse(theEmail).start();

    }
}
