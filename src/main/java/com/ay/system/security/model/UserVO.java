package com.ay.system.security.model;

import com.ay.system.authority.user.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author 邓海阳
 * @version 1.0
 * @description TODO
 * @CreateTime: 2023-07-13  16:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO extends SysUser {

    /**
     * 角色权限集
     */
    private List<GrantedAuthority> authorities;

}
