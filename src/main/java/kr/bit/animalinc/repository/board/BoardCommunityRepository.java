package kr.bit.animalinc.repository.board;

import kr.bit.animalinc.entity.board.BoardCommunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardCommunityRepository extends JpaRepository<BoardCommunity, Long> {

    Optional<BoardCommunity> findByUserEmail(String userEmail);

    Page<BoardCommunity> findByTitleContainingAndBcCodeAndType(String title, String bcCode, String type, Pageable pageable);
    Page<BoardCommunity> findByTitleContainingAndType(String title, String type, Pageable pageable);

    Page<BoardCommunity> findByType(String type, Pageable pageable);
}
