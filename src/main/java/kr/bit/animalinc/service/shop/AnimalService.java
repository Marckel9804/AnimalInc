package kr.bit.animalinc.service.shop;

import kr.bit.animalinc.entity.shop.Animal;
import kr.bit.animalinc.repository.shop.AnimalRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
