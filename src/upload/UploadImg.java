package upload;

import com.google.gson.Gson;
import connect.MySqlConnect;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UploadImg extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //文件的存储目录
    private static final String UPLOAD_DIRECTORY = "uploadfile";

    //上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 3; // 3MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 5; // 5MB
    private static MySqlConnect conn;

    //连接数据库



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map map = new HashMap();
        String resultJson;
        Gson gson = new Gson();
        String token = req.getHeader("Token");
        String type = req.getHeader("Type");
        String sql;
        PreparedStatement ps;
        ResultSet rs;
        Connection c;
        Integer userid = null;
        Integer insertrow;
        String respSrc;
        //审核id
        Integer id;
        //判断此用户是否是注册用户


        try {
            sql = "select * from user where token = ?";
            conn = new MySqlConnect();
            c = conn.getConnection();
            ps = c.prepareStatement(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next() == false) {
                map.put("status", 0);
                resultJson = gson.toJson(map);
                resp.getWriter().write(resultJson);
                return;
            } else {
                userid = rs.getInt("id");
            }
            rs.close();
            ps.close();
            c.close();
            System.out.println(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(req)){
            map.put("status", 1);
            resultJson = gson.toJson(map);
            resp.getWriter().write(resultJson);
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置内存临界值，超过后将产生临时文件并存于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        //设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        //设置文件最大上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        //设置最大请求值(包括表单数据和文件数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        //中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = req.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(req);

            if (formItems != null && formItems.size() > 0) {
                for (FileItem item: formItems) {
                    //处理不在表单中的字段
                    if (!item.isFormField()) {
//                        String fileName = new File(item.getName()).getName();
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                        String fileName = sdf.format(date);
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        System.out.println(filePath);
                        item.write(storeFile);
                        respSrc = "http://localhost:8083/" + UPLOAD_DIRECTORY + "/" + fileName;
                        //调用检查应该插入记录还是更新记录
                        id = checkStatus(userid);
                        //判断上传的身份证是正面还是反面
                        if (Integer.parseInt(type) == 1) {
                            if (id == 0) { //插入一条记录
                                sql = "insert into real_name_authentication(id_card_ping, user_id, check_status) values(?, ?, 1)";
                                c = conn.getConnection();
                                ps = c.prepareStatement(sql);
                                ps.setString(1, respSrc);
                                ps.setInt(2, userid);
                                insertrow = ps.executeUpdate();
                                if (insertrow == 1) {
                                    map.put("status", 2);
                                    map.put("img_src", respSrc);
                                    resultJson = gson.toJson(map);
                                    resp.getWriter().write(resultJson);
                                }
                                ps.close();
                                c.close();
                            } else {
                                sql = "update real_name_authentication set id_card_ping = ? where user_id = ?";
                                c = conn.getConnection();
                                ps = c.prepareStatement(sql);
                                ps.setString(1, respSrc);
                                ps.setInt(2, userid);
                                insertrow = ps.executeUpdate();
                                if (insertrow == 1) {
                                    map.put("status", 2);
                                    map.put("img_src", respSrc);
                                    resultJson = gson.toJson(map);
                                    resp.getWriter().write(resultJson);
                                }
                                ps.close();
                                c.close();
                            }
                        } else if(Integer.parseInt(type) == 0) {
                            if (id == 0) { //插入一条记录
                                sql = "insert into real_name_authentication(id_card_ning, user_id, check_status) values(?, ?, 1)";
                                c = conn.getConnection();
                                ps = c.prepareStatement(sql);
                                ps.setString(1, respSrc);
                                ps.setInt(2, userid);
                                insertrow = ps.executeUpdate();
                                if (insertrow == 1) {
                                    map.put("status", 2);
                                    map.put("img_src", respSrc);
                                    resultJson = gson.toJson(map);
                                    resp.getWriter().write(resultJson);
                                }
                                ps.close();
                                c.close();
                            } else {
                                sql = "update real_name_authentication set id_card_ning = ? where user_id = ?";
                                c = conn.getConnection();
                                ps = c.prepareStatement(sql);
                                ps.setString(1, respSrc);
                                ps.setInt(2, userid);
                                insertrow = ps.executeUpdate();
                                if (insertrow == 1) {
                                    map.put("status", 2);
                                    map.put("img_src", respSrc);
                                    resultJson = gson.toJson(map);
                                    resp.getWriter().write(resultJson);
                                }
                                ps.close();
                                c.close();
                            }
                        }


                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer checkStatus(Integer userid) throws SQLException {
        conn = new MySqlConnect();
        Connection c = conn.getConnection();
        String sql;
        PreparedStatement ps;
        ResultSet rs;
        sql = "select * from real_name_authentication where user_id = ?";
        ps = c.prepareStatement(sql);
        ps.setInt(1, userid);
        rs = ps.executeQuery();
        while (rs.next()){
            if (rs.getInt("check_status") == 1 || rs.getInt("check_status") == 4) {
                return rs.getInt("id");
            }
        }
        return 0;
    }
}
