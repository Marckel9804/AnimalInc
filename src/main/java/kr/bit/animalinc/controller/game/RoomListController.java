package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.dto.game.GameRoomDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/game")
@CrossOrigin("*")
public class RoomListController {
    // 공지사항 가져오기

    // 방 리스트 가져오기

    // 방 만들기
    @PostMapping("/insertroom")
    public void insertRoom(@RequestBody GameRoomDTO gameRoomDTO) {
        log.info("gameRoomDTO : {}", gameRoomDTO.toString());
    }

}
