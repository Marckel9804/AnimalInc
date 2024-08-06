package kr.bit.animalinc.entity.game.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    private String stockId;

    private String realStock;
    //1- 식품. 2-조선. 3-엔터. 4-전자. 5-테크,IT
    private int industry;

}
