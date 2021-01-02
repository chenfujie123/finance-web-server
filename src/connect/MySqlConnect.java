package connect;
import java.sql.*;


public class MySqlConnect {
    //连接数据库类
    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver"); System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://49.235.199.101:3306/finance?characterEncoding=UTF-8", "chenfujie", "cfj85965746");
            System.out.println("连接数据成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


}
