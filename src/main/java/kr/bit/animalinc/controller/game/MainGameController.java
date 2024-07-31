package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.entity.game.GameStatusStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Slf4j
@CrossOrigin("*")
public class MainGameController {

    @GetMapping("/get/status/{game_room_id}")
    public GameStatusStock getStatus(@PathVariable("game_room_id") String game_room_id) {

        GameStatusStock gameStatusStock = new GameStatusStock();
        return gameStatusStock;
    }
}
