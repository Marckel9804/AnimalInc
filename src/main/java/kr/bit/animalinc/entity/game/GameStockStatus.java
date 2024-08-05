package kr.bit.animalinc.entity.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@IdClass(GameStockStatusKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStockStatus {

    @Id
    @ManyToOne
    @JoinColumn(name = "gameRoomId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GameRoom gameRoom;

    @Id
    private String stockId;

    private float weight;
    private int price;
    private int turn;

}
