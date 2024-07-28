package kr.bit.animalinc.repository.user;

import kr.bit.animalinc.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserEmail(String userEmail);
    Optional<Users> findByUserId(String userId);
}
