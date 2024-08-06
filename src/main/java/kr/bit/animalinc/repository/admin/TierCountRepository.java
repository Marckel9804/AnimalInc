package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.TierCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierCountRepository extends JpaRepository<TierCount, Long> {
}
