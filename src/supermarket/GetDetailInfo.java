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

public class GetDetailInfo extends HttpServlet {
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
        Integer goodsId =  Integer.parseInt(req.getParameter("id"));
        String[] x = new String[10];

        try {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            sql = "select * from supermarket_goods_func where goods_id = ?";
            Integer i;
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setInt(1, goodsId);
            rs = ps.executeQuery();
            for (i = 0; rs.next(); i++) {
                x[i] = rs.getString("func");
            }
            resultJson = gson.toJson(x);
            resp.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
