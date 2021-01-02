package upload;

import com.google.gson.Gson;
import connect.MySqlConnect;
import forall.GetUserId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class RealNameStatus extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = null;
        GetUserId getId = new GetUserId();
        Map map = new HashMap();
        Gson gson = new Gson();
        MySqlConnect conn = new MySqlConnect();
        Connection c;
        PreparedStatement ps;
        ResultSet rs;
        String sql;
        String token = req.getHeader("token");
        String resultJson;
        try {
            userId = getId.getUserId(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sql = "select * from real_name_authentication where user_id = ?";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                map.put("status", 1);
            } else if(rs.getInt("check_status") == 1) {
                map.put("status", 1);
            } else if (rs.getInt("check_status") == 2){
                map.put("status", 2);
            } else if (rs.getInt("check_status") == 3){
                map.put("status", 3);
            } else if (rs.getInt("check_status") == 4) {
                map.put("status", 4);
            }
            resultJson = gson.toJson(map);
            resp.getWriter().write(resultJson);
            rs.close();
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
