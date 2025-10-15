package com.catalogue.my_spring_boot_project.security.filter;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.user_module.service.impl.CustomUserDetailsServiceImpl;
import com.catalogue.my_spring_boot_project.modules.user_module.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.catalogue.my_spring_boot_project.config.constant.SecurityWhitelist.PUBLIC_URLS;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Log.info(getClass(), "验证身份：jwt验证");
        String path = request.getRequestURI();

        // ✅ 定义白名单路径
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            Log.info(getClass(), "请求路径属于白名单：{}，跳过JWT验证", request.getRequestURI());
            return; // 直接跳过，不执行JWT验证
        }

        String header = request.getHeader("Authorization");
        if (header != null) {
            String emailOrUid = jwtUtil.parseToken(header);
            if (emailOrUid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(emailOrUid);
                if (jwtUtil.validateToken(header, userDetails)) {
                    Log.info(getClass(), "用户 {} 已自动登录", emailOrUid);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else {
                Log.error(getClass(), "token解析无效/用户已登陆", null);
            }
        } else {
            Log.error(getClass(), "token不存在", null);
        }
        Log.info(getClass(), "请求路径：{}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    // ✅ 定义白名单路径
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_URLS) {
            if (path.equals(publicPath)) {
                return true;
            }
        }
        return false;
    }

}
