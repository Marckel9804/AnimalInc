package kr.bit.animalinc.dto.game;

import jakarta.persistence.Id;
import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameUsersStatusDTO {

    private GameRoom gameRoom;
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
    private boolean isMe = false;
    private String nickName;

    public static GameUsersStatusDTO toGameUserStatusDTO(GameUsersStatus gameUsersStatus) {
        return GameUsersStatusDTO.builder()
                .gameRoom(gameUsersStatus.getGameRoom())
                .userNum(gameUsersStatus.getUserNum())
                .lottery(gameUsersStatus.getLottery())
                .fakeNews(gameUsersStatus.getFakeNews())
                .timeMachine(gameUsersStatus.getTimeMachine())
                .shortSelling(gameUsersStatus.getShortSelling())
                .worthInfo(gameUsersStatus.getWorthInfo())
                .goodNews(gameUsersStatus.getGoodNews())
                .badNews(gameUsersStatus.getBadNews())
                .food1(gameUsersStatus.getFood1())
                .food2(gameUsersStatus.getFood2())
                .food3(gameUsersStatus.getFood3())
                .food4(gameUsersStatus.getFood4())
                .ship1(gameUsersStatus.getShip1())
                .ship2(gameUsersStatus.getShip2())
                .ship3(gameUsersStatus.getShip3())
                .ship4(gameUsersStatus.getShip4())
                .enter1(gameUsersStatus.getEnter1())
                .enter2(gameUsersStatus.getEnter2())
                .enter3(gameUsersStatus.getEnter3())
                .enter4(gameUsersStatus.getEnter4())
                .elec1(gameUsersStatus.getElec1())
                .elec2(gameUsersStatus.getElec2())
                .elec3(gameUsersStatus.getElec3())
                .elec4(gameUsersStatus.getElec4())
                .tech1(gameUsersStatus.getTech1())
                .tech2(gameUsersStatus.getTech2())
                .tech3(gameUsersStatus.getTech3())
                .tech4(gameUsersStatus.getTech4())
                .cash(gameUsersStatus.getCash())
                .nickName("ananymous")
                .isMe(false).build();
    }
}
