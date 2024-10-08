package kr.bit.animalinc.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.bit.animalinc.dto.admin.CountDTO;
import kr.bit.animalinc.entity.shop.Animal;
import kr.bit.animalinc.entity.user.UserItemDTO;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.service.admin.CountService;
import kr.bit.animalinc.service.email.EmailService;
import kr.bit.animalinc.service.shop.AnimalService;
import kr.bit.animalinc.service.user.UserService;
import kr.bit.animalinc.util.JWTUtil;
import kr.bit.animalinc.util.RedisTokenService;
import kr.bit.animalinc.util.UserBannedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin("*")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;
    private final CountService countService;

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

        // 1. 필수 입력값 확인
        if (userEmail == null || userPw == null) {
            return ResponseEntity.status(400).body("이메일과 비밀번호를 입력해주세요");
        }

        try {
            // 2. 로그인 시도
            Users user = userService.login(userEmail, userPw);

            // 3. 로그인 실패 시 처리
            if (user == null) {
                return ResponseEntity.status(401).body("이메일이나 비밀번호를 확인해주세요");
            }

            // 4. Redis에서 기존 토큰 처리
            String existingToken = redisTokenService.getAccessToken(user.getUserNum());
            if (existingToken != null) {
                redisTokenService.deleteAccessToken(user.getUserNum());
                redisTokenService.addToBlacklist(existingToken);
            }

            // 5. 로그인 성공 시 사용자 수 집계
            countService.increaseUserCount(user.getUserNum());
            LocalDate today = LocalDate.now();
            CountDTO result = countService.getUserCountDTO(today);

            List<String> roles = user.getMemRoleList().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

            UsersDTO authenticatedUser = new UsersDTO(user.getUserNum(), user.getUserEmail(), user.getUserRealname(), user.getUserNickname(), user.isSlogin(), roles);
            Map<String, Object> claims = authenticatedUser.getClaims();

            String accessToken = jwtUtil.generateToken(claims, 30); // 액세스 토큰: 30분
            String refreshToken = jwtUtil.generateToken(claims, 60 * 24); // 리프레시 토큰: 24시간

            redisTokenService.storeAccessToken(accessToken, user.getUserNum(), Duration.ofMinutes(30));

            // 7. 리프레시 토큰을 쿠키에 저장
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(60 * 60 * 24); // 24시간
            response.addCookie(refreshTokenCookie);

            // 8. 액세스 토큰을 헤더에 추가
            response.setHeader("Authorization", "Bearer " + accessToken);

            // 9. 응답 바디 구성 및 반환
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("user", authenticatedUser);
            responseBody.put("todayUserCount", result);

            return ResponseEntity.ok(responseBody);

        } catch (UserBannedException e) {
            // UserBannedException을 RestControllerAdvice에서 처리하도록 재던짐
            throw e;
        } catch (Exception e) {
            // 그 외 예외 처리
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred during login.");
        }
    }


    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String email = request.get("email");
        String userRealname = request.get("name");
        String platform = request.get("platform");

        try {
            Users user = userService.socialLogin(userRealname, email, platform);

            // 만약 액세스토큰이 redis에 존재하면 삭제 및 블랙리스트 추가
            String existingToken = redisTokenService.getAccessToken(user.getUserNum());
            if (existingToken != null) {
                redisTokenService.deleteAccessToken(user.getUserNum());
                redisTokenService.addToBlacklist(existingToken);
            }

            // 로그인 시 금일 사용자 수에 집계 (중복적용 안됨)
            countService.increaseUserCount(user.getUserNum());
            LocalDate today = LocalDate.now();
            CountDTO result = countService.getUserCountDTO(today);

            List<String> roles = user.getMemRoleList().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());

            UsersDTO authenticatedUser = new UsersDTO(user.getUserNum(), user.getUserEmail(), user.getUserRealname(), user.getUserNickname(), user.isSlogin(), roles);

            Map<String, Object> claims = authenticatedUser.getClaims();

            String accessToken = jwtUtil.generateToken(claims, 30);
            String refreshToken = jwtUtil.generateToken(claims, 60 * 24);

            redisTokenService.storeAccessToken(accessToken, user.getUserNum(), Duration.ofMinutes(30));

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
            responseBody.put("todayUserCount", result);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalStateException e) {
            log.error("Social login error: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserBannedException e) {
            // UserBannedException을 RestControllerAdvice에서 처리하도록 재던짐
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred during social login: {}", e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred during social login.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);

            //액세스 토큰을 블랙리스트에 추가
            redisTokenService.addToBlacklist(accessToken);

            Long userNum = jwtUtil.extractAllClaims(accessToken).get("userNum", Long.class);
            redisTokenService.deleteAccessToken(userNum);
        }

        // Refresh Token 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS 사용 시 설정
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        // 선택적으로 빈 Authorization 헤더를 설정하여 클라이언트 측의 액세스 토큰을 지웁니다
        response.setHeader("Authorization", "");

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

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.extractTokenFromCookie(request, "refreshToken");

        // 리프레시 토큰이 유효한지 검사
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            removeRefreshToken(response);  // 리프레시 토큰 삭제 함수 호출
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token. Please log in again.");
        }

        Map<String, Object> claims = jwtUtil.extractAllClaims(refreshToken);
        Long userNum = Long.parseLong(claims.get("userNum").toString());

        // Redis에서 현재 저장된 액세스 토큰 확인
        String currentAccessToken = redisTokenService.getAccessToken(userNum);
        if (currentAccessToken == null) {
            removeRefreshToken(response);  // 리프레시 토큰 삭제 함수 호출
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired. Please log in again.");
        }

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtUtil.generateToken(claims, 30);

        // 기존 액세스 토큰 블랙리스트 추가 및 Redis에서 삭제
        redisTokenService.addToBlacklist(currentAccessToken);
        redisTokenService.deleteAccessToken(userNum);

        // 새로운 액세스 토큰 Redis에 저장
        redisTokenService.storeAccessToken(newAccessToken, userNum, Duration.ofMinutes(30));

        // 새로운 리프레시 토큰 생성 및 쿠키에 저장
        String newRefreshToken = jwtUtil.generateToken(claims, 60 * 24);
        Cookie newRefreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        newRefreshTokenCookie.setHttpOnly(true);
        newRefreshTokenCookie.setSecure(true);
        newRefreshTokenCookie.setPath("/");
        newRefreshTokenCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(newRefreshTokenCookie);

        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return ResponseEntity.ok("Tokens refreshed successfully");
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

        // 생년월일 형식 검사
        if (!isValidBirthdate(birthdate)) {
            return ResponseEntity.status(400).body("유효하지 않은 생년월일 형식입니다");
        }

        boolean nicknameAvailable = userService.checkNickname(nickname);
        if (!nicknameAvailable) {
            return ResponseEntity.status(400).body("Nickname already in use");
        }

        Users user = userService.completeProfile(email, birthdate, nickname);
        return ResponseEntity.ok(user);
    }
    @Autowired
    private AnimalService animalService;  // AnimalService 주입

    private boolean isValidBirthdate(String birthdate) {
        if (!birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        try {
            String[] parts = birthdate.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (year < 1900 || year > 2024) {
                return false;
            }

            if (month < 1 || month > 12) {
                return false;
            }

            if (day < 1 || day > 31) {
                return false;
            }

            // 날짜 객체로 유효성 검사 (윤년 등)
            LocalDate date = LocalDate.of(year, month, day);
            return date != null;
        } catch (Exception e) {
            return false;
        }
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

        // UserItem을 UserItemDTO로 변환
        List<UserItemDTO> userItemDTOs = user.getUserItems().stream()
                .map(userItem -> UserItemDTO.builder()
                        .userItemId(userItem.getUserItemId())
                        .itemId(userItem.getItem().getItemId())
                        .itemName(userItem.getItem().getItemName())
                        .itemDescription(userItem.getItem().getItemDescription())
                        .itemType(userItem.getItem().getItemType())
                        .itemRarity(userItem.getItem().getItemRarity())
                        .itemImage(userItem.getItem().getItemImage())
                        .build())
                .collect(Collectors.toList());

        // selected_animal_id로 animalImage 가져오기
        String animalImage = null;
        Animal selectedAnimal = user.getSelectedAnimal();
        if (selectedAnimal != null) {
            // int 타입의 animalId를 Long 타입으로 변환
            Long animalId = Long.valueOf(selectedAnimal.getAnimalId());
            Animal foundAnimal = animalService.findById(animalId);
            if (foundAnimal != null) {
                animalImage = foundAnimal.getAnimalImage();
            }
        }

        UsersDTO userDTO = new UsersDTO(
                user.getUserNum(),
                user.getUserEmail(),
                user.getUserRealname(),
                user.getUserNickname(),
                user.isSlogin(),
                user.getMemRoleList().stream().map(Enum::name).collect(Collectors.toList())
        );

        userDTO.setUserPw(user.getUserPw());
        userDTO.setUserBirthdate(user.getUserBirthdate());
        userDTO.setUserPoint(user.getUserPoint());
        userDTO.setUserGrade(user.getUserGrade());
        userDTO.setUserRuby(user.getUserRuby());
        userDTO.setUserPicture(user.getUserPicture());
        userDTO.setUserItems(userItemDTOs);  // DTO 리스트로 설정
        userDTO.setAnimalImage(animalImage); // 선택된 캐릭터 이미지 추가

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
                user.getUserNum(),
                user.getUserEmail(),
                user.getUserPw(),
                user.getUserNickname(),
                user.isSlogin(),
                user.getMemRoleList().stream().map(Enum::name).collect(Collectors.toList())
        );

        usersDTO.setUserGrade(user.getUserGrade());
        usersDTO.setUserPoint(user.getUserPoint());
        usersDTO.setUserPicture(user.getUserPicture());
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

    @PostMapping("/update-profile-picture")
    public ResponseEntity<?> updateProfilePicture(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String token = jwtUtil.extractToken(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = jwtUtil.extractAllClaims(token).get("userEmail").toString();
        String userPicture = request.get("userPicture");

        boolean isUpdated = userService.updateUserProfilePicture(email, userPicture);

        if (isUpdated) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile picture updated successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update profile picture");
        }
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

    //게임 보상
    @GetMapping("/rewards")
    public void getRewards(@RequestParam("userNum") Long userNum,
                                        @RequestParam("ruby") int ruby,
                                        @RequestParam("point") int point){
        userService.giveRewards(userNum, ruby, point);

    }

    private void removeRefreshToken(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(refreshTokenCookie);
    }

}
