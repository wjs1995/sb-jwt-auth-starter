package cloud.xiaoweiyun.auth.filters;

import cloud.xiaoweiyun.auth.JwtUtil;
import cloud.xiaoweiyun.auth.pojo.UserEntity;
import cloud.xiaoweiyun.auth.vo.JwtResponse;
import cloud.xiaoweiyun.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // JWT工具类

    @Autowired
    private CustomUserDetailsService customUserDetailsService;  // 用户服务

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 检查Authorization头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // 提取JWT
            try {
                username = jwtUtil.extractUsername(jwt);  // 从JWT中提取用户名
            } catch (ExpiredJwtException e) {
                // 处理过期的JWT
                ok(response,"Token过期了");
                return;
            } catch (IllegalArgumentException e) {
                ok(response,"Token解析异常");

                return;
            } catch (SignatureException e) {
                ok(response,"Token不被信任");
                return;
            }
        }

        // 如果JWT存在且未在上下文中进行身份验证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userDetails = null;
            try {
                userDetails = this.customUserDetailsService.loadUserByJwt(jwt);  // 加载用户详情
            } catch (UsernameNotFoundException e) {
                ok(response,"用户名称不能为null");
            }
            // 验证JWT
            if (userDetails != null && jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // 设置Authentication到上下文中
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);  // 继续执行过滤链
    }

    public void ok(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(JwtResponse.message(msg,403));
    }
}
