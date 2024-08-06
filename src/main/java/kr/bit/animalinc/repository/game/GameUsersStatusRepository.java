package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.game.GameUsersStatusKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameUsersStatusRepository extends JpaRepository<GameUsersStatus, GameUsersStatusKey> {
    List<GameUsersStatus> findByGameRoom(GameRoom gameRoom);
    GameUsersStatus findByUserNum(long userNum); // 이 줄 추가

}
