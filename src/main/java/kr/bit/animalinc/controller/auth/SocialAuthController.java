package kr.bit.animalinc.controller.auth;

import kr.bit.animalinc.entity.user.MemberRole;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.user.UserService;
import kr.bit.animalinc.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SocialAuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping("/api/oauth2/login")
    public ResponseEntity<?> loginOAuth2User(@RegisteredOAuth2AuthorizedClient OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Users user = userService.findByEmail(email).orElseGet(() -> {
            Users newUser = new Users();
            newUser.setUserEmail(email);
            newUser.setUserRealname(name);
            newUser.setSlogin(true);
            newUser.setMemRoleList(List.of(MemberRole.USER));
            return userService.register(newUser);
        });

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getUserEmail());
        claims.put("password", user.getUserPw());

        String accessToken = jwtUtil.generateToken(claims, 10);
        String refreshToken = jwtUtil.generateToken(claims, 60 * 24);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        
        return ResponseEntity.ok(tokens);
    }
}
