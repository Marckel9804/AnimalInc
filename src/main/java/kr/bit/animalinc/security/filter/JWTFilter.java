package kr.bit.animalinc.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    //요청마다 한번씩 실행되는 필터
    //특정조건을 만족하는 요청에 대해서 필터링하지 않도록 설정가능

    //로그인 - 받아온 서버요청 - 서버쪽 토큰검증
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        log.info("Request URI: {}", requestURI);

        // 인증이 필요 없는 경로를 명시
        return requestURI.startsWith("/api/user/register") ||
                requestURI.startsWith("/api/user/login") ||
                requestURI.startsWith("/api/user/social-login") ||
                requestURI.startsWith("/api/user/update-profile") ||
                requestURI.startsWith("/api/user/get-profile") ||
                requestURI.startsWith("/api/user/delete") ||
                requestURI.startsWith("/api/user/update-password") ||
                requestURI.startsWith("/api/user/search-user") ||
                requestURI.startsWith("/api/user/logout") ||
                requestURI.startsWith("/api/user/send-verification-code") ||
                requestURI.startsWith("/api/user/verify-email") ||
                requestURI.startsWith("/api/user/check-nickname");
    }

    //jwt토큰 검증 / 인증정보 설정 -> 필터 핵심
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authstr= request.getHeader("Authorization");

        //Bearer ej~~~~~
        try{
            String accessToken=authstr.substring(7); //실제 토큰 추출
            Map<String, Object> claims= JWTUtil.validateToken(accessToken);

            String email=(String) claims.get("userEmail");
            String password=(String) claims.get("userPw");
            String nickname=(String) claims.get("userNickname");
            Boolean slogin=(Boolean) claims.get("slogin");
            List<String> roleName=(List<String>) claims.get("roleName");



            UsersDTO usersDTO = new UsersDTO(email, password, nickname, slogin.booleanValue(), roleName);

            //사용자 정보 기반으로 인증토큰 생성
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(usersDTO, password, usersDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }
        catch (Exception e){

            Gson gson=new Gson();
            String str=gson.toJson(Map.of("error","error_token"));

            response.setContentType("application/json");
            PrintWriter printWriter=response.getWriter();
            printWriter.println(str);
            printWriter.close();
            return;
        }

        filterChain.doFilter(request,response);  //다음 필터로 요청 전달

    }

}