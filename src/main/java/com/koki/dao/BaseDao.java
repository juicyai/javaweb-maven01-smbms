package com.koki.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的基类
public class BaseDao {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    //静态代码初始化静态变量
    static {
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            driver = p.getProperty("driver");
            url=p.getProperty("url");
            user=p.getProperty("user");
            password=p.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取连接conn
    public static Connection getConnection(){
        Connection conn=null;
        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    //查询公共类
    public static ResultSet excute(Connection conn,PreparedStatement ps,ResultSet rs,
                                   String sql,Object[] params) throws SQLException {
        //预编译
        ps=conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1,params[i]);
        }
        rs=ps.executeQuery();
        return rs;
    }

    //增删改公关类
    public static int excute(Connection conn,PreparedStatement ps,
                             String sql,Object[] params) throws SQLException {
        //预编译
        ps=conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1,params[i]);
        }
        int rows=ps.executeUpdate();
        return rows;
    }

    //关闭资源
    public static boolean closeResource(Connection conn,PreparedStatement ps,ResultSet rs){
        boolean flag=true;
        if(conn!=null){
            try {
                conn.close();
                //GC垃圾回收
                conn=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        if(ps!=null){
            try {
                ps.close();
                //GC垃圾回收
                ps=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        if(rs!=null){
            try {
                rs.close();
                //GC垃圾回收
                rs=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        return flag;

    }


}
