package com.ay.system.authority.user.controller;


import com.ay.system.authority.user.entity.SysUser;
import com.ay.system.authority.user.service.SysUserService;
import com.ay.system.common.ServiceData;
import com.ay.system.security.config.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author DHY
 * @since 2023年07月13日
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "/get")
    public ServiceData success() {
        return ServiceData.getSuccessResult("获取数据成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/get1")
    public ServiceData getUser() {
        return ServiceData.getSuccessResult("获取数据成功get1");
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping(value = "/get2")
    public ServiceData getUser2() {
        return ServiceData.getSuccessResult("获取数据成功get2");
    }

    @GetMapping(value = "/get3")
    public ServiceData getUser3() {
        return ServiceData.getSuccessResult("获取数据成功get3");
    }

    @GetMapping(value = "/get4")
    public ServiceData getUser4(HttpServletRequest request, HttpServletResponse response) {
        //获取jwt验证过后存入session中的值，用于处理各种业务需要
        HttpSession session = request.getSession();
        System.out.println("----token---" + session.getAttribute("token"));
        System.out.println("----userId---" + session.getAttribute("user-id"));
        return ServiceData.getSuccessResult("获取数据成功get4");
    }

    @GetMapping(value = "/add")
    public ServiceData add(SysUser sysUser) {
        //密码加密
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        return sysUserService.add(sysUser);
    }

    @PostMapping("/login")
    public ServiceData login(@RequestBody SysUser sysUser, HttpServletRequest request) {
        UserDetails userDetails = securityUserDetailsService.loadUserByUsername(sysUser.getUsername());
        if (null == userDetails || !passwordEncoder.matches(sysUser.getPassword(), userDetails.getPassword())) {
            return ServiceData.getExceptionResult("用户名或密码错误");
        }
        return sysUserService.login(sysUser, request, userDetails);
    }



}

