package supermarket;

import com.google.gson.Gson;
import connect.MySqlConnect;

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

public class GoodsList extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map map = new HashMap();
        Gson gson = new Gson();
        MySqlConnect conn =  new MySqlConnect();
        Connection c;
        PreparedStatement ps;
        ResultSet rs;
        String sql;
        String resultJson;
        Map[] x = new Map[15];

        try {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            Integer i;
            sql = "select * from supermarket_goods";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            for (i = 0; rs.next(); i++) {
                x[i] = new HashMap();
                x[i].put("id", rs.getInt("id"));
                x[i].put("type", rs.getInt("type"));
                x[i].put("name", rs.getString("name"));
                x[i].put("price", rs.getInt("price"));
                x[i].put("img_url", rs.getString("img"));
            }
            resultJson = gson.toJson(x);
            resp.getWriter().write(resultJson);
            rs.close();
            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
