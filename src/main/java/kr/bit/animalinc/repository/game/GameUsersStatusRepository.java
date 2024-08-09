package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameUsersStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameUsersStatusRepository extends JpaRepository<GameUsersStatus, Long> {
    List<GameUsersStatus> findByGameRoomId(String gameRoomId);
    GameUsersStatus findByUserNumAndGameRoomId(long userNum, String gameRoomId);
}
