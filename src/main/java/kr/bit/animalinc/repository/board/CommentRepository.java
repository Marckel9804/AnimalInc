package kr.bit.animalinc.repository.board;

import kr.bit.animalinc.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardCommunity_UserEmailOrderByCreatDateDesc(String userEmail);
}
