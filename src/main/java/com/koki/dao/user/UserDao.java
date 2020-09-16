package com.koki.dao.user;

import com.koki.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {
    //获取当前用户
    public User getLoginUser(Connection conn,String userCode,String password) throws SQLException;
    //修改用户密码
    public int updateUserPWD(Connection conn,int userId,String password) throws SQLException;

}
