package kr.bit.animalinc.repository.game.stock;

import kr.bit.animalinc.entity.game.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {

}
