package kr.bit.animalinc.controller.user;

import kr.bit.animalinc.entity.board.BoardCommunity;
import kr.bit.animalinc.entity.board.BoardFAQ;
import kr.bit.animalinc.entity.board.Comment;
import kr.bit.animalinc.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class UserBoardController {
    private final BoardService boardService;

    @GetMapping("/my-posts")
    public ResponseEntity<List<BoardCommunity>> getMyPosts(Principal principal) {
        String userEmail = principal.getName();
        List<BoardCommunity> myPosts = boardService.getUserPosts(userEmail);
        return ResponseEntity.ok(myPosts);
    }

    @GetMapping("/my-comments")
    public ResponseEntity<List<Comment>> getMyComments(Principal principal) {
        String userEmail = principal.getName();
        List<Comment> myComments = boardService.getUserComments(userEmail);
        return ResponseEntity.ok(myComments);
    }

    @GetMapping("/my-reports")
    public ResponseEntity<List<BoardFAQ>> getMyReports(Principal principal) {
        String userEmail = principal.getName();
        List<BoardFAQ> myReports = boardService.getUserReports(userEmail);
        return ResponseEntity.ok(myReports);
    }
}
