package connect;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
public class Login extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name;
        String sql;
        String passwd;
        String token;
        String resultJson;
        PreparedStatement ps = null;
        Connection c = null;
        Map map = new HashMap();
        Gson gson = new Gson();
        try {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        name = req.getParameter("name");
        passwd = Md5Encrypt.md5(req.getParameter("passwd"));
        token = Md5Encrypt.md5(name+req.getParameter("passwd"));
        try {
            sql = "insert into user(name, tpassword, token) values(?,?,?)";
            MySqlConnect con =  new MySqlConnect();
            c = con.getConnection();
            ps = c.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, passwd);
            ps.setString(3, token);
            Integer result = ps.executeUpdate();
            if (result == 1) {
                map.put("status", 1);
                map.put("token_for_finance", token);
                resultJson = gson.toJson(map);
                resp.getWriter().write(resultJson);
                System.out.println(resultJson);
            }else {
                map.put("status", 0);
                resultJson = gson.toJson(map);
                resp.getWriter().write(resultJson);
                System.out.println(resultJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
