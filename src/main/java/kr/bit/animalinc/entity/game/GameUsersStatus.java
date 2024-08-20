package kr.bit.animalinc.entity.game;

import jakarta.persistence.*;
import kr.bit.animalinc.dto.game.GameUsersStatusDTO;
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
    @ManyToOne
    @JoinColumn(name = "gameRoomId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GameRoom gameRoom;

    @Id
    @Column(name = "userNum")
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
    private int elec1;
    private int elec2;
    private int elec3;
    private int elec4;
    private int tech1;
    private int tech2;
    private int tech3;
    private int tech4;
    private int cash;
    private int newsCount = 5;

    public GameUsersStatus(GameUsersStatusDTO gameUsersStatusDTO){
        this.gameRoom = gameUsersStatusDTO.getGameRoom();
        this.userNum = gameUsersStatusDTO.getUserNum();
        this.lottery = gameUsersStatusDTO.getLottery();
        this.fakeNews = gameUsersStatusDTO.getFakeNews();
        this.timeMachine = gameUsersStatusDTO.getTimeMachine();
        this.shortSelling = gameUsersStatusDTO.getShortSelling();
        this.worthInfo = gameUsersStatusDTO.getWorthInfo();
        this.goodNews = gameUsersStatusDTO.getGoodNews();
        this.badNews = gameUsersStatusDTO.getBadNews();
        this.food1 = gameUsersStatusDTO.getFood1();
        this.food2 = gameUsersStatusDTO.getFood2();
        this.food3 = gameUsersStatusDTO.getFood3();
        this.food4 = gameUsersStatusDTO.getFood4();
        this.ship1 = gameUsersStatusDTO.getShip1();
        this.ship2 = gameUsersStatusDTO.getShip2();
        this.ship3 = gameUsersStatusDTO.getShip3();
        this.ship4 = gameUsersStatusDTO.getShip4();
        this.enter1 = gameUsersStatusDTO.getEnter1();
        this.enter2 = gameUsersStatusDTO.getEnter2();
        this.enter3 = gameUsersStatusDTO.getEnter3();
        this.enter4 = gameUsersStatusDTO.getEnter4();
        this.elec1 = gameUsersStatusDTO.getElec1();
        this.elec2 = gameUsersStatusDTO.getElec2();
        this.elec3 = gameUsersStatusDTO.getElec3();
        this.elec4 = gameUsersStatusDTO.getElec4();
        this.tech1 = gameUsersStatusDTO.getTech1();
        this.tech2 = gameUsersStatusDTO.getTech2();
        this.tech3 = gameUsersStatusDTO.getTech3();
        this.tech4 = gameUsersStatusDTO.getTech4();
        this.cash = gameUsersStatusDTO.getCash();
        this.newsCount = gameUsersStatusDTO.getNewsCount();
    }
}
