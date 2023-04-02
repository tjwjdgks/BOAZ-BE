package boaz.site.boazback.common.util;


import boaz.site.boazback.common.domain.ResendEmailToken;
import boaz.site.boazback.common.domain.TokenType;
import boaz.site.boazback.common.exception.JwtException;
import boaz.site.boazback.user.domain.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    private String secretKey = "<YOU JWT TOKEN  SECRET KEY>";


    //retrieve username from jwt token
    // jwt token으로부터 username을 획득한다.
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    // jwt token으로부터 만료일자를 알려준다.
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


//    public Date getExpirationDateFromToken(String token) {
//        try{
//            Claims claims  = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//            return claims.getExpiration();
//        }catch (Exception e){
//            throw JwtException.TOKEN_EXPIRED;
//        }
//    }

    public Map<String,Object> getClaimFromToken(String token,String key) {
        final Claims claims = getAllClaimsFromToken(token);
        return  (LinkedHashMap)claims.get(key);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        try{
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e ){
            throw JwtException.TOKEN_EXPIRED;
        } catch (UnsupportedJwtException e2){
            throw JwtException.TOKEN_UNSUPPORTED;
        } catch (MalformedJwtException e3){
            throw JwtException.TOKEN_MALFORMED;
        } catch (SignatureException e4){
            throw JwtException.TOKEN_SIGNATURE_ERROR;
        }
    }

    //check if the token has expired
    // 토큰이 만료되었는지 확인한다.
    public Boolean isTokenExpired(String token) {
        try{
             Claims claims  = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
             Date expiration = claims.getExpiration();
            if(expiration.before(new Date())){
                return true;
            }
            return false;
        }catch (Exception e){
            return true;
        }
    }




    public Boolean isRefreshTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if(expiration.before(new Date())){
            throw JwtException.TOKEN_EXPIRED;
        }
        return false;
    }


    //generate token for user
    // 유저를 위한 토큰을 발급해준다.
    public String generateToken(User userDetails, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user",userDetails);
        return doGenerateToken(claims, userDetails.getEmail(),tokenType);
    }



    public String generateCertificateToken(TokenType tokenType,String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("certificate","true");
        return doGenerateToken(claims, email, tokenType);
    }


    public String generateResendEmailToken(ResendEmailToken resendEmailToken){
        Map<String, Object> claims = new HashMap<>();
        System.out.println("resendEmailToken = " + resendEmailToken);
        claims.put("certificate",resendEmailToken);
        return doGenerateToken(claims, resendEmailToken.getEmail(),TokenType.CERTIFICATION);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject,TokenType tokenType) {
//        try{
//
//            return Jwts.builder().setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + tokenType.getTime() ))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//        }catch (Exception e){
//           e.printStackTrace();
//            return null;
//        }
        return Jwts.builder().setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenType.getTime() ))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    //validate token
    public Boolean validateToken(String token, User userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

}
