package memory;

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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class deleteOne extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GetUserId getId = new GetUserId();
        Map map = new HashMap();
        Gson gson = new Gson();
        MySqlConnect conn = new MySqlConnect();
        Connection c;
        PreparedStatement ps;
        Integer affectLine;
        String sql;
        String token = req.getHeader("token");
        Integer key = Integer.parseInt(req.getParameter("key"));

        Integer userid = null;
        String resultData = null;
        try {
            userid = getId.getUserId(token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(userid == 0){
            map.put("status", 0);
            resultData = gson.toJson(map);
            resp.getWriter().write(resultData);
        }

        try {
            sql = "update to_do_list set status = 3 where k = ? and user_id = ?";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setInt(1, key);
            ps.setInt(2, userid);
            affectLine = ps.executeUpdate();
            if (affectLine == 1) {
                map.put("status",1);
                resultData = gson.toJson(map);
                c.close();
                ps.close();
            } else {
                map.put("status", 0);
                resultData = gson.toJson(map);
                c.close();
                ps.close();
            }
            resp.getWriter().write(resultData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
