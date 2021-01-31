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

public class addOne extends HttpServlet {
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

        Integer userid = null;
        String resultData;
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
            sql = "insert into to_do_list(user_id, todo, date, status) values(?, ?, ?, ?)";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setInt(1, userid);
            ps.setString(2, req.getParameter("todo"));
            ps.setString(3, req.getParameter("time"));
            ps.setInt(4, 1);
            affectLine = ps.executeUpdate();
            c.close();
            ps.close();
            if (affectLine == 1) {
                map.put("status", 1);
                resultData = gson.toJson(map);
                resp.getWriter().write(resultData);
            } else {
                map.put("status", 0);
                resultData = gson.toJson(map);
                resp.getWriter().write(resultData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
