package kr.bit.animalinc.entity.payment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String paymentKey;

    private String userEmail;

    private int amount; // 결제 금액

    private int rubyAmount; // 구매한 루비 수량

    private String status; // 결제 상태 (PENDING, SUCCESS, FAILURE)

    private String failureMessage; // 실패 메시지 (선택적)

    private LocalDateTime paymentDate;
}
