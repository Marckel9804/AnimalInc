package kr.bit.animalinc.entity.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStockStatus {

    @Id
    String id;

    @ManyToOne
    @JoinColumn(name = "gameRoomId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GameRoom gameRoom;

    private String stockId;

    private float weight;
    private int price;
    private int turn;

}
