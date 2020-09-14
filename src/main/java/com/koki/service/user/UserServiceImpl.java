package com.koki.service.user;

import com.koki.dao.BaseDao;
import com.koki.dao.user.UserDao;
import com.koki.dao.user.UserDaoImpl;
import com.koki.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{
    //业务层调用Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String password) {
        Connection conn=null;
        User user=null;
        conn = BaseDao.getConnection();
        try {
            //调用userDao获取数据
            user = userDao.getLoginUser(conn, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return user;
    }
    @Test
    public void test(){
        User user = new UserServiceImpl().login("admin", "1234567");
        System.out.println(user.getUserPassword());

    }
}
