package com.ay.system.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.ay.system.common.ServiceData;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录成功处理
 * @author 邓海阳
 * @date: 2023/7/14 10:42
 */
@Component
public class LogoutMySuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSONObject.toJSONString(ServiceData.getSuccessResult("退出登录成功")));
    }
}
