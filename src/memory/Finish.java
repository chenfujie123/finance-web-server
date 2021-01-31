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

public class Finish extends HttpServlet {
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
        String[] x = req.getParameterValues("key")[0].split(",");
        System.out.println(x);
        Integer userid = null;
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
        if(userid == 0){
            map.put("status", 0);
            resultData = gson.toJson(map);
            resp.getWriter().write(resultData);
        }

        try {
            Integer i;
            Integer key;
            Integer affectLine;
            sql = "update to_do_list set status = ? where k = ?";
            for (i = 0; i < x.length; i++) {
                key = Integer.parseInt(x[i]);
                c = conn.getConnection();
                ps = c.prepareStatement(sql);
                ps.setInt(1, 2);
                ps.setInt(2, key);
                affectLine = ps.executeUpdate();
                if (affectLine != 1) {
                    map.put("status", 0);
                    resultData = gson.toJson(map);
                    resp.getWriter().write(resultData);
                }
            }
            map.put("status", 1);
            resultData = gson.toJson(map);
            resp.getWriter().write(resultData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
