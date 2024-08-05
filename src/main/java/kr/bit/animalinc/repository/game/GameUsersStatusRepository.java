package kr.bit.animalinc.repository.game;

import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.game.GameUsersStatusKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameUsersStatusRepository extends JpaRepository<GameUsersStatus, GameUsersStatusKey> {
}
