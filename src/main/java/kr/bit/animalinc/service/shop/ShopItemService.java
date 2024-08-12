package kr.bit.animalinc.service.shop;


import kr.bit.animalinc.entity.user.Item;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.shop.ItemRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserItemService userItemService;

    // 전체 아이템 목록을 가져오는 메서드
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // 아이템을 구매하는 메서드 (트랜잭션 처리 포함)
    @Transactional
    public int purchaseItem(Users user, Long itemId) {
        // 아이템 ID로 아이템을 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이템 ID입니다."));

        // 사용자의 루비가 아이템 가격보다 적으면 예외 발생
        if (user.getUserRuby() < item.getItemPrice()) {
            throw new InsufficientRubyException("루비가 부족합니다.");
        }

        // 사용자의 루비에서 아이템 가격을 차감
        user.setUserRuby(user.getUserRuby() - item.getItemPrice());

        // 유저가 구매한 아이템을 유저의 인벤토리에 추가
        userItemService.addUserItem(user, item);

        // 변경된 사용자 정보를 저장
        userRepository.save(user);

        // 최종적으로 사용자의 남은 루비 수를 반환
        return user.getUserRuby();
    }

    // 루비가 부족할 때 발생하는 예외 클래스
    public static class InsufficientRubyException extends RuntimeException {
        public InsufficientRubyException(String message) {
            super(message);
        }
    }
}