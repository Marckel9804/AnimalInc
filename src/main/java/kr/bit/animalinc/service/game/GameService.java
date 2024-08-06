package kr.bit.animalinc.service.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameStockStatus;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.game.stock.Stock;
import kr.bit.animalinc.entity.game.stock.StockHistory;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameStockStatusRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import kr.bit.animalinc.repository.game.stock.StockHistoryRepository;
import kr.bit.animalinc.repository.game.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    @Autowired
    GameRoomRepository gameRoomRepository;

    @Autowired
    GameUsersStatusRepository gameUsersStatusRepository;

    @Autowired
    GameStockStatusRepository gameStockStatusRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    StockHistoryRepository stockHistoryRepository;

    public Optional<GameRoom> getGameRoomById(String roomId) {
        return gameRoomRepository.findById(roomId);
    }

    public List<GameUsersStatus> getUserStatus(String roomId){
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElse(null);
        return gameUsersStatusRepository.findByGameRoom(gameRoom);
    }

    public GameUsersStatus getUserStatus(long userNum, String roomId) {

        List<GameUsersStatus> gameUsersStatuses =  getUserStatus(roomId);
        for(GameUsersStatus gameUsersStatus : gameUsersStatuses){
            if(gameUsersStatus.getUserNum() == userNum){
                return gameUsersStatus;
            }
        }
        return null;
    }

    public List<GameStockStatus> getGameStockStatus(String roomId){
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElse(null);
        return gameStockStatusRepository.findByGameRoom(gameRoom);
    }

    public void addStock(String roomId, int turn){
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElse(null);
        if(turn == 1){
            initStock(gameRoom);
            return;
        }else if(turn < 12 && turn > 1){
            int year = Objects.requireNonNull(gameRoom).getYear();
            List<StockHistory> stocks = stockHistoryRepository.findAllByYearAndMonthOrderByStock(year,turn);
            List<GameStockStatus> gameStockStatuses = gameStockStatusRepository.findByGameRoomAndTurnOrderByStockId(gameRoom, turn-1);
            List<GameStockStatus> gameStockStatuses2 = new ArrayList<>();
            for(int i=0; i<stocks.size(); i++){
                GameStockStatus gameStockStatus = new GameStockStatus();
                gameStockStatus.setId(gameRoom.getGameRoomId()+"-"+stocks.get(i).getId());
                gameStockStatus.setGameRoom(gameRoom);
                gameStockStatus.setStockId(gameStockStatuses.get(i).getStockId());
                gameStockStatus.setPrice((int)(gameStockStatuses.get(i).getPrice()*(100+gameStockStatuses.get(i).getWeight())/100));
                gameStockStatus.setTurn(turn);
                gameStockStatus.setWeight(stocks.get(i).getWeight());
                gameStockStatuses2.add(gameStockStatus);
            }
            gameStockStatusRepository.saveAll(gameStockStatuses2);
        }
        List<GameStockStatus> gameStockStatuses = new ArrayList<>();
        int year = Objects.requireNonNull(gameRoom).getYear();

    }

    public void initStock(GameRoom gameRoom){
        List<GameStockStatus> gameStockStatuses = new ArrayList<>();
        int year = Objects.requireNonNull(gameRoom).getYear();
        //초기 설정이니까 무조건 1월 가중치만 가져오기
        List<StockHistory> stocks = stockHistoryRepository.findAllByYearAndMonthOrderByStock(year,1);
        Random rand = new Random();
        for(StockHistory stock : stocks){
            //0 = 주식이름, 1-연도, 2-턴
            String[] ids = stock.getId().split("-");

            int price = 0;
            int num1 = rand.nextInt(90001) + 10000; // 30000 ~ 100000
            int num2 = rand.nextInt(200001) + 100000; // 100000 ~ 300000
            price = rand.nextBoolean() ? num1 : num2;//10만 이하 이상 같은 확률로 뽑기

            GameStockStatus gameStockStatus = new GameStockStatus();
            gameStockStatus.setId(gameRoom.getGameRoomId()+"-"+stock.getId());
            gameStockStatus.setGameRoom(gameRoom);
            gameStockStatus.setStockId(ids[0]);
            gameStockStatus.setPrice(price);
            gameStockStatus.setTurn(1);
            gameStockStatus.setWeight(stock.getWeight());
            gameStockStatuses.add(gameStockStatus);

        }
        gameStockStatusRepository.saveAll(gameStockStatuses);
    }

}
