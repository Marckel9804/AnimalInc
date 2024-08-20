package kr.bit.animalinc.repository.admin;

import kr.bit.animalinc.entity.admin.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserCountRepository extends JpaRepository<UserCount, Long> {
    UserCount findByCuDate(Date cuDate);

    @Query("SELECT u FROM UserCount u WHERE FUNCTION('MONTH', u.cuDate) = :month")
    List<UserCount> findByMonth(@Param("month") int month);

    @Query("SELECT u FROM UserCount u WHERE FUNCTION('YEAR', u.cuDate) = :year AND FUNCTION('MONTH', u.cuDate) = :month")
    List<UserCount> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
