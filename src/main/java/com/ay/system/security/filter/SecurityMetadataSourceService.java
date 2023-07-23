package com.ay.system.security.filter;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 动态获取url所需要的访问权限
 * @author 邓海阳
 * @date: 2023/7/17 11:27
 */
@Service
public class SecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource {

    /** 要放行的url */
    private static final List<String> EXU = new ArrayList<String>(){{
        add("/sysUser/login");
        add("/sysUser/get4");
    }};

    /**
     * 根据url到数据库中查出访问该url所需的权限
     * @param object
     * @return Collection<ConfigAttribute>
     * @author 邓海阳
     * @date: 2023/7/17 11:26
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();
        //请求类型GET ...
        String method = filterInvocation.getRequest().getMethod();
        //去掉？加后面的参数
        String urlPlus = url.split("\\?")[0];

        //去数据库中查对应url的权限
        //得到该请求需要的访问权限
        String role = "list";
        if("".equals(role) || role == null || EXU.contains(url)) {
            role = "null";
        }
        Collection<ConfigAttribute> co=new ArrayList<>();
        co.add(new SecurityConfig(role));
        return co;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
