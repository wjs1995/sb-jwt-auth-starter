package cloud.xiaoweiyun.auth.service;

import cloud.xiaoweiyun.auth.JwtUtil;
import cloud.xiaoweiyun.auth.pojo.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查找用户
        UserEntity user = null;
        if (user == null) {

            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public UserEntity loadUserByJwt(String jwt) throws UsernameNotFoundException {
        // 从数据库中查找用户
        UserEntity user = jwtUtil.parseToken(jwt);
        System.out.println(user);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
