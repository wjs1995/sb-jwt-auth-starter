package cloud.xiaoweiyun.auth;

import cloud.xiaoweiyun.auth.pojo.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {

    private String secret = "wangjinsheng";
    private long expiration = 86400000; // 1天

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public UserEntity parseToken(String jwt) {
        try {
            // 解析 JWT
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
            // 提取信息
//            String username = claims.get("username", String.class);
            String username = claims.getSubject();
            // 你可以提取其他信息
            UserEntity userEntity = new UserEntity(username,"",new ArrayList<>());
            System.out.println(userEntity);
            return userEntity;

        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing JWT: " + e.getMessage());
        }
        return null;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
