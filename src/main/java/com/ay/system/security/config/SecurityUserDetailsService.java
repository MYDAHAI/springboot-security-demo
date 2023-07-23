package com.ay.system.security.config;

import com.ay.system.authority.user.entity.SysUser;
import com.ay.system.authority.user.service.SysUserService;
import com.ay.system.security.model.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据用户名查找用户信息, 返回认证实体给security安全管理器与页面输入的账号信息对比
     * @param userName
     * @return 实现loadUserByUsername方法，根据用户名查找用户信息, 返回认证实体与页面输入的账号信息对比
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUser userByName = sysUserService.getUserByName(userName);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userByName, userVO);

        //要从数据库中查对应的用户角色所拥有的权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + "admin"));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + "list"));

        userVO.setAuthorities(authorities);
        return new SecurityUserDetails(userVO);
    }
}
