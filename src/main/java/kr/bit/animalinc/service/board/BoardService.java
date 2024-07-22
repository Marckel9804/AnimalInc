package kr.bit.animalinc.service.board;

import kr.bit.animalinc.entity.board.BoardFAQ;
import kr.bit.animalinc.repository.board.BoardCommunityRepository;
import kr.bit.animalinc.repository.board.BoardFAQRepository;
import kr.bit.animalinc.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardCommunityRepository boardCommunityRepository;
    private final BoardFAQRepository boardFAQRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public List<BoardFAQ> getBoardFAQs() {
        return boardFAQRepository.findAll();
    }
}
