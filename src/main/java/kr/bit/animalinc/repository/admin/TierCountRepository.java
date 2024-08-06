package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.TierCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TierCountRepository extends JpaRepository<TierCount, Long> {
    TierCount findByTuDate(Date tuDate);
}
