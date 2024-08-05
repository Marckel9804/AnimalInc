package kr.bit.animalinc.entity.game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {
    @Id
    private String gameRoomId;

    private Date gameTime;
    private int turn;
    private String roomName;
    private String tier;
    private int players;
    private int bots;

}
