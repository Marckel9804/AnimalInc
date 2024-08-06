package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.BanList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanListRepository extends JpaRepository<BanList, Long> {
    public BanList findByUserNum(long user_num);
}
