package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameStockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameStockStatusRepository extends JpaRepository<GameStockStatus, String> {
    List<GameStockStatus> findByGameRoom(GameRoom gameRoom);

    List<GameStockStatus> findByGameRoomAndTurnOrderByStockId(GameRoom gameRoom, int turn);

}
