package kr.bit.animalinc.entity.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {
    private Long itemId;
    private String itemName;
    private String itemDescription;
    private String itemType;
    private int itemPrice;
    private String itemRarity;
    private String itemImage;
}
