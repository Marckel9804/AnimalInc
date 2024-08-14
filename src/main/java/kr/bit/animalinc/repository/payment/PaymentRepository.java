package kr.bit.animalinc.repository.payment;

import kr.bit.animalinc.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
// 결제 정보를 데이터베이스에 저장하고 관리
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Custom query methods can be defined here
}
