package kr.bit.animalinc.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNum")
    private Users user;    // 유저 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;     // 아이템 정보

    private LocalDate acquiredDate; //아이템 획득 날짜

}