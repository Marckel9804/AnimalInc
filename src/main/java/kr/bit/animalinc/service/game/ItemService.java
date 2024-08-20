package kr.bit.animalinc.service.game;

import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameStockStatusRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    GameUsersStatusRepository gameUsersStatusRepository;
    @Autowired
    GameStockStatusRepository gameStockStatusRepository;
    @Autowired
    GameRoomRepository gameRoomRepository;

    public void shortSelling(String roomId, Long userNum, String stockId, int amount){

    }

    public void lottery(String roomId, Long userNum){

    }

    public void timeMachine(String roomId, Long userNum){

    }

    public void worthInfo(String roomId, Long userNum){

    }

    public void fakeNews(String roomId, Long userNum){
        
    }




}
