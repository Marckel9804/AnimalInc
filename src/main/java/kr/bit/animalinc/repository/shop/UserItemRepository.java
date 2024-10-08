package kr.bit.animalinc.repository.shop;

import kr.bit.animalinc.entity.user.UserItem;
import kr.bit.animalinc.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    public List<UserItem> findByUser(Users User);
}
