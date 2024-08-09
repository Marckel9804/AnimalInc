package kr.bit.animalinc.entity.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameUsersStatusKey implements Serializable {

    private String gameRoomId;
    private long userNum;

    // equals() and hashCode() methods should be implemented as well
}
