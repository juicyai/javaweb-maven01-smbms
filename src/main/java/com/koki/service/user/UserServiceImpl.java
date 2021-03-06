package com.koki.service.user;

import com.koki.dao.BaseDao;
import com.koki.dao.user.UserDao;
import com.koki.dao.user.UserDaoImpl;
import com.koki.pojo.Role;
import com.koki.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    @Override
    public int getUserCount(String userName, int userRole) {
        Connection conn=null;
        conn = BaseDao.getConnection();
        int count=0;
        try {
            count = userDao.getUserCount(conn, userName, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null); //关闭conn
        }
        System.out.println("service:count:"+count);
        return count;

    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection conn=BaseDao.getConnection();
        List<User> userList=null;
        try {
            userList = userDao.getUserList(conn, userName, userRole, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return userList;
    }

    @Override
    public List<Role> getRoleList() {
        Connection conn=BaseDao.getConnection();
        List<Role> roleList=null;
        try {
            roleList = userDao.getRoleList(conn);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return roleList;
    }

    @Override
    public boolean addUser(User user) {

        Connection conn=BaseDao.getConnection();
        boolean flag=false;

        try {
            UserDao userDao = new UserDaoImpl();
            conn.setAutoCommit(false);//开启JDBC事务管理
            int rows = userDao.addUser(conn, user);
            if(rows>0){
                flag=true;
                System.out.println("add user success");
            }else {
                System.out.println("add user fail");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(conn, null, null);
        }
        return flag;

    }

    @Override
    public boolean modifyUser() {
        return false;
    }

    @Override
    public boolean delUser(int userId) {
        Connection conn=BaseDao.getConnection();
        boolean flag=false;
        try {
            conn.setAutoCommit(false);
            UserService userService = new UserServiceImpl();
            if(userService.delUser(userId)){
                flag=true;
                System.out.println("成功删除用户");
            }else {
                System.out.println("删除用户失败");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return false;
    }

    @Test
    public void test() {
        UserService userService = new UserServiceImpl();
        List<Role> roleList = userService.getRoleList();
    }
}
