package kr.bit.animalinc.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class LoginSuccess implements AuthenticationSuccessHandler {

    //사용자 정보를 json형식으로 반환하려고함
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //로그인에 성공했을때 사용자 정보 다 가져옴
        Users users = (Users) authentication.getPrincipal();
        Map<String, Object> claims = users.getClaims();
        //
        String accessToken= JWTUtil.generateToken(claims,10);
        String refreshToken=JWTUtil.generateToken(claims,60*24);

        claims.put("accessToken",accessToken);
        claims.put("refreshToken",refreshToken);

        Gson gson=new Gson();
        String str=gson.toJson(claims);  //map->json문자열로 변환

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter= response.getWriter();
        printWriter.println(str);
        printWriter.close();

    }
}