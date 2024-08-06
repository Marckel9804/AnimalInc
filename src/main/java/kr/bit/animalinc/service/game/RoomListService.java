package kr.bit.animalinc.service.game;

import kr.bit.animalinc.dto.game.GameRoomDTO;
import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomListService {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    public void insertRoom(GameRoomDTO gameRoomDTO) {
        GameRoom gameRoom = new GameRoom();
        // init 정보 세팅
        gameRoom.setGameRoomID(gameRoomDTO.getGameRoomId());
        gameRoom.setTurn(0);
        gameRoom.setTier(gameRoomDTO.getUserGrade());
        gameRoom.setPlayers(1);
        gameRoom.setBots(3);
        // 방 생성
        gameRoomRepository.save(gameRoom);
    }

}
