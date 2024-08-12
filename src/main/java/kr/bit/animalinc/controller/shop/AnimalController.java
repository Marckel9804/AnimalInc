package kr.bit.animalinc.controller.shop;

import jakarta.servlet.http.HttpServletRequest;
import kr.bit.animalinc.entity.shop.Animal;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.shop.AnimalService;
import kr.bit.animalinc.service.shop.GachaService;
import kr.bit.animalinc.util.JWTUtil;
import kr.bit.animalinc.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {
    @Autowired
    private AnimalService animalService;

    @Autowired
    private GachaService gachaService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // 도감 데이터 가져오기 API
    @GetMapping("/encyclopedia")
    public ResponseEntity<List<Animal>> getAllAnimals() {
        List<Animal> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    }

    // 가차 뽑기 API
    @PostMapping("/pull")
    public ResponseEntity<Animal> pullGacha(@RequestHeader("Authorization") String token) {
        try {
            // JWT 토큰에서 이메일을 추출합니다.
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);

            // 이메일을 통해 사용자를 조회합니다.
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 가차 뽑기 서비스 호출
            Animal result = gachaService.pullGacha(email);

            return ResponseEntity.ok(result);
        } catch (GachaService.InsufficientRubyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 가차 결과 가져오기 API
    @GetMapping("/gacha-result")
    public ResponseEntity<Animal> getGachaResult(@RequestHeader("Authorization") String token) {
        try {
            // JWT 토큰에서 이메일을 추출합니다.
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);

            // 이메일을 통해 사용자를 조회합니다.
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 최근 뽑은 가차 결과를 가져옵니다.
            Animal result = gachaService.getRecentGachaResult(email);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
