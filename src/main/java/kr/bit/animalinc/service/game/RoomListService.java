package kr.bit.animalinc.service.game;

import kr.bit.animalinc.dto.game.GameRoomDTO;
import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomListService {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private GameUsersStatusRepository gameUsersStatusRepository;

    // 공지사항 가져오기

    // 방 리스트 가져오기
    public List<GameRoomDTO> selectAllRoom() {
        List<GameRoom> allRooms = gameRoomRepository.findAll();
        // Entity 를 DTO 로 변환
        List<GameRoomDTO> allDTORooms = new ArrayList<>();
        allDTORooms.add(new GameRoomDTO().toGameRoomDto(allRooms.iterator().next()));
        return allDTORooms;
    }

    // 방 만들기
    public void insertRoom(GameRoomDTO gameRoomDTO) {
        GameRoom gameRoom = new GameRoom();
        // init 정보 세팅
        gameRoom.setGameRoomId(gameRoomDTO.getGameRoomId());
        gameRoom.setTurn(0);
        gameRoom.setPlayers(1); // 현재 방에 입장한 인원 (방 생성 단계니까 한명밖에 없음)
        gameRoom.setBots(gameRoomDTO.getPlayers() - 1); // 봇 수 = 게임방 전체 인원수 - 입장한 인원
        gameRoom.setRoomName(gameRoomDTO.getRoomName());
        gameRoom.setTier(gameRoomDTO.getTier());
        gameRoom.setYear(0);
        // 방 생성
        gameRoomRepository.save(gameRoom);

    }

    public GameRoomDTO getRoomById(String roomId) {
        Optional<GameRoom> gameRoom = gameRoomRepository.findById(roomId);
        return gameRoom.map(value -> new GameRoomDTO().toGameRoomDto(value)).orElse(null);
    }
}
