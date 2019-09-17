package connection;

import static connection.DbConnection.close;
import static connection.DbConnection.connect;
import other_classes.T;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Db {

    public static void insert(String table, String[] columns, String[] values) {
        // fetch anything from the database
        Connection con = connect();
        PreparedStatement ps = null;

        // insert into table(columns[index]) values(values[index]);
        String sql = "INSERT INTO " + table + "(";

        for (int i = 0; i < columns.length; i++) {
            int count = i + 1;
            if (count == columns.length) {
                sql += columns[i];
            } else {
                sql += columns[i] + ", ";
            }
        }

        sql += ") VALUES(";

        for (int index = 0; index < columns.length; index++) {
            int count = index + 1;
            if (count == columns.length) {
                sql += "?";
            } else {
                sql += "?, ";
            }
        }

        sql += ")";

        try {

            ps = con.prepareStatement(sql);

            int start = 0;
            while (start < values.length) {
                int paramCount = start + 1;
                ps.setString(paramCount, values[start]);
                start++;
            }

            ps.execute();
        } catch (SQLException e) {
            T.show("Db Error", e.toString(), 0);
        } finally {
            close(ps);
            close(con);
        }
    }

    public static void update(String table, String[] columns, String[] values, String whereClause, String[] whereValues) {
        // update table set col1= value1, col2 = value 2 where data = ?
        Connection con = connect();
        PreparedStatement ps = null;

        try {
            if (whereValues.length == 0) {
                String sql = "UPDATE " + table + " SET ";
                for (int i = 0; i < columns.length; i++) {
                    int count = i + 1;
                    if (count == columns.length) {
                        sql += columns[i] + " = ? ";
                    } else {
                        sql += columns[i] + " = ?, ";
                    }
                }

                ps = con.prepareStatement(sql);

                ps.execute();
            } else {

                String sql = "UPDATE " + table + " SET ";
                for (int i = 0; i < columns.length; i++) {
                    int count = i + 1;
                    if (count == columns.length) {
                        sql += columns[i] + " = ? ";
                    } else {
                        sql += columns[i] + " = ?, ";
                    }
                }

                sql += "WHERE " + whereClause;

                ps = con.prepareStatement(sql);

                int totalBindVales = values.length + whereValues.length;

                // update users set a = ?, b = ? where data = ? 
                // Db.update('users', array(), array() );
                for (int i = 0; i < totalBindVales; i++) {
                    int paramCount = i + 1;

                    if (paramCount <= values.length) {
                        ps.setString(paramCount, values[i]);
                    } else {
                        int newKey = i - values.length;
                        ps.setString(paramCount, whereValues[newKey]);
                    }

                }

                ps.execute();

            }

        } catch (SQLException e) {
            T.show("Db Error", e + "", 0);
        } finally {
            close(ps);
            close(con);
        }

    }

    public static void delete(String table, String whereClause, String[] whereValues) {
        // delete from table where whereValues[params]
        Connection con = connect();
        PreparedStatement ps = null;

        String sql = "";
        try {
            if (whereValues.length == 0) {
                // that means you want to delete the whole table
                sql += "DELETE FROM " + table;
                ps = con.prepareStatement(sql);
            } else {
                sql += "DELETE FROM " + table + " WHERE " + whereClause;
                ps = con.prepareStatement(sql);
                for (int i = 0; i < whereValues.length; i++) {
                    int paramCount = i + 1;
                    ps.setString(paramCount, whereValues[i]);
                }
            }

            ps.execute();
        } catch (SQLException e) {
            T.show("Db Error", e.toString(), 0);
        } finally {
            close(ps);
            close(con);
        }
    }

    public static ResultSet fetch(Connection con, PreparedStatement ps, String table, String columns[], String whereClause, String whereValues[]) throws SQLException {

        ResultSet rs;

        if (columns.length == 0) {

            if (whereValues.length == 0) {
                String sql = "SELECT * FROM " + table;
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
            } else {
                String sql = "SELECT * FROM " + table + " WHERE " + whereClause;
                ps = con.prepareStatement(sql);
                for (int i = 0; i < whereValues.length; i++) {
                    int paramCount = i + 1;
                    ps.setString(paramCount, whereValues[i]);
                    //System.out.println("Data from Db.java:\n" + paramCount + ", " + whereValues[i] + "\n\n");
                }
                rs = ps.executeQuery();
            }

        } else {

            if (whereValues.length == 0) {
                String sql = "SELECT ";

                for (int i = 0; i < columns.length; i++) {
                    int paramsCount = i + 1;
                    if (paramsCount == columns.length) {
                        sql += columns[i] + " ";
                    } else {
                        sql += columns[i] + ", ";
                    }
                }

                sql += "FROM " + table;

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

            } else {
                String sql = "SELECT ";

                for (int i = 0; i < columns.length; i++) {
                    int paramsCount = i + 1;
                    if (paramsCount == columns.length) {
                        sql += columns[i] + " ";
                    } else {
                        sql += columns[i] + ", ";
                    }
                }

                sql += "FROM " + table + " WHERE " + whereClause;
                ps = con.prepareStatement(sql);
                int start = 0;
                while (start < whereValues.length) {
                    int paramCount = start + 1;
                    ps.setString(paramCount, whereValues[start]);
                    start++;
                }

                rs = ps.executeQuery();
            }

        }

        return rs;
    }

    public static ResultSet fetch(Connection con, PreparedStatement ps, String table, String columns[], String whereClause, String whereValues[], String groupBy, String orderBy, String limit) throws SQLException {
        ResultSet rs;

        String groupByClause;
        String orderByClause;
        String limitClause;

        if (groupBy == null) {
            groupByClause = "";
        } else {
            groupByClause = "group by " + groupBy;
        }

        if (orderBy == null) {
            orderByClause = "";
        } else {
            orderByClause = "order by " + orderBy;
        }

        if (limit == null) {
            limitClause = "";
        } else {
            limitClause = "LIMIT " + limit;
        }

        String sqlEdning = groupByClause + " " + orderByClause + " " + limitClause;

        if (columns.length == 0) {

            if (whereValues.length == 0) {
                String sql = "SELECT * FROM " + table + " " + sqlEdning;
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
            } else {
                String sql = "SELECT * FROM " + table + " WHERE " + whereClause + " " + sqlEdning;
                ps = con.prepareStatement(sql);
                for (int i = 0; i < whereValues.length; i++) {
                    int paramCount = i + 1;
                    ps.setString(paramCount, whereValues[i]);
                }
                rs = ps.executeQuery();
            }

        } else {

            if (whereValues.length == 0) {
                String sql = "SELECT ";

                for (int i = 0; i < columns.length; i++) {
                    int paramsCount = i + 1;
                    if (paramsCount == columns.length) {
                        sql += columns[i] + " ";
                    } else {
                        sql += columns[i] + ", ";
                    }
                }

                sql += "FROM " + table + " " + sqlEdning;

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

            } else {
                String sql = "SELECT ";

                for (int i = 0; i < columns.length; i++) {
                    int paramsCount = i + 1;
                    if (paramsCount == columns.length) {
                        sql += columns[i] + " ";
                    } else {
                        sql += columns[i] + ", ";
                    }
                }

                sql += "FROM " + table + " WHERE " + whereClause + " " + sqlEdning;
                ps = con.prepareStatement(sql);
                int start = 0;
                while (start < whereValues.length) {
                    int paramCount = start + 1;
                    ps.setString(paramCount, whereValues[start]);
                    start++;
                }

                rs = ps.executeQuery();
            }

        }

        return rs;
    }

    public static int count(ResultSet rs) {
        int size;
        try {
            rs.last();
            size = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {
            size = 0;
            T.show("Db Error", e + "", 0);
        } finally {
            DbConnection.close(rs);
        }

        return size;
    }

    public static int count(String table, String col, String whereClause, String[] whereValues) {
        // we need to code this manually
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        int size;

        if (whereValues.length == 0) {
            // this means we are counting the whole table
            try {
                String sql = "SELECT count(" + col + ") FROM  " + table;
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

                size = rs.getInt(1);
            } catch (SQLException e) {
                size = 0;
                T.show("Database Error", "Error during counting rows when WHERE clause is null (*). Defined Error:\n" + e, 0);
            } finally {
                close(con, ps, rs);
            }
        } else {
            // we are conting table rows based on the where clause
            try {
                String sql = "SELECT count(" + col + ") FROM  " + table + " WHERE " + whereClause;
                ps = con.prepareStatement(sql);
                for (int i = 0; i < whereValues.length; i++) {
                    int paramCount = i + 1;
                    ps.setString(paramCount, whereValues[i]);
                }

                rs = ps.executeQuery();
                size = rs.getInt(1);
            } catch (SQLException e) {
                size = 0;
                T.show("Database Error", "Error during counting rows when WHERE clause is available. Defined Error:\n" + e, 0);
            } finally {
                close(con, ps, rs);
            }
        }
        return size;
    }

    public static Boolean exists(String table, String[] columns, String whereClause, String[] whereValues) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            rs = fetch(con, ps, table, columns, whereClause, whereValues);
            return rs.next();
        } catch (SQLException e) {
            T.show("Db Error, while checking if col exists", e + "", 0);
            return null;
        } finally {
            close(con, ps, rs);
        }
    }

    public static String get(String table, String field, String whereClause, String whereValues[]) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String data;
        try {
            rs = Db.fetch(con, ps, table, new String[]{field}, whereClause, whereValues);
            data = rs.getString(1);
        } catch (SQLException e) {
            data = null;
            T.show("Database Error", e + "", 0);
        } finally {
            close(con, ps, rs);
        }

        return data;

    }

    public static ResultSet loadTable(String table, String columns[], String whereClause, String whereValues[]) {
        Connection con = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            rs = fetch(con, ps, table, columns, whereClause, whereValues);
            
        } catch (SQLException e) {
            T.show("Database Error", e + "", 0);
        } 
        return rs;
    }
}
