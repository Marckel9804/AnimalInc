package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCountRepository extends JpaRepository<UserCount, Long> {
}
