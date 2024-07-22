package kr.bit.animalinc.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierCountRepository extends JpaRepository<TierCountRepository, Long> {
}
