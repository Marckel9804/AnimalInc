package kr.bit.animalinc.repository.board;

import kr.bit.animalinc.entity.board.BoardFAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFAQRepository extends JpaRepository<BoardFAQ, Long> {
}
