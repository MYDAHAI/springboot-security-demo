package com.ay.system.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 动态权限过滤器，用于实现基于路径的动态权限过滤
 * @author 邓海阳
 * @date: 2023/7/17 11:55
 */
@Slf4j
@Component
public class AbstractSecurityInterceptorFilter extends AbstractSecurityInterceptor implements Filter {

    private SecurityMetadataSourceService securityMetadataSourceService;

    public AbstractSecurityInterceptorFilter(SecurityMetadataSourceService securityMetadataSourceService, AccessDecisionManagerFilter accessDecisionManagerFilter){
        this.securityMetadataSourceService = securityMetadataSourceService;
        //添加验证权限的过滤器类
        super.setAccessDecisionManager(accessDecisionManagerFilter);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);

        //此处会调用AccessDecisionManager中的decide方法进行鉴权操作
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        //该接口的实现类用于查出当前请求需要的访问权限
        return this.securityMetadataSourceService;
    }

}

