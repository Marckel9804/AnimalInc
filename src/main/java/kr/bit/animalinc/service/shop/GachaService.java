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

        // 사용자의 루비가 5개 미만이면 예외를 발생시킴
        if (user.getUserRuby() < 5) {
            throw new InsufficientRubyException("Not enough rubies");
        }

        // 모든 동물 목록에서 랜덤으로 하나를 선택
        List<Animal> animals = animalRepository.findAll();
        Random random = new Random();
        Animal selectedAnimal = animals.get(random.nextInt(animals.size()));

        // 사용자의 루비 차감 및 최근 가차 결과 업데이트
        user.setUserRuby(user.getUserRuby() - 5);
        user.setLastGachaResult(selectedAnimal); // 최근 가차 결과 저장
        user.addOwnedAnimal(selectedAnimal); // 사용자의 소유 동물 목록에 추가
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
