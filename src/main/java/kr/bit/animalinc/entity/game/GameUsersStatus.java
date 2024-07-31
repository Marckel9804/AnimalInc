package kr.bit.animalinc.entity.game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameUsersStatus {
    @Id
    @ManyToOne
    @JoinColumn(name = "gameRoomId")
    private String gameRoomId;

    private long userNum;
    private int lottery;
    private int fakeNews;
    private int timeMachine;
    private int shortSelling;
    private int worthInfo;
    private int goodNews;
    private int badNews;
    private int food1;
    private int food2;
    private int food3;
    private int food4;
    private int ship1;
    private int ship2;
    private int ship3;
    private int ship4;
    private int enter1;
    private int enter2;
    private int enter3;
    private int enter4;
    private int tech1;
    private int tech2;
    private int tech3;
    private int tech4;
    private int cash;
}
