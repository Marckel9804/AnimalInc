package kr.bit.animalinc.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

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
                requestURI.startsWith("/api/user/check-profile");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }

        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            setAuthenticationContext(accessToken);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        String refreshToken = cookie.getValue();
                        if (jwtUtil.validateToken(refreshToken)) {
                            String newAccessToken = jwtUtil.generateToken(jwtUtil.extractAllClaims(refreshToken), 30);
                            response.setHeader("Authorization", "Bearer " + newAccessToken);
                            setAuthenticationContext(newAccessToken);
                        }
                    }
                }
            }
        }

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
