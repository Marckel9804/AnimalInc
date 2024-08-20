package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.dto.game.GameUsersStatusDTO;
import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.service.game.GameUsersStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        log.info("Received request to insert user status with gameRoomId: {} and userNum: {}", gameRoomId, userNum);

        GameRoom gameRoom = gameRoomRepository.findById(gameRoomId).orElse(null);
        if (gameRoom != null) {
            gameUsersStatusService.saveUserStatus(gameRoom, userNum);
            return ResponseEntity.ok("User status inserted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game room not found");
        }
    }

    @PostMapping("/saveUserStatus")
    public ResponseEntity<String> saveUserStatus(@RequestParam String gameRoomId, @RequestParam long userNum) {
        // 전달된 값을 로그에 출력
        log.info("Saving user status for gameRoomId: {} and userNum: {}", gameRoomId, userNum);

        // gameRoomId로 GameRoom 객체를 조회
        GameRoom gameRoom = gameRoomRepository.findById(gameRoomId).orElse(null);
        if (gameRoom != null) {
            // User 상태를 데이터베이스에 저장
            gameUsersStatusService.saveUserStatus(gameRoom, userNum);
            return ResponseEntity.ok("User status saved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game room not found");
        }
    }


}
