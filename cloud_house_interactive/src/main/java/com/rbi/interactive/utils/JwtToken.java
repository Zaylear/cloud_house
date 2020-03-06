package com.rbi.interactive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * APP登录Token的生成和解析
 *
 */
public class JwtToken {

    private final static Logger logger = LoggerFactory.getLogger(JwtToken.class);

    /**
     * JWT生成Token.<br/>
     *
     * JWT构成: header, payload, signature
     *
     * @param userId
     *            登录成功后用户userId, 参数userId不可传空
     */
    public static String createToken(String userId) throws Exception {
        Date iatDate = new Date();
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Constants.CALENDARFIELD, Constants.CALENDARINTERVAL);
        Date expiresDate = nowTime.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        /**
         * build token
         * param backups {iss:Service, aud:APP}
         *
         * withHeader : header
         * withClaim : payload
         * withIssuedAt : sign time
         * withExpiresAt : expire time
         * sign :signature
         */
        return JWT.create().withHeader(map)
                .withClaim("iss", "Service")
                .withClaim("aud", "APP")
                .withClaim("userId", null == userId ? null : userId.toString())
                .withIssuedAt(iatDate)
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(Constants.SECRET));
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) throws Exception {
        DecodedJWT jwt = null;
        Map<String, Claim> claims = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Constants.SECRET)).build();
            jwt = verifier.verify(token);
            claims = jwt.getClaims();
            Optional.ofNullable(claims).orElseThrow(IllegalArgumentException::new);
        } catch (Exception e) {
            logger.error("【拦截器】非法token，验证失败！");
            throw new Exception();
        }
        return claims;
    }

    public static String getClaim(String token,String key) throws Exception {
        Map<String, Claim> claims = verifyToken(token);
        Claim claim = claims.get(key);
        return String.valueOf(claim.asString());
    }

}
