package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.game.GameUsersStatusKey;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import kr.bit.animalinc.service.game.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @Autowired
    private GameUsersStatusRepository gameUsersStatusRepository;
    @Autowired
    private GameService gameService;
    @GetMapping("/roomInfo/{roomId}")
    public Optional<GameRoom> getRoomInfo(@PathVariable String roomId, Principal principal) {
        log.info("userInfo: {}", principal.getName());
        log.info("userInfo: {}", principal.getClass());
        return gameRoomRepository.findById(roomId);
    }

    @GetMapping("/gameStart/{roomId}")
    public void gameStart(@PathVariable String roomId) {


    }

    @GetMapping("/status/{roomId}/{userNum}/")
    public Optional<GameUsersStatus> getStatus(@PathVariable String roomId, @PathVariable Long userNum) throws ChangeSetPersister.NotFoundException {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        GameUsersStatusKey gameUsersStatusKey = new GameUsersStatusKey();
        gameUsersStatusKey.setGameRoom(gameRoom);
        gameUsersStatusKey.setUserNum(userNum);
        return gameUsersStatusRepository.findById(gameUsersStatusKey);
    }

    @GetMapping("/gameOver/{roomId}")
    public void gameOver(@PathVariable String roomId) {
        gameRoomRepository.deleteById(roomId);
    }
}
