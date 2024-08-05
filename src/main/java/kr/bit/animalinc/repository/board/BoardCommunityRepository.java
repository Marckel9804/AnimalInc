package kr.bit.animalinc.repository.board;

import kr.bit.animalinc.entity.board.BoardCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardCommunityRepository extends JpaRepository<BoardCommunity, Long> {

    Optional<BoardCommunity> findByUserNum(Long title);
}
