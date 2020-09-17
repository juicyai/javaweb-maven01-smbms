package com.koki.dao.user;

import com.koki.pojo.Role;
import com.koki.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //获取当前用户
    public User getLoginUser(Connection conn,String userCode,String password) throws SQLException;
    //修改用户密码
    public int updateUserPWD(Connection conn,int userId,String password) throws SQLException;
    //根据用户名或者用户角色查询用户总数
    public int getUserCount(Connection conn,String userName,int userRole) throws SQLException;
    //获取用户列表（分页）
    public List<User> getUserList(Connection conn, String userName, int userRole,int currentPageNo,int pageSize) throws SQLException;
    //获取角色列表
    public List<Role> getRoleList(Connection conn) throws SQLException;

}
