package kr.bit.animalinc.service.game;

import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    GameUsersStatusRepository gameUsersStatusRepository;
}
