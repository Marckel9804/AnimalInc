package kr.bit.animalinc.service.shop;


import kr.bit.animalinc.entity.user.Item;
import kr.bit.animalinc.entity.user.UserItem;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserItemRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserItemService {

    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addUserItem(Users user, Item item) {
        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItem(item);
        userItemRepository.save(userItem);
    }

    public List<Item> getUserItems(String email) {
        Users user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

        return userItemRepository.findByUser(user)
                .stream()
                .map(UserItem::getItem)
                .collect(Collectors.toList());
    }
}
