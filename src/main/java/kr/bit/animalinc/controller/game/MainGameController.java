package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameStockStatus;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.service.game.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Slf4j
@CrossOrigin("*")
public class MainGameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/roomInfo/{roomId}")
    public Optional<GameRoom> getRoomInfo(@PathVariable String roomId) {
        return gameService.getGameRoomById(roomId);
    }

    @GetMapping("/gameStart/{roomId}")
    public void gameStart(@PathVariable String roomId) {
        // 게임 시작 로직 추가
    }

    @GetMapping("/userStatus/{roomId}")
    public List<GameUsersStatus> getUserStatus(@PathVariable String roomId) {
        return gameService.getUserStatus(roomId);
    }

    @GetMapping("/stockStatus/{roomId}")
    public List<GameStockStatus> getStockStatus(@PathVariable String roomId) {
        return gameService.getGameStockStatus(roomId);
    }

    @GetMapping("/gameOver/{roomId}")
    public void gameOver(@PathVariable String roomId) {
        // 게임 종료 로직 추가
    }

    @GetMapping("/test/{roomId}/{turn}")
    public void test(@PathVariable String roomId, @PathVariable String turn) {
        gameService.addStock(roomId, Integer.parseInt(turn));
    }

    // 추가된 엔드포인트
    @GetMapping("/status/{userNum}")
    public ResponseEntity<GameUsersStatus> getGameUserStatus(@PathVariable long userNum) {
        GameUsersStatus status = gameService.getGameUserStatus(userNum);
        if (status != null) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
