package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.service.game.GameUsersStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/game")
@CrossOrigin("*")
@Slf4j
public class GameUsersStatusController {

    // Logger 선언
    private static final Logger logger = LoggerFactory.getLogger(GameUsersStatusController.class);

    @Autowired
    private GameUsersStatusService gameUsersStatusService;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @PostMapping("/insertUserStatus")
    public ResponseEntity<String> insertUserStatus(@RequestParam String gameRoomId, @RequestParam long userNum) {
        // 전달된 값을 로그에 출력
        log.info("Received request to insert user status with gameRoomId: {} and userNum: {}", gameRoomId, userNum);

        GameRoom gameRoom = gameRoomRepository.findById(gameRoomId).get();
        gameUsersStatusService.saveUserStatus(gameRoom, userNum);
        return ResponseEntity.ok("User status inserted successfully");
    }
}
