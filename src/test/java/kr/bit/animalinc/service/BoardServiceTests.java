package kr.bit.animalinc.service;

import kr.bit.animalinc.entity.board.BoardCommunity;
import kr.bit.animalinc.entity.board.Comment;
import kr.bit.animalinc.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testInsert() {

        for (int i = 1; i < 30; i++) {

            List<Comment> comments = new ArrayList<>();
            Date date = new Date();

            BoardCommunity bc = BoardCommunity.builder()
                    .bcCode("info")
                    .title("제목" + i)
                    .content("내용" + i)
                    .userNum(3L)
                    .writeDate(date)
                    .comments(comments)
                    .build();

            boardService.addBoardCommunity(bc);
        }
    }

    @Test
    public void testRead() {
        long boardId = 1L;

        log.info("\n\nboardId: {}", boardService.getBoardCommunity(1L));
    }

}
