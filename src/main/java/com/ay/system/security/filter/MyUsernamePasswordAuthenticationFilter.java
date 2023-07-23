package com.ay.system.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义用户名密码登录实现
 * @author 邓海阳
 * @date: 2023/7/13 18:51
 */

@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * 获取页面输入的用户名和密码，交给安全认证管理器AuthenticationManager
     * @param request
     * @param response
     * @return Authentication
     * @author 邓海阳
     * @date: 2023/7/14 10:08
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        System.out.println("进入MyUsernamePasswordAuthenticationFilter...");
        if(request.getContentType().equals("application/json")){
            try {
                //参数转换为map
                Map map = new ObjectMapper().readValue(request.getInputStream(),Map.class);
                String username = (String) map.get("username");
                String password = (String) map.get("password");
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.attemptAuthentication(request, response);
    }
}
