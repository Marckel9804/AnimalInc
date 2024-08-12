package kr.bit.animalinc.entity.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserItemDTO {
    private Long userItemId;        // 유저 아이템 ID
    private Long itemId;            // 아이템 ID
    private String itemName;        // 아이템 이름
    private String itemDescription; // 아이템 설명
    private String itemType;        // 아이템 타입 (예: 프로필 사진, 포인트 아이템 등)
    private String itemRarity;      // 아이템 희귀도 (예: Common, Rare, Epic)
    private String itemImage;       // 아이템 이미지 URL
}
