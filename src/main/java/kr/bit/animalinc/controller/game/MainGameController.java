package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Slf4j
@CrossOrigin("*")
public class MainGameController {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @PostMapping("/roomInfo")
    public Optional<GameRoom> getRoomInfo(@RequestBody Map<String, String> roomId) {
        System.out.println("으아아아");
        return gameRoomRepository.findById(roomId.get("roomId"));
    }
}
