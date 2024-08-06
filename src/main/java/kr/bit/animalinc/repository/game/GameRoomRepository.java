package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface GameRoomRepository extends JpaRepository<GameRoom, String> {


}
