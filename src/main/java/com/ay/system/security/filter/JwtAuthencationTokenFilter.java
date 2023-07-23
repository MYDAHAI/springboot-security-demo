package com.ay.system.security.filter;

import com.ay.system.security.config.SecurityUserDetailsService;
import com.ay.system.security.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * jwt token认证过滤器
 * @author 邓海阳
 * @date: 2023/7/14 15:07
 */
public class JwtAuthencationTokenFilter extends BasicAuthenticationFilter {
    private String tokenHeader = "gy_token";
    private String tokenHead = "Bearer";

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

    public JwtAuthencationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 自定义过滤器，用来校验token是否存在，token是否失效
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 请求头中获取token信息
        String authheader = request.getHeader(tokenHeader);
        // 存在token
        if (null != authheader && authheader.startsWith(tokenHead)) {
            // 去除字段名称, 获取真正token
            String authToken = authheader.substring(tokenHead.length());
            // 利用token获取用户名
            String username = JwtTokenUtils.getUsername(authToken);

            System.out.println("自定义JWT过滤器获得用户名为" + username);
            Authentication a = SecurityContextHolder.getContext().getAuthentication();

            // token存在用户未登陆
            // SecurityContextHolder.getContext().getAuthentication() 获取上下文对象中认证信息
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                // 自定义数据源获取用户信息
                UserDetails userDetails = securityUserDetailsService.loadUserByUsername(username);
                // 验证token是否有效 验证token用户名和存储的用户名是否一致以及是否在有效期内, 重新设置用户对象
//                if (JwtTokenUtils.isExpiration(authToken)) {
                // 重新将用户信息封装到UsernamePasswordAuthenticationToken
                if (JwtTokenUtils.checkToken(authToken)) {
                    System.out.println("token有效");
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    // 数据放入session
                    HttpSession session = request.getSession();
                    session.setAttribute("token", authToken);
//                    session.setAttribute("user-id", userId);
                }
            }
        }
        //继续下一个拦截器
        filterChain.doFilter(request, response);
    }
}
