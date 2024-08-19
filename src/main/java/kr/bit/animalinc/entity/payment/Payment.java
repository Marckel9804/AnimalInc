package kr.bit.animalinc.entity.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String paymentKey;
    private String orderId;
    private String paymentMethod;
    private String paymentName;
    private Integer amount;
    private LocalDateTime paymentDate;
    private String userEmail;  // 결제와 연결된 사용자의 이메일
    private int rubyAmount;    // 결제 시 구매한 루비 수량


}
