package kr.bit.animalinc.service.game;

import kr.bit.animalinc.dto.game.GameRoomDTO;
import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoomListService {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    // 방 리스트 가져오기
    public List<GameRoomDTO> selectAllRoom() {
        List<GameRoom> allRooms = gameRoomRepository.findAll();
        // Entity 를 DTO 로 변환
        List<GameRoomDTO> allDTORooms = new ArrayList<>();
        for (GameRoom room : allRooms) {
            allDTORooms.add(new GameRoomDTO().toGameRoomDto(room));
        }
        return allDTORooms;
    }

    // 방 만들기
    public void insertRoom(GameRoomDTO gameRoomDTO) {
        GameRoom gameRoom = new GameRoom();
        int randomYear = (int)(Math.random() * 10) + 2014;
        // init 정보 세팅
        gameRoom.setGameRoomId(gameRoomDTO.getGameRoomId());
        gameRoom.setTurn(0);
        gameRoom.setPlayers(1); // 현재 방에 입장한 인원 (방 생성 단계니까 한명밖에 없음)
        gameRoom.setBots(gameRoomDTO.getPlayers() - 1); // 봇 수 = 게임방 전체 인원수 - 입장한 인원
        gameRoom.setRoomName(gameRoomDTO.getRoomName());
        gameRoom.setTier(gameRoomDTO.getTier());
        gameRoom.setYear(randomYear);
        // 방 생성
        gameRoomRepository.save(gameRoom);
    }

    // 방에 입장하면 플레이어 수 증가시키기
    public void updatePlayerCount(String roomId) {
        Optional<GameRoom> room = gameRoomRepository.findById(roomId);
        GameRoom gameRoom = null;
        /*
        프론트에서 게임방 인원이 전부 차면 유저에게 방이 보이지 않도록 설정해두었기 때문에
        백에서는 인원 수 증감만 처리하면 된다...
         */
        if(room.isPresent()) {
            gameRoom = room.get();
            log.info("gameRoom ? {}", gameRoom.toString());
        }
        gameRoom.setPlayers(gameRoom.getPlayers() + 1);
        gameRoom.setBots(gameRoom.getBots() - 1);
        gameRoomRepository.save(gameRoom);
    }

}
