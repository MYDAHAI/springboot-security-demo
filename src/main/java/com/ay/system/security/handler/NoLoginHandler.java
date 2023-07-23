package com.ay.system.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.ay.system.common.ServiceData;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 没有登录处理
 * @author 邓海阳
 * @date: 2023/7/14 10:31
 */
@Component
public class NoLoginHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSONObject.toJSONString(ServiceData.getExceptionResult("请先登录")));
    }
}
