package com.ay.system.security.config;

import com.ay.system.security.filter.*;
import com.ay.system.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * https://blog.csdn.net/Oaklkm/article/details/128190531
 * @author 邓海阳
 * @version 1.0
 * @description TODO
 * @CreateTime: 2023-07-13  15:55
 */
@Configuration
@EnableWebSecurity //启用web安全
//prePostEnabled = true：开启@PreAuthorize，@PostAuthorize，@PreFilter ，@PostFilter4个注解
//securedEnabled = true：开启@Secured注解
//jsr250Enabled = true：开启@RolesAllowed，@PermitAll，@DenyAll注解
@EnableGlobalMethodSecurity(prePostEnabled = true)//会拦截注解了@PreAuthrize、@prePostEnabled注解的配置
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    // 自定义身份认证的逻辑
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception { }
//    // 自定义全局安全过滤的逻辑
//    public void configure(WebSecurity web) throws Exception { }
//    // 自定义URL访问权限的逻辑
//    protected void configure(HttpSecurity http) throws Exception { }

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    private SecurityMetadataSourceService securityMetadataSourceService;

    @Autowired
    private AccessDecisionManagerFilter accessDecisionManagerFilter;

    /**
     * 常用的三种存储方式，项目找那个用的最多的为，自定义用户存储
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //1、内存用户配置
//        auth.inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder())
//                .withUser("admin").password(bCryptPasswordEncoder().encode("123456")).authorities("ADMIN")
//                .and()
//                .withUser("test").password(bCryptPasswordEncoder().encode("123456")).authorities("TEST");
        //2、数据库用户配置
//        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
//                .usersByUsernameQuery(
//                        "select username, password, status from Users where username = ?")
//                .authoritiesByUsernameQuery(
//                        "select username, authority from Authority where username = ?");
        //3、自定义用户存储
        auth.userDetailsService(securityUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置路径访问验证权限
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/sysUser/add").permitAll()
                .antMatchers("/sysUser/login", "/sysUser/get4").permitAll()
                //指定权限为ROLE_ADMIN才能访问，这里和方法注解配置效果一样，但是会覆盖注解，注意大小写区分
                .antMatchers("/sysUser/get").hasRole("admin")
                // 所有请求都需要验证
                .anyRequest().authenticated();

        http    //配置登录方面的设置
                //.httpBasic() Basic认证，和表单认证只能选一个
                // 使用表单认证页面
//                .formLogin()
                //配置登录入口，默认为security自带的页面/login
//                .loginProcessingUrl("/login")
                // 登录的用户名和密码参数名称
//                .usernameParameter("username")
//                .passwordParameter("password")
                //登录成功或失败处理
//                .successHandler(new LoginSuccessHandler())
//                .failureHandler(new LoginFailureHandler())
                .logout()
//                .logoutSuccessUrl("/loginPage")
                .logoutUrl("logout")
                //添加 /logout 能够以 GET 请求的配置
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessHandler(new LogoutMySuccessHandler())
                //配置取消session管理, 用Jwt来获取用户状态,否则即使token无效,也会有session信息,依旧判断用户为登录状态
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // post请求要关闭csrf验证,不然访问报错；实际开发中开启，需要前端配合传递其他参数
                .csrf().disable();

        //配置自定义过滤器
        // 注意：使用了`BasicAuthenticationFilter`那么在配置`WebSecurityConfigurerAdapter`时，就不要设置`http.httpBasic()`，不然不会走我们自定义的Filter
        //自定义用户名密码登录接口实现
        //在UsernamePasswordAuthenticationFilter之前添加我们自己实现的用于token认证的过滤器
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                //加入AbstractSecurityInterceptor拦截过滤，实现动态权限控制，直接new AbstractSecurityInterceptorFilter填参，防止重复执行过滤器
                .addFilterBefore(new AbstractSecurityInterceptorFilter(securityMetadataSourceService, accessDecisionManagerFilter), FilterSecurityInterceptor.class);

        //自定义UsernamePasswordAuthenticationFilter用户名密码登录实现
//                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)

        //添加异常处理的handler
        http.exceptionHandling()
                .accessDeniedHandler(new MyAccessDeniedHandler())
                .authenticationEntryPoint(new NoLoginHandler());

        //开启跨域，需要spring配合一起开启跨域配置
        http.cors();

    }

    /**
     * 自定义过滤器，用来替换security的默认过滤器（UsernamePasswordAuthenticationFilter），
     * 实现自定义的login接口，接口路径为了区别默认的/login我们定义为/mylogin
     *
     * @return
     * @throws Exception
     */
    @Bean
    public MyUsernamePasswordAuthenticationFilter loginFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter loginFilter = new MyUsernamePasswordAuthenticationFilter();
        loginFilter.setFilterProcessesUrl("/mylogin");
        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        return loginFilter;
    }

    /**
     * JWT token过滤器
     * @return
     * @throws Exception
     */
    @Bean
    public JwtAuthencationTokenFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthencationTokenFilter jwtAuthenticationFilter = new JwtAuthencationTokenFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    /**
     * 使用security 提供的加密规则（还有其他加密方式）
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
