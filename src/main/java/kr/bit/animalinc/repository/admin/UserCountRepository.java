package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserCountRepository extends JpaRepository<UserCount, Long> {
    UserCount findByCuDate(Date cuDate);
}
