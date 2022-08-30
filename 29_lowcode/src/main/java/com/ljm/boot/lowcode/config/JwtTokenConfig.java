package com.ljm.boot.lowcode.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Dominick Li
 * @description java web token 配置类
 **/
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "jwt")
@Component
@Slf4j
public class JwtTokenConfig {

    private String secret;
    private long expire;
    private String tokenHead;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }

    private Clock clock = DefaultClock.INSTANCE;

    /**
     * 根据身份ID标识，生成Token
     */
    public String getToken(Integer identityId, String roleName) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                //放入唯一标识,可以是用户名或者Id
                .setSubject(identityId.toString())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                //自定义属性 放入用户拥有请求权限
                .claim("role", roleName)
                .compact();
    }

    /**
     * 根据token获取身份信息
     */
    public Claims getTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("getTokenClaim error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 判断token是否失效
     */
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(clock.now());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 根据token获取username
     */
    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getClaimFromToken(token, Claims::getSubject));
    }

    /**
     * 根据token获取角色
     */
    public String getRoleCodeFromToken(String token) {
        return getTokenClaim(token).get("role").toString().toLowerCase();
    }

    /**
     * 根据token获取失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getTokenClaim(token);
        return claimsResolver.apply(claims);
    }


}
