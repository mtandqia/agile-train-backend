package com.agile.train.util;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:43
 */
public class TokenUtils {
    //设置过期时间
    private static final long EXPIRE_DATE = (long) 7 * 24 * 60 * 60 * 1000;

    private static final SecretKey key =
            new SecretKeySpec(
                    "iesplanbackendZCfasfhuaUUHufansjXNaldxzxncZKWLSJFkdkfhkawhejkhkfdbshdbf823e33u2020BQWE".getBytes(),
                    SignatureAlgorithm.HS256.getJcaName());

    public static String token(String username) {
        return Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DATE)).setAudience(username)
//                .signWith(SignatureAlgorithm.HS256, key)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public static String getUsernameFromToken(String token) throws MalformedJwtException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claimsJws.getBody().getAudience();
    }

    public static Boolean isTokenExpired(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        final Date expiration = claimsJws.getBody().getExpiration();
        return expiration.before(new Date());
    }

    private TokenUtils(){}
}
