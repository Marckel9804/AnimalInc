package kr.bit.animalinc.repository.shop;

import kr.bit.animalinc.entity.shop.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {
}
