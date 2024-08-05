package kr.bit.animalinc.service.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameStockStatus;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameStockStatusRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class GameService {
    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    GameUsersStatusRepository gameUsersStatusRepository;

    @Autowired
    GameStockStatusRepository gameStockStatusRepository;

    public Optional<GameRoom> getGameRoomById(String roomId) {
        return gameRoomRepository.findById(roomId);
    }

    public List<GameUsersStatus> getUserStatus(String roomId){
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElse(null);
        return gameUsersStatusRepository.findByGameRoom(gameRoom);
    }

    public List<GameStockStatus> getGameStockStatus(String roomId){
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElse(null);
        return gameStockStatusRepository.findByGameRoom(gameRoom);
    }

    public void initStock(String roomId){
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElse(null);
        int year = Objects.requireNonNull(gameRoom).getYear();
        Random rand = new Random();
        for(int i = 1; i< 6; i++){
            for(int j = 1; j< 13; j++){
                int num1 = rand.nextInt(90001) + 10000; // 30000 ~ 100000
                int num2 = rand.nextInt(200001) + 100000; // 100000 ~ 300000
                int finalNum = rand.nextBoolean() ? num1 : num2;//10만 이하 이상 같은 확률로 뽑기
            }
        }
    }

}
