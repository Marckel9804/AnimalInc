package kr.bit.animalinc.entity.game;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class GameUsersStatusKey implements Serializable {
    private GameRoom gameRoom;
    private long userNum;
}