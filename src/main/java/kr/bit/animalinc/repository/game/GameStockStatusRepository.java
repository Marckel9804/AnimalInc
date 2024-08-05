package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameStockStatus;
import kr.bit.animalinc.entity.game.GameStockStatusKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameStockStatusRepository extends JpaRepository<GameStockStatus, GameStockStatusKey> {
    List<GameStockStatus> findByGameRoom(GameRoom gameRoom);
}
