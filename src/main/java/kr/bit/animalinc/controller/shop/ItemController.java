package kr.bit.animalinc.controller.shop;

import kr.bit.animalinc.entity.user.Item;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.shop.ShopItemService;
import kr.bit.animalinc.service.shop.UserItemService;
import kr.bit.animalinc.util.JWTUtil;
import kr.bit.animalinc.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ShopItemService itemService;

    @Autowired
    private UserItemService userItemService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // 아이템 목록 가져오기 API
    @GetMapping("/list")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // 아이템 구매 API
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseItem(
            @RequestHeader("Authorization") String token,
            @RequestParam Long itemId) {
        try {
            // JWT 토큰에서 이메일을 추출합니다.
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);

            // 이메일을 통해 사용자를 조회합니다.
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 아이템 구매 서비스 호출 후 남은 루비 수 반환
            int remainingRuby = itemService.purchaseItem(user, itemId);

            return ResponseEntity.ok(remainingRuby);
        } catch (ShopItemService.InsufficientRubyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // 서버 콘솔에 오류 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while purchasing the item.");
        }
    }

    // 유저의 소유 아이템 목록 가져오기 API
    @GetMapping("/my-items")
    public ResponseEntity<List<Item>> getUserItems(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);
            List<Item> items = userItemService.getUserItems(email);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 사용자 루비 수 조회 API
    @GetMapping("/ruby")
    public ResponseEntity<Integer> getUserRuby(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractAllClaims(token.substring(7)).get("userEmail", String.class);
            Users user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(user.getUserRuby());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
