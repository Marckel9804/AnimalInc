package kr.bit.animalinc.service.payment;

import kr.bit.animalinc.entity.payment.Payment;
import kr.bit.animalinc.repository.payment.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
// 결제 승인 요청 및 결제 정보 저장 등의 기능을 수행
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public Payment savePayment(Payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Payment processPaymentApproval(String paymentKey, String orderId, int amount) {
        // 여기서 실제 결제 승인 API 호출
        // 예제 코드를 위한 단순화된 처리
        Payment payment = new Payment();
        payment.setPaymentKey(paymentKey);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod("카드"); // 예시 값
        return savePayment(payment);
    }
}