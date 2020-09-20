package com.koki.service.user;

import com.koki.pojo.Role;
import com.koki.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.SQLException;
import java.util.List;

//业务层，判断
public interface UserService {
    //登录
    public User login(String userCode,String password);
    //获取密码
    public boolean updateUserPWD(int userId,String password);
    //获取用户数量
    public int getUserCount(String userName,int userRole);
    //获取用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);
    //获取角色列表
    public List<Role> getRoleList();
    //添加用户
    public boolean addUser(User user);
    //修改用户
    public boolean modifyUser();
    //删除用户
    public boolean delUser(int userId);
}
