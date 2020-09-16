package com.koki.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.koki.pojo.User;
import com.koki.service.user.UserService;
import com.koki.service.user.UserServiceImpl;
import com.koki.util.Constant;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
}
