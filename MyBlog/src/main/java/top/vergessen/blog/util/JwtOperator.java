package top.vergessen.blog.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt权限验证工具类
 * @author Vergessen
 * @date 2020/7/5 10:53.
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("WeakerAccess")
@Component
public class JwtOperator {
    /**
     * 秘钥
     * - 默认MyBlog-Vergessen
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 有效期，单位秒
     * - 默认3天
     */
    @Value("${jwt.expire-time-in-second:259200}")
    private Long expirationTimeInSecond;

    /**
     * 从token中获取claim
     *
     * @param token token
     * @return claim
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(this.secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("token解析错误", e);
            throw new IllegalArgumentException("Token invalided.");
        }
    }

    /**
     * 从token中获取指定的key的值
     * @param token token
     * @param key 指定的键值
     * @return 返回key对应的value不存在则返回null
     */
    public Object getInfoFromToken(String token, String key) {
        Claims claims = this.getClaimsFromToken(token);
        return claims.get(key);
    }

    /**
     * 从token中获取指定的key的值
     * @param token token
     * @param key 指定的键值
     * @param tClass 指定返回类型
     * @param <T> 返回类型
     */
    @SuppressWarnings("unchecked")
    public <T> T getInfoFromToken2(String token, String key, Class<T> tClass) {
        Claims claims = this.getClaimsFromToken(token);
        return claims.get(key,tClass);
    }

    /**
     * 获取token的过期时间
     *
     * @param token token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token)
            .getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 已过期返回true，未过期返回false
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 计算token的过期时间
     *
     * @return 过期时间
     */
    public Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + this.expirationTimeInSecond * 1000);
    }

    /**
     * 为指定用户生成token
     *
     * @param claims 用户信息
     * @return token
     */
    public String generateToken(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = this.getExpirationTime();


        byte[] keyBytes = secret.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(createdTime)
            .setExpiration(expirationTime)
            // 你也可以改用你喜欢的算法
            // 支持的算法详见：https://github.com/jwtk/jjwt#features
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 判断token是否非法
     *
     * @param token token
     * @return 未过期返回true，否则返回false
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public static void main(String[] args) {
        // 1. 初始化
        JwtOperator jwtOperator = new JwtOperator();
        jwtOperator.expirationTimeInSecond = 1209600L;
        jwtOperator.secret = "MyBlog--Vergessen--secret--default";

        // 2.设置用户信息
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("username", "Vergessen");
        userMap.put("password", "Vergessen");

        // 测试1: 生成token
        String token = jwtOperator.generateToken(userMap);
        System.out.println(token);

        // 测试2: 如果能token合法且未过期，返回true
        Boolean validateToken = jwtOperator.validateToken(token);
        System.out.println(validateToken);

        // 测试3: 获取用户信息
        Claims claims = jwtOperator.getClaimsFromToken(token);
        System.out.println(claims);
    }
}
