package kr.bit.animalinc.service.board;

import kr.bit.animalinc.entity.board.BoardCommunity;
import kr.bit.animalinc.entity.board.BoardFAQ;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.board.BoardCommunityRepository;
import kr.bit.animalinc.repository.board.BoardFAQRepository;
import kr.bit.animalinc.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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


    @Transactional
    public BoardCommunity addBoardCommunity(BoardCommunity boardCommunity) {
        BoardCommunity res = boardCommunityRepository.save(boardCommunity);
        return res;
    }

    @Transactional
    public BoardCommunity updateBoardCommunity(BoardCommunity boardCommunity) {
        Optional<BoardCommunity> opBefore =
                boardCommunityRepository.findById(boardCommunity.getBcId());
        BoardCommunity before = null;

        if (opBefore.isPresent()) {
            before = opBefore.get();
        }

        before.setBcCode(boardCommunity.getBcCode());
        before.setTitle(boardCommunity.getTitle());
        before.setContent(boardCommunity.getContent());
//        before.setComments(boardCommunity.getComments());
        BoardCommunity res = boardCommunityRepository.save(before);

        return res;
    }

    @Transactional
    public BoardCommunity getBoardCommunity(Long id) {
        Optional<BoardCommunity> opBefore = boardCommunityRepository.findById(id);
        BoardCommunity result = null;
        if(opBefore.isPresent()) {
            result =  opBefore.get();
        }

        return result;
    }

    @Transactional
    public void deleteBoardCommunity(Long id) {
        Optional<BoardCommunity> opBefore = boardCommunityRepository.findById(id);
        if(opBefore.isPresent()) {
            boardCommunityRepository.delete(opBefore.get());
        }
    }


    public Page<BoardCommunity> getBoardCommunities(String type,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("writeDate").descending());
        return boardCommunityRepository.findByType(type,pageable);
    }


}

