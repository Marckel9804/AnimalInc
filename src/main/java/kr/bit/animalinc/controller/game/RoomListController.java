package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.dto.game.GameRoomDTO;
import kr.bit.animalinc.service.game.RoomListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/game")
@CrossOrigin("*")
public class RoomListController {

    @Autowired
    private RoomListService roomListService;

    // 공지사항 가져오기

    // 방 리스트 가져오기
    @GetMapping("/selectAllRoom")
    public List<GameRoomDTO> selectAllRoom() {
        List<GameRoomDTO> gameRooms = roomListService.selectAllRoom();
        return gameRooms;
    }

    // 방 만들기
    @PostMapping("/insertRoom")
    public void insertRoom(@RequestBody GameRoomDTO gameRoomDTO) {
        log.info("gameRoomDTO : {}", gameRoomDTO.toString());
        roomListService.insertRoom(gameRoomDTO);
    }

    // 방에 입장하면 플레이어 수 증가시키기
    @PostMapping("/updateCount/{roomId}")
    public void updatePlayerCount(@PathVariable String roomId) {
        log.info("방 번호 받아오기 : {}", roomId);
        roomListService.updatePlayerCount(roomId);
    }

}
