package forall;

import connect.MySqlConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserId {
    public static Integer getUserId(String token) throws SQLException {
        String sql;
        PreparedStatement ps;
        MySqlConnect conn = new MySqlConnect();
        Connection c;
        ResultSet rs;
        sql = "select * from user where token = ?";
        c = conn.getConnection();
        ps = c.prepareStatement(sql);
        ps.setString(1, token);
        rs = ps.executeQuery();
        if (!rs.next()) {
            return 0;
        } else {
            return rs.getInt("id");
        }
    }
}
