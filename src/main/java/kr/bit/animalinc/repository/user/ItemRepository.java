package kr.bit.animalinc.repository.user;

import kr.bit.animalinc.entity.user.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
