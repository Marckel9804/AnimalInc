package kr.bit.animalinc.controller.payment;

import kr.bit.animalinc.entity.payment.Payment;
import kr.bit.animalinc.service.payment.PaymentService;
import kr.bit.animalinc.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// 사용자가 결제를 요청하거나 결제 후 결과를 처리하는 엔드포인트 제공
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;  // UserService 주입

    @Value("${toss.payments.success-url}")
    private String successUrl;

    @Value("${toss.payments.fail-url}")
    private String failUrl;

    @PostMapping("/approve")
    public ResponseEntity<?> approvePayment(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        try {
            Payment payment = paymentService.processPaymentApproval(paymentKey, orderId, amount);

            // 결제 승인 후 사용자 루비 업데이트
            String email = payment.getUserEmail(); // 결제 객체에서 사용자 이메일 가져오기
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("User email not found for orderId: " + orderId);
            }

            int rubyAmount = payment.getRubyAmount(); // 구매한 루비 수량 가져오기 (필요에 따라 amount로 대체)
            userService.updateUserRuby(email, rubyAmount); // 루비 수 업데이트

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            // 구체적인 예외 로그 추가 (로그 기능이 있다면 사용)
            System.err.println("Payment approval failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment approval failed: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> handleSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        return approvePayment(paymentKey, orderId, amount);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> handleFailure(@RequestParam String code, @RequestParam String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed: " + message);
    }
}
