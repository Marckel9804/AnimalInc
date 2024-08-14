package kr.bit.animalinc.repository.board;

import kr.bit.animalinc.entity.board.BoardFAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFAQRepository extends JpaRepository<BoardFAQ, Long> {
    @Query("SELECT b FROM BoardFAQ b WHERE b.qUser.userEmail = :userEmail ORDER BY b.reportDate DESC")
    List<BoardFAQ> findUserReports(@Param("userEmail") String userEmail);
}
