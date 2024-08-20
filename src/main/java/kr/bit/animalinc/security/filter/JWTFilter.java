package kr.bit.animalinc.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.util.JWTUtil;
import kr.bit.animalinc.util.RedisTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final RedisTokenService redisTokenService;

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
                requestURI.startsWith("/api/user/check-nickname") ||
                requestURI.startsWith("/api/user/google-userinfo") ||
                requestURI.startsWith("/api/user/send-password") ||
                requestURI.startsWith("/api/user/check-profile")||
                requestURI.startsWith("/testapi/test1")||
                requestURI.startsWith("/testapi");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(accessToken)) {
                    Long userNum = jwtUtil.extractAllClaims(accessToken).get("userNum", Long.class  );
                    String storedToken = redisTokenService.getAccessToken(userNum);

                    if (storedToken != null && storedToken.equals(accessToken)) {
                        setAuthenticationContext(accessToken);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token is valid");
                        return;
                    }
                } else {
                    throw new ExpiredJwtException(null, null, "Token has expired");
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("세션이 만료되어 자동으로 로그아웃됩니다");
                return;
            }
        }

        // 액세스 토큰이 존재하는 경우
        if (accessToken != null) {
            //토큰이 블랙리스트에 있는가?
            if (redisTokenService.isTokenBlacklisted(accessToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted");
                return;
            }
        }

        if (jwtUtil.validateToken(accessToken)) {
            Long userNum = jwtUtil.extractAllClaims(accessToken).get("userNum", Long.class);

            //Redis에 저장된 액세스 토큰과 일치하는지 확인
            String storedToken = redisTokenService.getAccessToken(userNum);
            if (storedToken != null && storedToken.equals(accessToken)) {
                setAuthenticationContext(accessToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is invalid");
                return;
            }
        } else {
            // 액세스 토큰이 없을 경우 쿠키에서 리프레시 토큰 확인
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        String refreshToken = cookie.getValue();

                        // 리프레시 토큰 유효성 검사
                        if (jwtUtil.validateToken(refreshToken)) {
                            Long userNum = jwtUtil.extractAllClaims(refreshToken).get("userNum", Long.class);

                            // 새로운 액세스 토큰 발급
                            String newAccessToken = jwtUtil.generateToken(jwtUtil.extractAllClaims(refreshToken), 30);

                            //새로운 액세스 토큰을 Redis에 저장
                            redisTokenService.storeAccessToken(newAccessToken, userNum, Duration.ofMinutes(30));

                            // 새로ㅜㅇㄴ 액세스 토큰을 헤더에 추가하고 인증 컨텍스트 설정
                            response.setHeader("Authorization", "Bearer " + newAccessToken);
                            setAuthenticationContext(newAccessToken);
                        }
                    }
                }
            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String token) {
        Map<String, Object> claims = jwtUtil.extractAllClaims(token);
        Integer userNumInt = (Integer) claims.get("userNum");
        Long userNum = userNumInt != null ? userNumInt.longValue() : null;
        String email = (String) claims.get("userEmail");
        String password = (String) claims.get("userPw");
        String nickname = (String) claims.get("userNickname");
        Boolean slogin = (Boolean) claims.get("slogin");
        List<String> roleName = (List<String>) claims.get("roleName");

        UsersDTO usersDTO = new UsersDTO(userNum, email, password, nickname, slogin, roleName);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usersDTO, null, usersDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
