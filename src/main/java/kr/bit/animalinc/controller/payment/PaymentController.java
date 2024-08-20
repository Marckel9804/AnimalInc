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

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            Payment payment = paymentService.initiatePayment(
                    paymentRequest.getOrderId(),
                    paymentRequest.getUserEmail(),
                    paymentRequest.getAmount()
            );
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to initiate payment: " + e.getMessage());
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approvePayment(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        try {
            // 결제가 성공하면 processPaymentResult 호출
            Payment payment = paymentService.processPaymentResult(paymentKey, orderId, amount, true, null);

            // 결제 승인 후 사용자 루비 업데이트를 여기서 더 이상 호출하지 않음
            // userService.updateUserRuby(email, rubyAmount); // 중복 제거

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment approval failed: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> handleSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        return approvePayment(paymentKey, orderId, amount);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> handleFailure(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount, @RequestParam String message) {
        // 결제가 실패하면 processPaymentResult 호출
        paymentService.processPaymentResult(paymentKey, orderId, amount, false, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed: " + message);
    }
}

// PaymentRequest 클래스 정의
class PaymentRequest {
    private String orderId;
    private String userEmail;
    private int amount;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
