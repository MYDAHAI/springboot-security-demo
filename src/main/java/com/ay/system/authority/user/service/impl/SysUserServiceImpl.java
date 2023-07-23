package com.ay.system.authority.user.service.impl;

import com.ay.system.authority.user.entity.SysUser;
import com.ay.system.authority.user.dao.SysUserMapper;
import com.ay.system.authority.user.service.SysUserService;
import com.ay.system.common.ServiceData;
import com.ay.system.security.config.SecurityUserDetailsService;
import com.ay.system.security.utils.JwtTokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author DHY
 * @since 2023年07月13日
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public ServiceData add(SysUser sysUser) {
        sysUser.setCreateBy(5187);
        sysUser.setUpdateBy(5187);
        sysUser.setEnableFlag("T");
        return ServiceData.getSuccessResult(save(sysUser));
    }

    @Override
    public SysUser getUserByName(String userName) {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.lambda().eq(SysUser::getUsername, userName);
        return getOne(sysUserQueryWrapper);
    }

    @Override
    public ServiceData login(SysUser sysUser, HttpServletRequest request, UserDetails userDetails) {
        String username = sysUser.getUsername();

//        String code = user.getCode();
//        // 验证码
//        String captcha = (String) request.getSession().getAttribute("captcha");
//        // 判断验证码
//        if ("".equals(code) || !captcha.equalsIgnoreCase(code)) {
//            return ResponseHandle.FAIL(HttpCode.USER_CODE);
//        }
        if (userDetails.isEnabled()) {
            // 更新security登录用户对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //将authenticationToken放入spring security全局中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // 创建一个token
            String token = JwtTokenUtils.createToken(username, "", true);
            Map<String, String> tokenMap = new HashMap<>(16);
            tokenMap.put("token", "Bearer" + token);
            return ServiceData.getSuccessResult(tokenMap);
        }
        return ServiceData.getExceptionResult("用户已禁用");
    }
}
