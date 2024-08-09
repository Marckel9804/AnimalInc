package kr.bit.animalinc.service.game;

import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameStockStatus;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.game.stock.StockHistory;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameStockStatusRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import kr.bit.animalinc.repository.game.stock.StockHistoryRepository;
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
    StockHistoryRepository stockHistoryRepository;

    public Optional<GameRoom> getGameRoomById(String gameRoomId) {
        return gameRoomRepository.findById(gameRoomId);
    }

    public List<GameUsersStatus> getUserStatus(String gameRoomId){
        return gameUsersStatusRepository.findByGameRoomId(gameRoomId);
    }

    public GameUsersStatus getUserStatus(long userNum, String gameRoomId) {
        return gameUsersStatusRepository.findByUserNumAndGameRoomId(userNum, gameRoomId);
    }

    public List<GameStockStatus> getGameStockStatus(String gameRoomId){
        GameRoom gameRoom = gameRoomRepository.findById(gameRoomId).orElse(null);
        return gameStockStatusRepository.findByGameRoom(gameRoom);
    }

    public void addStock(String gameRoomId, int turn){
        GameRoom gameRoom = gameRoomRepository.findById(gameRoomId).orElse(null);
        int year = Objects.requireNonNull(gameRoom).getYear();
        if(turn == 1){
            initStock(gameRoom);
            return;
        }else if(turn < 12 && turn > 1){
            List<StockHistory> stocks = stockHistoryRepository.findAllByYearAndMonthOrderByStock(year, turn);
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
        }else if(turn == 12){
            List<GameStockStatus> gameStockStatuses = gameStockStatusRepository.findByGameRoomAndTurnOrderByStockId(gameRoom, turn-1);
            List<GameStockStatus> gameStockStatuses2 = new ArrayList<>();
            for (GameStockStatus stockStatus : gameStockStatuses) {
                GameStockStatus gameStockStatus = new GameStockStatus();
                gameStockStatus.setGameRoom(gameRoom);
                gameStockStatus.setId(gameRoom.getGameRoomId() + "-" + stockStatus.getStockId() + "-" + year + "-" + turn);
                gameStockStatus.setStockId(stockStatus.getStockId());
                gameStockStatus.setPrice((int) (stockStatus.getPrice() * (100 + stockStatus.getWeight()) / 100));
                gameStockStatus.setTurn(turn);
                gameStockStatus.setWeight(0);
                gameStockStatuses2.add(gameStockStatus);
            }
            gameStockStatusRepository.saveAll(gameStockStatuses2);
        }
    }

    public void initStock(GameRoom gameRoom){
        List<GameStockStatus> gameStockStatuses = new ArrayList<>();
        int year = Objects.requireNonNull(gameRoom).getYear();
        // 초기 설정이니까 무조건 1월 가중치만 가져오기
        List<StockHistory> stocks = stockHistoryRepository.findAllByYearAndMonthOrderByStock(year, 1);
        Random rand = new Random();
        for(StockHistory stock : stocks){
            // 0 = 주식이름, 1-연도, 2-턴
            String[] ids = stock.getId().split("-");

            int price = 0;
            int num1 = rand.nextInt(90001) + 10000; // 10000 ~ 100000
            int num2 = rand.nextInt(200001) + 100000; // 100000 ~ 300000
            price = rand.nextBoolean() ? num1 : num2; // 10만 이하 이상 같은 확률로 뽑기

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
