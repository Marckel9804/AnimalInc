package kr.bit.animalinc.repository.game.stock;

import kr.bit.animalinc.entity.game.stock.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, String> {

    List<StockHistory> findAllByYear(int year);

    List<StockHistory> findAllByYearAndMonthOrderByStock(int year, int month);
}
