package kr.bit.animalinc.service.payment;

import kr.bit.animalinc.entity.payment.Payment;
import kr.bit.animalinc.repository.payment.PaymentRepository;
import kr.bit.animalinc.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Payment initiatePayment(String orderId, String userEmail, int amount) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserEmail(userEmail);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PENDING"); // 결제 시도 중 상태로 설정

        // 디버깅 로그 추가
        System.out.println("Initiate Payment:");
        System.out.println("Order ID: " + orderId);
        System.out.println("User Email: " + userEmail);
        System.out.println("Amount: " + amount);

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment processPaymentApproval(String paymentKey, String orderId, int amount) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        Payment payment = paymentOpt.orElseThrow(() -> new IllegalArgumentException("Invalid orderId: " + orderId));

        payment.setPaymentKey(paymentKey);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());

        // 디버깅 로그 추가
        System.out.println("Processing payment with amount: " + amount);

        // 결제 금액에 따른 루비 수량 매핑
        int rubyAmount = mapAmountToRuby(amount);
        System.out.println("Mapped ruby amount: " + rubyAmount);
        payment.setRubyAmount(rubyAmount); // 결제 정보에 루비 수량 저장

        // `processPaymentApproval`에서 사용자 루비 업데이트 코드를 제거하여 중복 업데이트 방지
        // userService.updateUserRuby(payment.getUserEmail(), rubyAmount);

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment processPaymentResult(String paymentKey, String orderId, int amount, boolean isSuccess, String failureMessage) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid orderId: " + orderId));

        payment.setPaymentKey(paymentKey);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());

        if (isSuccess) {
            payment.setStatus("SUCCESS");

            // 루비 업데이트 로직 추가
            int rubyAmount = mapAmountToRuby(amount);
            System.out.println("Payment Success: Updating ruby amount to " + rubyAmount);
            payment.setRubyAmount(rubyAmount);

            // 결제가 성공하면 이곳에서 한 번만 사용자 루비를 업데이트
            userService.updateUserRuby(payment.getUserEmail(), rubyAmount);
        } else {
            payment.setStatus("FAILURE");
            payment.setFailureMessage(failureMessage);
            System.out.println("Payment Failure: " + failureMessage);
        }

        return paymentRepository.save(payment);
    }

    // 결제 금액에 따라 루비 수량을 매핑하는 메서드
    private int mapAmountToRuby(int amount) {
        switch (amount) {
            case 1100: return 10;
            case 3300: return 30;
            case 5500: return 50;
            case 7700: return 70;
            case 9900: return 90;
            case 11000: return 100;
            default:
                System.out.println("Unexpected amount: " + amount); // 예상치 못한 금액의 경우
                return 0;
        }
    }
}
