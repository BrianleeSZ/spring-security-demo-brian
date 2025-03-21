package com.example.demo.config;

import com.example.demo.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

// 将主要的安全配置类分成两部分：API请求和表单登录
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1) // API 安全配置优先级高
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
        
        @Autowired
        private PasswordEncoder passwordEncoder;
        
        @Autowired
        private CustomUserDetailsService userDetailsService;
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
        }
        
        // 自定义一个匹配器来识别API请求
        private static final class ApiRequestMatcher implements RequestMatcher {
            @Override
            public boolean matches(HttpServletRequest request) {
                String path = request.getRequestURI();
                return path.startsWith("/api/");
            }
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .requestMatcher(new ApiRequestMatcher()) // 仅应用于API请求
                    .authorizeRequests()
                    .antMatchers("/api/users/register").permitAll() // 注册API允许所有人访问
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic() // 使用HTTP基本认证
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // API请求无状态
        }
    }
    
    @Configuration
    @Order(2) // 表单登录配置优先级低
    public static class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {
        
        @Autowired
        private PasswordEncoder passwordEncoder;
        
        @Autowired
        private CustomUserDetailsService userDetailsService;
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // 使用自定义的UserDetailsService
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
            
            // 同时保留内存中的用户，用于测试
            auth.inMemoryAuthentication()
                    .withUser("user").password(passwordEncoder.encode("password")).roles("USER")
                    .and()
                    .withUser("admin").password(passwordEncoder.encode("password")).roles("ADMIN");
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/", "/login", "/register", "/js/**", "/css/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/perform_login")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error=true")
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
        }
    }
}