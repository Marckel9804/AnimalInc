package kr.bit.animalinc.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.service.email.EmailService;
import kr.bit.animalinc.service.user.UserService;
import kr.bit.animalinc.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bit.animalinc.entity.shop.Animal; // 추가된 부분

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin("*")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        if (user.getUserPw() == null || user.getUserPw().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty.");
        }

        if (userService.checkEmailExists(user.getUserEmail())) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        Users registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        log.info("Checking nickname: {}", nickname);
        boolean isAvailable = userService.checkNickname(nickname);
        log.info("Nickname available: {}", isAvailable);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        String registeredMethod = userService.getRegisteredMethod(email);
        if (!"알 수 없음".equals(registeredMethod)) {
            return ResponseEntity.status(400).body("해당 이메일은 이미 " + registeredMethod + "에서 가입된 이메일입니다.");
        }

        log.info("Request to send verification code to {}", email);
        String verificationCode = emailService.sendVerificationEmail(email);
        if (verificationCode == null) {
            return ResponseEntity.status(500).body("Verification code sending failed");
        }
        Map<String, String> response = new HashMap<>();
        response.put("verificationCode", verificationCode);
        return ResponseEntity.ok("Verification code sent successfully.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String verificationCode = request.get("verificationCode");

        log.info("Request to verify email {} with code {}", email, verificationCode);

        boolean isVerified = emailService.verifyEmailCode(email, verificationCode);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isVerified", isVerified);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String userEmail = request.get("userEmail");
        String userPw = request.get("userPw");

        if (userEmail == null || userPw == null) {
            return ResponseEntity.status(400).body("이메일과 비밀번호를 입력해주세요");
        }

        Users user = userService.login(userEmail, userPw);

        if (user == null) {
            return ResponseEntity.status(401).body("이메일이나 비밀번호를 확인해주세요");
        }

        List<String> roles = user.getMemRoleList().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        UsersDTO authenticatedUser = new UsersDTO(user.getUserEmail(), user.getUserRealname(), user.getUserNickname(), user.isSlogin(), roles);
        Map<String, Object> claims = authenticatedUser.getClaims();

        String accessToken = jwtUtil.generateToken(claims, 30);
        String refreshToken = jwtUtil.generateToken(claims, 60 * 24);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(refreshTokenCookie);

        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String email = request.get("email");
        String userRealname = request.get("name");
        String platform = request.get("platform");

        try {
            Users user = userService.socialLogin(userRealname, email, platform);

            List<String> roles = user.getMemRoleList().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());

            UsersDTO authenticatedUser = new UsersDTO(user.getUserEmail(), user.getUserRealname(), user.getUserNickname(), user.isSlogin(), roles);
            Map<String, Object> claims = authenticatedUser.getClaims();

            String accessToken = jwtUtil.generateToken(claims, 30);
            String refreshToken = jwtUtil.generateToken(claims, 60 * 24);

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(refreshTokenCookie);

            response.setHeader("Authorization", "Bearer " + accessToken);

            // 사용자 정보와 토큰을 함께 반환
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", user);
            responseBody.put("accessToken", accessToken);
            responseBody.put("refreshToken", refreshToken);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalStateException e) {
            log.error("Social login error: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred during social login: {}", e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred during social login.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Refresh Token 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS 사용 시 설정
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("Logout successful");
    }

    //구글 로그인 시 해당 사용자 정보 가져오는 메소드
    @GetMapping("/google-userinfo")
    public ResponseEntity<?> getGoogleUserInfo(@RequestParam("access_token") String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;
        try {
            String result = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error fetching Google user info: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching Google user info");
        }
    }

    @GetMapping("/naver-info")
    public ResponseEntity<?> getNaverUserInfo(@RequestParam("access_token") String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching Naver user info");
        }
    }

    //페이지 이동할때마다 리프레시 토큰으로 새로운 accesstoken 발급
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    if (jwtUtil.validateToken(refreshToken)) {
                        Map<String, Object> claims = jwtUtil.extractAllClaims(refreshToken);
                        String newAccessToken = jwtUtil.generateToken(claims, 30);
                        log.info("Access token refreshed successfully for email: {}", claims.get("userEmail"));
                        return ResponseEntity.ok().header("Authorization", "Bearer " + newAccessToken).build();
                    }
                }
            }
        }
        log.warn("Invalid refresh token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    @PostMapping("/check-profile")
    public ResponseEntity<?> completeProfile(@RequestBody Map<String, String> request, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.extractToken(httpServletRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or missing token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail", String.class);
        String birthdate = request.get("birthdate");
        String nickname = request.get("nickname");

        if (email == null || birthdate == null || nickname == null) {
            return ResponseEntity.status(400).body("모든 정보를 입력해주세요");
        }

        boolean nicknameAvailable = userService.checkNickname(nickname);
        if (!nicknameAvailable) {
            return ResponseEntity.status(400).body("Nickname already in use");
        }


        Users user = userService.completeProfile(email, birthdate, nickname);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or missing token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail").toString();
        Users user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        UsersDTO userDTO = new UsersDTO(user.getUserEmail(), user.getUserRealname(), user.getUserNickname(), user.isSlogin(), user.getMemRoleList().stream().map(Enum::name).collect(Collectors.toList()));
        userDTO.setUserPw(user.getUserPw());
        userDTO.setUserBirthdate(user.getUserBirthdate());
        userDTO.setUserPoint(user.getUserPoint());
        userDTO.setUserRuby(user.getUserRuby()); // userRuby 설정
        userDTO.setUserGrade(user.getUserGrade()); // userRuby 설정
        userDTO.setUserGrade(user.getUserGrade());
        userDTO.setUserNum(user.getUserNum());

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String token = jwtUtil.extractToken(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail").toString();
        String userRealname = request.get("userRealname");
        String userNickname = request.get("userNickname");
        String userBirthdate = request.get("userBirthdate");

        Users updatedUser = userService.updateProfile(email, userRealname, userNickname, userBirthdate);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail").toString();
        boolean deleted = userService.deleteUser(email);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/send-password")
    public ResponseEntity<?> sendPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            emailService.sendEmailForCertification(email);
            log.info("임시 비밀번호가 이메일로 전송되었습니다: {}", email);
            return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            log.error("임시 비밀번호 전송 중 오류 발생: ", e);
            return ResponseEntity.status(500).body("임시 비밀번호 전송 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/players")
    public ResponseEntity<List<Users>> getPlayers() {
        List<Users> users = userService.getAllUsers(); // 모든 사용자 정보 가져오기 (적절한 서비스 메소드 호출)
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UsersDTO> getCurrentUser(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail", String.class);
        Users user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UsersDTO usersDTO = new UsersDTO(
                user.getUserEmail(),
                user.getUserPw(),
                user.getUserNickname(),
                user.isSlogin(),
                user.getMemRoleList().stream().map(Enum::name).collect(Collectors.toList())
        );

        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String token = jwtUtil.extractToken(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail", String.class);
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        boolean isPasswordChanged = userService.changePassword(email, currentPassword, newPassword);

        if (!isPasswordChanged) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect");
        }

        return ResponseEntity.ok("비밀번호가 변경되었습니다!");
    }

    @GetMapping("/rankings")
    public ResponseEntity<List<UsersDTO>> getRankings() {
        List<UsersDTO> rankings = userService.getRankings();
        return ResponseEntity.ok(rankings);
    }

    // 새로운 메서드 추가: 사용자가 동물을 선택하는 기능
    @PostMapping("/select-animal")
    public ResponseEntity<?> selectAnimal(@RequestBody Map<String, Long> request, HttpServletRequest httpRequest) {
        String token = jwtUtil.extractToken(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail", String.class);
        Long animalId = request.get("animalId");

        boolean isSelected = userService.selectAnimal(email, animalId);
        if (!isSelected) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to select animal");
        }

        return ResponseEntity.ok("Animal selected successfully");
    }
}
