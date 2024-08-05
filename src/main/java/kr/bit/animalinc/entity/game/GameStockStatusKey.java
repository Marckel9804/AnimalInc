package kr.bit.animalinc.entity.game;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class GameStockStatusKey {
    private GameRoom gameRoom;
    private String stockId;
}
