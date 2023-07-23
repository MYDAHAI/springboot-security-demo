package com.ay.system.authority.user.service;

import com.ay.system.authority.user.entity.SysUser;
import com.ay.system.common.ServiceData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author DHY
 * @since 2023年07月13日
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 添加用户
     * @param sysUser
     * @return ServiceData
     * @author 邓海阳
     * @date: 2023/7/13 16:17
     */
    ServiceData add(SysUser sysUser);

    /**
     * 根据用户名查找用户信息
     * @param userName
     * @return SysUser
     * @author 邓海阳
     * @date: 2023/7/13 16:25
     */
    SysUser getUserByName(String userName);

    /**
     * 自定义登录接口
     * @param sysUser
     * @param request
     * @param userDetails
     * @return ServiceData
     * @author 邓海阳
     * @date: 2023/7/14 11:18
     */
    ServiceData login(SysUser sysUser, HttpServletRequest request, UserDetails userDetails);

}
