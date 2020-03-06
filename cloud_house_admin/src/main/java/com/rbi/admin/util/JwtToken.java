package com.rbi.admin.util;

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
    public static String createToken(String userId,String username) throws Exception {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Constants.CALENDARFIELD, Constants.CALENDARINTERVAL);
        Date expiresDate = nowTime.getTime();

        // header Map
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
                .withClaim("user_id", null == userId ? null : userId.toString())
                .withClaim("username",username)
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

    /**
     * 根据Token获取user_id
     *
     * @param token
     * @return user_id
     */
//    public static String getAppUID(String token) {
//        Map<String, Claim> claims = verifyToken(token);
//        Claim userIdClaim = claims.get("user_id");
//        if (null == userIdClaim || StringUtils.isEmpty(userIdClaim.asString())) {
//            // token 校验失败, 抛出Token验证非法异常
//        }
//        return String.valueOf(userIdClaim.asString());
//    }
//    public static void main(String[] args){
//        try {
//            String token = createToken("12","hgx");
//            System.out.println("token ======== " + token);
//            String appUID = getAppUID(token);
//            System.out.println("appUID ======== " + appUID);
//            Map<String, Claim> claimMap = verifyToken(token);
//            System.out.println("claimMap ======== " + claimMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
