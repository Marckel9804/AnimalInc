package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.BanList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BanListRepository extends JpaRepository<BanList, Long> {
    Optional<BanList> findByUserNum(long user_num);
}
