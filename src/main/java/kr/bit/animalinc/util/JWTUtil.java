package kr.bit.animalinc.util;

import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

public class JWTUtil {  //1.jwt 생성하고 2.검증

    private static String key="1234567890123456789012345678901234567890";  //jwt서명에 사용할 비밀 키

    //클레임, 만료시간사용해 jwt토큰 생성
    public static String generateToken(Map<String, Object> map, int min){

        SecretKey key=null;

        try{
            key= Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        String str= Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setClaims(map)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))  //토큰발급된 시간설정
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) //토큰의 유효시간 설정
                .signWith(key)
                .compact();  //jwt문자열 생성

        return str;
    }

    //JWT검증
    public static Map<String, Object> validateToken(String token){  //
        Map<String,Object> claim=null;

        try{
            SecretKey key= Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

            //토큰을 파싱해서 검증
            claim=Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)  //토큰이 유효하지 않거나. 서명이 불일치하면 예외발생
                    .getBody();
        }catch (MalformedJwtException malformedJwtException) {
            throw new JwtException("malformedJwtException");
        }catch (InvalidClaimException invalidClaimException){
            throw new JwtException("invalidClaimException");
        }catch (JwtException jwtException){
            throw new JwtException("jwtException");
        } catch (Exception e) {
            throw new JwtException("error");
        }
        return claim;
    }
}
