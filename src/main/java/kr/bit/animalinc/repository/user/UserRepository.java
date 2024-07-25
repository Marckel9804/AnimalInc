package kr.bit.animalinc.repository.user;

import kr.bit.animalinc.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String user_email);

    Users findbyUsername(String user_name);

    Users findByIdAndPassword(Long user_id, String user_pw);

}
