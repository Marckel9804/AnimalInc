package kr.bit.animalinc.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;          // 아이템 이름
    private String itemDescription;   // 아이템 설명
    private String itemType;          // 아이템 타입 (예: 프로필 사진, 포인트 아이템 등)
    private int itemPrice;            // 아이템 가격 (아이템을 구매했을 때 차감되는 루비의 양)
    private String itemRarity;        // 아이템 희귀도 (예: Common, Rare, Epic)
    private String itemImage;         // 아이템 이미지 URL


}