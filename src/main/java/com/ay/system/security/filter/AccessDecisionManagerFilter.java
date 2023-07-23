package com.ay.system.security.filter;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * 权限认证处理类，用来验证访问当前url所需要的权限用户是否拥有
 * @author 邓海阳
 * @date: 2023/7/17 11:40
 */
@Service
public class AccessDecisionManagerFilter implements AccessDecisionManager {

    /**
     * 验证是否有权限
     * @param authentication 用户的信息
     * @param object  和SecurityMetadataSourceService.getAttributes的参数一样--FilterInvocation
     * @param configAttributes URL所需要的角色权限列表：String[]，SecurityMetadataSourceService.getAttributes返回的对象
     * @return void
     * @author 邓海阳
     * @date: 2023/7/17 14:58
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null || CollectionUtils.isEmpty(configAttributes)) {
            return;
        }

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while(iterator.hasNext()){
            ConfigAttribute configAttribute = iterator.next();
            //得到当前请求需要的权限
            String attribute = configAttribute.getAttribute();
            //请求需要的权限等于null，表示访问该url不需要权限，故直接放行
            if ("null".equals(attribute)) {
                return;
            }

            //得到当前用户的权限集合
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities){
                //拿到当前用户的权限
                String auth = authority.getAuthority();

                //权限匹配
                if(auth.equals("ROLE_" + attribute)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("您当前没有访问权限！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
