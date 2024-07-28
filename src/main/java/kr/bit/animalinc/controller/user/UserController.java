package kr.bit.animalinc.controller.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginInfo) {
        String userId = loginInfo.get("userId");
        String password = loginInfo.get("password");

        Users user = userService.login(userId, password);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerInfo) {
        String userId = registerInfo.get("userId");
        String password = registerInfo.get("password");
        String name = registerInfo.get("name");
        String email = registerInfo.get("email");
        String birthdate = registerInfo.get("birthdate");

        Users user = userService.register(userId, password, name, email, birthdate);
        if (user == null) {
            return ResponseEntity.status(400).body("User could not be created");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> userInfo) {
        String socialId = userInfo.get("socialId");
        String platform = userInfo.get("platform");
        String name = userInfo.get("name");
        String email = userInfo.get("email");

        Users user = userService.handleSocialLogin(socialId, platform, name, email);
        if (user == null) {
            return ResponseEntity.status(400).body("User could not be created or found");
        }
        return ResponseEntity.ok(user);
    }
}
