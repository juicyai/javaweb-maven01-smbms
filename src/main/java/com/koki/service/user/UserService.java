package com.koki.service.user;

import com.koki.pojo.User;

import java.sql.SQLException;

//业务层，判断
public interface UserService {
    public User login(String userCode,String password);
    public boolean updateUserPWD(int userId,String password);
}
