package kr.bit.animalinc.entity.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@IdClass(GameUsersStatusKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameUsersStatus {

    @Id
    @Column(name = "gameRoomId")
    private String gameRoomId;

    @Id
    @Column(name = "userNum")
    private long userNum;

    private int cash;
    private int enter1;
    private int enter2;
    private int enter3;
    private int enter4;
    private int fakeNews;
    private int food1;
    private int food2;
    private int food3;
    private int food4;
    private int lottery;
    private int ship1;
    private int ship2;
    private int ship3;
    private int ship4;
    private int shortSelling;
    private int tech1;
    private int tech2;
    private int tech3;
    private int tech4;
    private int timeMachine;
    private int worthInfo;
    private int badNews;  // 새로운 필드 추가
    private int goodNews;  // 새로운 필드 추가
    private int elec1;  // 새로운 필드 추가
    private int elec2;  // 새로운 필드 추가
    private int elec3;  // 새로운 필드 추가
    private int elec4;  // 새로운 필드 추가
}
