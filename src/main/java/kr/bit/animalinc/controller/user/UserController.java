package kr.bit.animalinc.controller.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.service.email.EmailService;
import kr.bit.animalinc.service.user.UserService;
import kr.bit.animalinc.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        if (userService.checkEmailExists(email)) {
            return ResponseEntity.status(400).body("이미 가입된 이메일입니다.");
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
    public ResponseEntity<?> login(@RequestBody UsersDTO userDTO) {
        log.info("Login attempt with email: {}", userDTO.getUserEmail());

        Users user = userService.login(userDTO.getUserEmail(), userDTO.getUserPw());
        if (user == null) {
            log.error("User not found or password mismatch for email: {}", userDTO.getUserEmail());
            return ResponseEntity.status(401).body("Invalid Email or Password");
        }

        // Convert List<MemberRole> to List<String>
        List<String> roles = user.getMemRoleList().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        UsersDTO authenticatedUser = new UsersDTO(user.getUserEmail(), user.getUserPw(), user.getUserNickname(), user.isSlogin(), roles);
        Map<String, Object> claims = authenticatedUser.getClaims();

        String accessToken = jwtUtil.generateToken(claims, 10);
        String refreshToken = jwtUtil.generateToken(claims, 60 * 24);
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        log.info("Login successful for user: {}", userDTO.getUserEmail());
        log.info("Access Token: {}", accessToken);
        log.info("Refresh Token: {}", refreshToken);

        return ResponseEntity.ok(tokens);
    }
}
