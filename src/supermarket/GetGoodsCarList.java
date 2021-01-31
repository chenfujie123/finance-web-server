package supermarket;

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

public class GetGoodsCarList extends HttpServlet {
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

        Map[] x = new Map[100];

        Integer userid = null;
        String resultData = null;

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

        try {
            sql = "select * from supermarket_goods_car inner join supermarket_goods on goods_id = supermarket_goods.id where user_id = ?";
            c= conn.getConnection();
            Integer i;
            ps = c.prepareStatement(sql);
            ps.setInt(1, userid);
            rs = ps.executeQuery();
            for (i = 0; rs.next(); i++) {
                x[i] = new HashMap();
                x[i].put("key", rs.getInt("id"));
                x[i].put("goods_id", rs.getInt("goods_id"));
                x[i].put("selected", false);
                x[i].put("img", rs.getString("img"));
                x[i].put("name", rs.getString("name"));
                x[i].put("price", rs.getInt("price"));
                x[i].put("count", rs.getInt("goods_num"));
                x[i].put("smallcompute", rs.getInt("price")*rs.getInt("goods_num"));
            }
            resultData = gson.toJson(x);
            c.close();
            ps.close();
            rs.close();
            resp.getWriter().write(resultData);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
