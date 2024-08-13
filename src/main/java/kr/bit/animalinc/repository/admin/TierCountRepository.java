package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.TierCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TierCountRepository extends JpaRepository<TierCount, Long> {
    TierCount findByTuDate(Date tuDate);

    @Query("SELECT t FROM TierCount t WHERE FUNCTION('YEAR', t.tuDate) = :year AND FUNCTION('MONTH', t.tuDate) = :month")
    List<TierCount> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
