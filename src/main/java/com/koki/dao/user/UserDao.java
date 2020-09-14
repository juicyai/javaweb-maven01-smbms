package com.koki.dao.user;

import com.koki.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {
    public User getLoginUser(Connection conn,String userCode) throws SQLException;

}
