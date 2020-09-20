package com.koki.dao.user;

import com.koki.dao.BaseDao;
import com.koki.pojo.Role;
import com.koki.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection conn, String userCode,String password) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
//        conn = BaseDao.getConnection();
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
            BaseDao.closeResource(null,ps,rs);//查询结束后,关闭ps和rs资源
            if(user!=null && !password.equals(user.getUserPassword())){
                user=null;
            }


        }
        return user;
    }

    @Override
    public int updateUserPWD(Connection conn, int userId, String password) throws SQLException {
//        conn=BaseDao.getConnection();
        String sql="UPDATE smbms_user SET userPassword=? WHERE id=?";
        PreparedStatement ps=null;
        int rows=0;
        if(conn!=null){
//           ps=conn.prepareStatement(sql);
           Object[] params={password,userId};
           rows = BaseDao.excute(conn, ps, sql, params);

           BaseDao.closeResource(null,ps,null);
        }
        System.out.println("UserDao:rows:"+rows);
        return rows;
    }

    @Override
    public int getUserCount(Connection conn, String userName, int userRole) throws SQLException {
//        conn=BaseDao.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        int count=0;
        StringBuilder sql = new StringBuilder();
        //连表查询用户数量,username为模糊查询, 进行sql语句的拼接
        sql.append("SELECT COUNT(1) as count FROM smbms_user su,smbms_role sr WHERE su.userRole=sr.id");
        //使用list集合存放params参数
        List<Object> list=new ArrayList<>();
        if(!StringUtils.isNullOrEmpty(userName)){
            sql.append(" AND su.userName LIKE ?");
            list.add("%"+userName+"%");
        }
        if(userRole>0){
            sql.append(" AND su.userRole=?");
            list.add(userRole);
        }
        System.out.println("dao:sql:"+sql.toString());
        System.out.println("dao:list:"+list.toString());
        Object[] params = list.toArray();
        if(conn!=null){
//            ps=conn.prepareStatement(sql.toString());
            rs=BaseDao.excute(conn,ps, rs, sql.toString(), params);
            if(rs.next()){
                count = rs.getInt("count");
            }
            BaseDao.closeResource(null,ps,rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection conn, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<User> userList=new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT su.*,sr.roleName as userRoleName FROM smbms_user su,smbms_role sr WHERE su.userRole=sr.id");
        if(conn!=null){
            List<Object> list=new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" AND su.userName LIKE ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" AND su.userRole=?");
                list.add(userRole);
            }

            if(currentPageNo>0 && pageSize>0){
                sql.append(" ORDER BY su.creationDate DESC limit ?,?");
                currentPageNo=(currentPageNo-1)*pageSize;
                list.add(currentPageNo);
                list.add(pageSize);
            }
            System.out.println("service:list:"+list.toString());
            System.out.println("service:sql:"+sql);

            Object[] params = list.toArray();
            rs=BaseDao.excute(conn, ps, rs, sql.toString(), params);
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
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
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);//将查询出的用户保存至列表
            }
            BaseDao.closeResource(null,ps,rs);
        }
//        if(!userList.isEmpty()){
//            for (User user : userList) {
//                System.out.println(user);
//            }
//        }
        return userList;
    }

    @Override
    public List<Role> getRoleList(Connection conn) throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        String sql="SELECT * FROM smbms_role";
        Object[] params={};
        List<Role> roleList = new ArrayList<>();
        if(conn!=null){
            rs = BaseDao.excute(conn, ps, rs, sql, params);
            while (rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("roleName"));
                role.setRoleCode(rs.getString("roleCode"));
                roleList.add(role);
            }
            BaseDao.closeResource(null,ps,rs);

        }
        for (Role role : roleList) {
            System.out.println("dao:role:"+role);
        }
        return roleList;
    }

    @Override
    public int addUser(Connection conn, User user) throws SQLException {
        PreparedStatement ps=null;
        int rows=0;
        String sql="INSERT INTO smbms_user(userCode,userName,userPassword,gender,birthday," +
                "phone,address,userRole,createdBy,creationDate) VALUES(?,?,?,?,?,?,?,?,?,?)";
        Object[] params={user.getUserCode(),user.getUserName(),user.getUserPassword(),
                user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),
                user.getUserRole(),user.getCreatedBy(),user.getCreationDate()};
        if(conn!=null) {
            ps = conn.prepareStatement(sql);
            rows=BaseDao.excute(conn, ps, sql, params);
            BaseDao.closeResource(null,ps,null);
            }
        return rows;


    }

    @Override
    public int modifyUser(Connection conn, int userId) {
        PreparedStatement ps=null;
        int rows=0;
        sql="UPDATE smbms_user SET "
    }

    @Override
    public int delUser(Connection conn, int userId) throws SQLException {
        PreparedStatement ps=null;
        int rows=0;
        String sql="DELETE FROM smbms_user WHERE id=UserId";
        Object[] params={};
        if(conn!=null){
            conn.prepareStatement(sql);
            rows = BaseDao.excute(conn, ps, sql, params);
            BaseDao.closeResource(null,ps,null);

        }
        return rows;
    }
}
