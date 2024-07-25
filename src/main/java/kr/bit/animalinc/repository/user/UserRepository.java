package kr.bit.animalinc.repository.user;

import kr.bit.animalinc.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
