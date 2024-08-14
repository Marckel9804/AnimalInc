package kr.bit.animalinc.controller.game;

import kr.bit.animalinc.dto.game.GameUsersStatusDTO;
import kr.bit.animalinc.dto.game.LadderGameDTO;
import kr.bit.animalinc.service.game.LadderGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/game/ladder")
@CrossOrigin("*")
public class LadderGameController {

    @Autowired
    private LadderGameService ladderGameService;

    // 게임방 정보로 참가자들 정보를 받아온다.
    @GetMapping("/participants/{roomId}")
    public List<LadderGameDTO> getParticipants(@PathVariable String roomId) {
        log.info("방 번호 ? {}", roomId);
        return ladderGameService.getParticipants(roomId);
    }

}
