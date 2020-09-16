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
            user = userDao.getLoginUser(conn, userCode,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return user;
    }

    @Override
    public boolean updateUserPWD(int userId, String password){
        Connection conn=null;
        conn=BaseDao.getConnection();
        int rows=0;
        boolean flag=false;
        try {
            rows = userDao.updateUserPWD(conn, userId, password);
            if(rows>0){
                flag=true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        System.out.println("userService:flag,"+flag);

        return flag;
    }

    @Test
    public void test() {
        UserService userService = new UserServiceImpl();
        boolean b = userService.updateUserPWD(1, "12345678");
        System.out.println(b);
    }
}
