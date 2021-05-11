package cn.mn.mn;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    /**
     * 签名用的密钥
     */
    private final String SIGNING_KEY = "78sebr72umyz33i9876gc31urjgyfhgj";
    private final String CLAIMS_KEY_USERID = "user_id";
    private final String CLAIMS_KEY_APPID = "app_id";

    public String createJWTByUserId(Integer userId, long ttlMillis) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(this.CLAIMS_KEY_USERID, userId);
        return this.createJWT(claims, ttlMillis);
    }

    public String createJWTByAppId(String appId, long ttlMillis) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(this.CLAIMS_KEY_APPID, appId);
        return this.createJWT(claims, ttlMillis);
    }

    public Integer parseJWTByUserId(String token) {
        Integer result = null;
        try {
            //得到DefaultJwtParser
            Claims claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(this.SIGNING_KEY)
                    //设置需要解析的token
                    .parseClaimsJws(token).getBody();
            if (claims != null) {
                result = (Integer) claims.get(this.CLAIMS_KEY_USERID);
            }
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public String parseJWTByAppId(String token) {
        String result = null;
        try {
            //得到DefaultJwtParser
            Claims claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(this.SIGNING_KEY)
                    //设置需要解析的token
                    .parseClaimsJws(token).getBody();
            if (claims != null) {
                result = (String) claims.get(this.CLAIMS_KEY_APPID);
            }
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * 登录成功后生成Jwt
     * 使用Hs256算法
     *
     * @param claims    创建payload的私有声明
     * @param ttlMillis jwt过期时间
     * @return token字符串
     */
    private String createJWT(Map<String, Object> claims, long ttlMillis) {
        //指定签名的时候使用的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //保存在Payload（有效载荷）中的内容
                .setClaims(claims)
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, this.SIGNING_KEY);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }

        return builder.compact();
    }
}
