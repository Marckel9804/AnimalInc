package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface GameRoomRepository extends JpaRepository<GameRoom, String> {
    @Modifying
    @Transactional
    @Query("update GameRoom set turn = turn+1 where gameRoomId = :gameRoomId")
    void updateTurn(String gameRoomId);
}
