package com.koki.dao.user;

import com.koki.dao.BaseDao;
import com.koki.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection conn, String userCode,String password) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = BaseDao.getConnection();
        String sql = "SELECT * FROM smbms_user WHERE userCode=?";
        Object[] params = {userCode};

        User user = null;
        if (conn != null) {
            rs = BaseDao.excute(conn, ps, rs, sql, params);

            if (rs.next()) {
                user=new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(userCode);
                user.setUserName(rs.getString("userName"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setAddress(rs.getString("address"));
                user.setBirthday(rs.getDate("birthday"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setGender(rs.getInt("gender"));
                user.setPhone(rs.getString("phone"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setUserRole(rs.getString("userRole"));
            }
            if(user!=null && !password.equals(user.getUserPassword())){
                user=null;
            }

        }
        return user;
    }

    @Override
    public int updateUserPWD(Connection conn, int userId, String password) throws SQLException {
        conn=BaseDao.getConnection();
        String sql="UPDATE smbms_user SET userPassword=? WHERE id=?";
        PreparedStatement ps=null;
        int rows=0;
        if(conn!=null){
           ps=conn.prepareStatement(sql);
           Object[] params={password,userId};
            rows = BaseDao.excute(conn, ps, sql, params);
        }
        System.out.println("UserDao:rows:"+rows);
        return rows;
    }

}
