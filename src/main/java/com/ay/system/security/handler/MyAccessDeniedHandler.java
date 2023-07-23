package com.ay.system.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 自定义的没权限页面
 * 在SpringSecurity没有权限的页面可以自己定制
 * 通过重写AccessDeniedHandler 这个接口的handle方法，
 * 然后再SpringSecurity配置类中注入此类在configure(HttpSecurity http)方法中添加代码
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setHeader("Content-Type","application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        String dateJson = "{'status':'403','msg':'缺少访问权限'}";
        httpServletResponse.getWriter().write(dateJson);
    }
}
