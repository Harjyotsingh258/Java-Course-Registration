package connection;

import static connection.DbConnection.close;
import static connection.DbConnection.connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import other_classes.T;

public class Courses {

    public static void add(String course, String departiment) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            rs = Db.fetch(con, ps, "courses", new String[]{}, "course = ? AND departiment = ?", new String[]{});
            if (rs.next()) {
                T.show("Db Error", "This data already exists in the system", 0);
                return;
            }
            close(con, ps, rs);
            Db.insert("courses",
                    new String[]{"course", "departiment"},
                    new String[]{course, departiment}
            );
            T.show("Success", "Great, course data has been added", 1);
            // update the table
        } catch (Exception e) {
            T.show("Db Error", e + "", 0);
        } finally {
            close(con, ps, rs);
        }

    }
}
