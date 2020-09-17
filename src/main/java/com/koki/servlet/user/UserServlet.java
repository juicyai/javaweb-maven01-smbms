package com.koki.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.koki.pojo.Role;
import com.koki.pojo.User;
import com.koki.service.user.UserService;
import com.koki.service.user.UserServiceImpl;
import com.koki.util.Constant;
import com.koki.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//实现servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("starting servlet---");
        String method = req.getParameter("method");
        System.out.println("method:"+method);
        if(method!=null && method.equals("savepwd")){
            this.updatePWD(req,resp);
        }else if(method!=null && method.equals("pwdmodify")){
            System.out.println("pwdmodify starting");
            this.pwdmodify(req,resp);
        }else if(method!=null && method.equals("query")){
            this.query(req,resp);
        }

        System.out.println("ending servlet---");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //修改密码
    public void updatePWD(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String newPassword=req.getParameter("newpassword");
        System.out.println("Servlet:new password:"+newPassword);
        boolean flag=false;
        if(o!=null && newPassword!=null){
            UserService userService= new UserServiceImpl();
            int userId=((User) o).getId();
            System.out.println("userId:"+userId);
            flag=userService.updateUserPWD(userId, newPassword);
            System.out.println("修改结果："+flag);
            if(flag){
                req.setAttribute("message","修改密码成功，请重新登录");
                //移除session
                req.getSession().removeAttribute(Constant.USER_SESSION);
            }else {//修改失败
                req.setAttribute("message","修改密码失败");
            }
        }else{
            req.setAttribute("message","新密码有误");
        }
        req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);

    }
    //Ajax与后端交互，验证旧密码oldpassword
    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword=req.getParameter("oldpassword");
        System.out.println("servlet:oldpassword"+oldpassword);
        //定义map结果集，应用于Ajax
        Map<String,String> resultMap = new HashMap<>();
        if(o==null){//session过期
            resultMap.put("result","errorSession");
            System.out.println("result"+"errorSession");
        }else{
            String pwd=((User)o).getUserPassword();
            System.out.println("servlet:current user pwd:"+pwd);

            if(oldpassword==null){
                resultMap.put("resilt","error");
            }else if(oldpassword.equals(pwd)){
                System.out.println("servlet:"+"result,"+"true");
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.write(JSONArray.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //查询用户列表
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //先从前端获取数据
        String queryUserName = req.getParameter("queryUserName");
        String tmp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole=0;

        //获取用户列表
        UserService userService = new UserServiceImpl();
        int pageSize=5;
        int currentPageNo=1;
        //特殊情况处理
        if(queryUserName==null){
            queryUserName="";
        }
        //处理queryUserRole参数
        if(tmp!=null && !tmp.equals("")){
            queryUserRole=Integer.parseInt(tmp);
        }
        if(pageIndex!=null){
            currentPageNo=Integer.parseInt(pageIndex);
        }
        //获取用户总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        //当前页面
        pageSupport.setCurrentPageCode(currentPageNo);
        //一页的条目数
        pageSupport.setPageSize(pageSize);
        //总条目数
        pageSupport.setTotalDataNumber(totalCount);
        //总页数
        pageSupport.setPageNumber(totalCount/pageSize);
        int totalPages=pageSupport.getPageNumber();

        //控制首页和尾页
        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPages){
            currentPageNo=totalPages;
        }

        //获取用户列表展示
        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        //获取角色列表
        List<Role> roleList = userService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("userList",userList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPages);
        //返回前端
        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
