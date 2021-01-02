package connect;

import com.google.gson.Gson;

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

public class ForLogin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name;
        String sql;
        String passwd;
        String token;
        String responseJson;
        PreparedStatement ps = null;
        Connection c = null;
        Map map = new HashMap();
        Gson gson = new Gson();
        ResultSet rs;
        try {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        name = req.getParameter("name");
        passwd = Md5Encrypt.md5(req.getParameter("passwd"));
        try {
            sql = "select token from user where name = ? and tpassword = ?";
            MySqlConnect con =  new MySqlConnect();
            c = con.getConnection();
            ps = c.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, passwd);
            rs = ps.executeQuery();
            if (rs.next() == false) {
                map.put("status", 0);
                responseJson = gson.toJson(map);
                resp.getWriter().write(responseJson);
            } else {
                token = rs.getString(1);
                map.put("status", 1);
                map.put("token_for_finance", token);
                responseJson = gson.toJson(map);
                resp.getWriter().write(responseJson);
                System.out.println(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
