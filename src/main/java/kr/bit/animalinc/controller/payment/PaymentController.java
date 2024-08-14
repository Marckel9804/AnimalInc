package kr.bit.animalinc.controller.payment;

import kr.bit.animalinc.entity.payment.Payment;
import kr.bit.animalinc.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//사용자가 결제를 요청하거나 결제 후 결과를 처리하는 엔드포인트 제공
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${toss.payments.success-url}")
    private String successUrl;

    @Value("${toss.payments.fail-url}")
    private String failUrl;

    @PostMapping("/approve")
    public ResponseEntity<?> approvePayment(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        try {
            Payment payment = paymentService.processPaymentApproval(paymentKey, orderId, amount);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment approval failed");
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
