package kr.bit.animalinc.service.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GameUsersStatusService {

    // Logger 선언
    private static final Logger logger = LoggerFactory.getLogger(GameUsersStatusService.class);

    @Autowired
    private GameUsersStatusRepository gameUsersStatusRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    public void saveUserStatus(String gameRoomId, long userNum) {
        GameUsersStatus gameUsersStatus = new GameUsersStatus();

        gameUsersStatus.setGameRoomId(gameRoomId);
        gameUsersStatus.setUserNum(userNum);
        gameUsersStatus.setCash(50000000);  // 기본값 설정

        // 다른 필드들도 필요하면 초기값 설정 가능

        gameUsersStatusRepository.save(gameUsersStatus);
    }

}
