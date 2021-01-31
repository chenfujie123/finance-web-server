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

public class CloseGoodsCar extends HttpServlet {
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
        String[] x = req.getParameterValues("key")[0].split(",");
        System.out.println(x);
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
            Integer i;
            Integer label = 1;
            c = conn.getConnection();
            for (i = 0; i < x.length; i++) {
                sql = "delete from supermarket_goods_car where user_id = ? and goods_id = ?";
                ps = c.prepareStatement(sql);
                ps.setInt(1, userid);
                ps.setInt(2, Integer.parseInt(x[i]));
                affectLine = ps.executeUpdate();
                if (affectLine != 1) {
                    label = 0;
                }
            }
            if (label == 1) {
                map.put("status", 1);
                resultData = gson.toJson(map);
                resp.getWriter().write(resultData);
            } else {
                map.put("status", 0);
                resultData = gson.toJson(map);
                resp.getWriter().write(resultData);
            }


        } catch (Exception e) {

        }
    }
}
