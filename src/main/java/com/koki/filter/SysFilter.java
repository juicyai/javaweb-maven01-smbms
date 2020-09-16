package com.koki.filter;

import com.koki.pojo.User;
import com.koki.util.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//让jsp下的页面只能在登陆后访问，不能直接访问
public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //如果session中已经有user，说明user已经登录，可以访问jsp下面的页面，若不存在则跳转到error页面
        User user=null;
        user = (User) request.getSession().getAttribute(Constant.USER_SESSION);
        if(user==null){
            response.sendRedirect("/smb/error.jsp");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
