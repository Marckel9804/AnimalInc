package kr.bit.animalinc.service.shop;

import kr.bit.animalinc.entity.shop.Animal;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.shop.AnimalRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GachaService {
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    // 가차 뽑기 서비스
    public Animal pullGacha(String userEmail) throws InsufficientRubyException {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

        if (user.getUserRuby() < 5) {
            throw new InsufficientRubyException("Not enough rubies");
        }

        List<Animal> animals = animalRepository.findAll();
        Random random = new Random();
        Animal selectedAnimal = animals.get(random.nextInt(animals.size()));

        user.setUserRuby(user.getUserRuby() - 5);
        user.setLastGachaResult(selectedAnimal); // 최근 가차 결과 저장
        userRepository.save(user);

        return selectedAnimal;
    }

    // 최근 가차 결과를 반환하는 서비스
    public Animal getRecentGachaResult(String userEmail) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

        return user.getLastGachaResult();
    }

    // 루비가 부족한 경우 발생하는 예외
    public static class InsufficientRubyException extends RuntimeException {
        public InsufficientRubyException(String message) {
            super(message);
        }
    }
}
