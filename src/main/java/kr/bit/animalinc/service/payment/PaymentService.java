package kr.bit.animalinc.service.payment;

import kr.bit.animalinc.entity.payment.Payment;
import kr.bit.animalinc.repository.payment.PaymentRepository;
import kr.bit.animalinc.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService; // UserService 주입

    @Transactional
    public Payment savePayment(Payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment processPaymentApproval(String paymentKey, String orderId, int amount) {
        // 결제 승인 처리 로직
        Payment payment = new Payment();
        payment.setPaymentKey(paymentKey);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod("카드"); // 예시 값
        payment.setPaymentDate(LocalDateTime.now()); // 결제 날짜 설정


        // 사용자의 이메일을 올바르게 설정
        String userEmail = getUserEmailFromOrderId(orderId); // OrderId로 사용자 이메일 찾기
        if (userEmail == null || userEmail.isEmpty()) {
            // 추가 로그
            System.err.println("Failed to find userEmail for orderId: " + orderId);
            throw new IllegalArgumentException("User email not found for orderId: " + orderId);
        }
        payment.setUserEmail(userEmail);

        // 결제 정보 저장
        Payment savedPayment = savePayment(payment);

        // 사용자의 루비 업데이트
        userService.updateUserRuby(userEmail, amount); // 사용자의 루비 업데이트

        return savedPayment;
    }

    // 실제로 orderId를 통해 사용자 이메일을 찾아야 합니다.
    private String getUserEmailFromOrderId(String orderId) {
        // orderId로부터 사용자 이메일을 얻는 로직 구현
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            return paymentOpt.get().getUserEmail(); // 실제 이메일 반환
        }

        // 이메일을 찾지 못한 경우 null 반환
        return null;
    }
}
