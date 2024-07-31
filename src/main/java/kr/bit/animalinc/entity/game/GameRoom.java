package kr.bit.animalinc.entity.game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {
    @Id
    private String gameRoomID;

    private int gameTime;
    private int Turn;
    private String roomName;
    private String tier;
    private int players;
    private int bots;

}
