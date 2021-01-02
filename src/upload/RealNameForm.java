package upload;

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

import com.google.gson.Gson;
import connect.MySqlConnect;
import forall.GetUserId;

public class RealNameForm extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GetUserId getId = new GetUserId();
        Integer userid = null;
        Map map = new HashMap();
        Gson gson = new Gson();
        MySqlConnect conn =  new MySqlConnect();
        Connection c;
        PreparedStatement ps;
        ResultSet rs;
        String sql;
        Integer affectLine;
        //获取参数
        String name = req.getParameter("name");
        String num  = req.getParameter("number");
        try {
            userid = getId.getUserId(req.getHeader("token"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            sql = "update real_name_authentication set actual_name = ?, id_num = ?, check_status = 2 where user_id = ?";
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, num);
            ps.setInt(3, userid);
            affectLine = ps.executeUpdate();
            ps.close();
            c.close();
            if (affectLine == 0) {
                map.put("status",0);
            }else {
                map.put("status", 1);
            }
            String respResult = gson.toJson(map);
            resp.getWriter().write(respResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
