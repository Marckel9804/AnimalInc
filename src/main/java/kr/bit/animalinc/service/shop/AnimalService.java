package kr.bit.animalinc.service.shop;

import kr.bit.animalinc.entity.shop.Animal;
import kr.bit.animalinc.repository.shop.AnimalRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    // 도감 데이터 가져오기 서비스
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    // ID로 동물 조회 서비스
    public Animal findById(Long animalId) {

        Integer intAnimalId = animalId.intValue();

        Optional<Animal> animal = animalRepository.findById(intAnimalId);
        return animal.orElse(null);  // 동물이 없을 경우 null 반환
    }

}
