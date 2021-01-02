package connect;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class IsLogin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String token = req.getParameter("token_for_finance");
        String sql;
        String userName;
        String responseJson;
        MySqlConnect conn = new MySqlConnect();
        Connection c;
        PreparedStatement ps;
        ResultSet rs;
        Map map = new HashMap();
        Gson gson = new Gson();
        try {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            sql = "select name from user where token = ?";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next() == false) {
                map.put("status", 0);
                responseJson = gson.toJson(map);
                resp.getWriter().write(responseJson);
            } else {
                userName = rs.getString(1);
                map.put("status", 1);
                map.put("userName", userName);
                responseJson = gson.toJson(map);
                resp.getWriter().write(responseJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
