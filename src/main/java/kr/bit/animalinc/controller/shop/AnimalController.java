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
            // JWT 토큰에서 이메일을 추출
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);

            // 이메일로 사용자 조회
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 가차 뽑기 서비스 호출
            Animal result = gachaService.pullGacha(email);

            // 뽑은 동물을 소유 목록에 추가
            user.addOwnedAnimal(result);
            userRepository.save(user);

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
            // JWT 토큰에서 이메일을 추출
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);

            // 이메일로 사용자 조회
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 최근 뽑은 가차 결과 가져오기
            Animal result = gachaService.getRecentGachaResult(email);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 사용자가 소유한 동물 목록 가져오기 API (추가)
    @GetMapping("/owned-animals")
    public ResponseEntity<List<Animal>> getOwnedAnimals(@RequestHeader("Authorization") String token) {
        try {
            // JWT 토큰에서 이메일을 추출
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);

            // 이메일로 사용자 조회
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 사용자가 소유한 동물 목록 반환
            return ResponseEntity.ok(user.getOwnedAnimals());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
