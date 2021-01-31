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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GetAllToDo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GetUserId getId = new GetUserId();
        Map map = new HashMap();
        Gson gson = new Gson();
        MySqlConnect conn = new MySqlConnect();
        Connection c;
        PreparedStatement ps;
        ResultSet rs;
        String sql;
        String token = req.getHeader("token");

        Integer userid = null;
        Map[] x = new Map[100];
        String resultData;
        try {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            userid = getId.getUserId(token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (userid == 0) {
            x[0] = new HashMap();
            x[0].put("status", 0);
            resultData = gson.toJson(x);
            resp.getWriter().write(resultData);
        }

        try {
            sql = "select * from to_do_list where user_id = ?";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setInt(1, userid);
            rs = ps.executeQuery();
            Integer i;
            x[0] = new HashMap();
            x[0].put("status", 1);
            for (i = 1; rs.next(); i++) {
                x[i] = new HashMap();
                x[i].put("key", rs.getInt("k"));
                x[i].put("select", false);
                x[i].put("todo", rs.getString("todo"));
                x[i].put("time", rs.getString("date"));
                x[i].put("status", rs.getInt("status"));
            }
            c.close();
            ps.close();
            rs.close();
            resultData = gson.toJson(x);
            resp.getWriter().write(resultData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
