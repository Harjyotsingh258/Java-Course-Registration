package instructor;

import connection.Db;
import javafx.stage.Stage;
import other_classes.T;

public class Instructor {

    public static final String TABLE = "instructors";

    public static void login(Stage stage, String email, String password) {
        if (Db.exists(TABLE, new String[]{}, "email = ? AND password = ? ", new String[]{email, password})) {
            stage.close();
            new InstructorHomepage(email).start();
            return;
        }
        T.show("Error", "Either your email or password is incorrect");

    }

    public static void register(Stage stage, String fn, String sn, String email, String password, String dep) {
        if (Db.exists(TABLE, new String[]{}, "email = ? ", new String[]{email})) {
            T.show("Error", "email provided already exists in the system");
            return;
        }

        Db.insert(TABLE,
                new String[]{"firstName", "secondName", "email", "password", "departiment"},
                new String[]{fn, sn, email, password, dep});
        T.show("You have registered into the system");
        stage.close();
        new InstructorLogin().start();
    }

    public static void edit(Stage stage, String mainEmail, String fn, String sn, String email) {
        Db.update(TABLE, new String[]{"firstName", "secondName", "email"}, new String[]{fn, sn, email}, "email = ? ", new String[]{mainEmail});
        T.show("Profile information has been edited");
        stage.close();
        new EditPersonalInfo(mainEmail).start();
    }

    public static void changePassword(String mainEmail, String op, String np) {
        if (!op.contentEquals(get(mainEmail, "password"))) {
            T.show("Error", "Your old password is incorrect");
            return;
        }

        if (op.contentEquals(np)) {
            T.show("Error", "Your new passwod cannot be the same as the old password.");
            return;
        }

        Db.update(TABLE, new String[]{"password"}, new String[]{np}, "email = ? ", new String[]{mainEmail});
        T.show("Password has been changed");
    }

    public static String get(String mainEmail, String field) {
        return Db.get(TABLE, field, "email = ? ", new String[]{mainEmail});
    }
}
