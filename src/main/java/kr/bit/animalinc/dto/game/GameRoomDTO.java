package kr.bit.animalinc.dto.game;

import kr.bit.animalinc.entity.game.GameRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomDTO {

    private String gameRoomId;
    private String roomName;
    private String tier;
    private int players;
    private int bots;

    //Entity => DTO 변환 메서드
    public GameRoomDTO toGameRoomDto(GameRoom gameRoom) {
        return GameRoomDTO.builder()
                .gameRoomId(gameRoom.getGameRoomId())
                .roomName(gameRoom.getRoomName())
                .tier(gameRoom.getTier())
                .players(gameRoom.getPlayers())
                .bots(gameRoom.getBots())
                .build();
    }

}
