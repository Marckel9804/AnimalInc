package kr.bit.animalinc.controller.board;

import kr.bit.animalinc.dto.board.BoardWriteDTO;
import kr.bit.animalinc.entity.board.BoardCommunity;
import kr.bit.animalinc.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/test")
    public ResponseEntity<Map<String,Object>> testBoardController() {
        BoardCommunity res = boardService.getBoardCommunity(1L);

        return ResponseEntity.ok(Map.of("data", res));
    }

    @GetMapping("")
    public Page<BoardCommunity> getBoardCommunities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardCommunities(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardCommunity> getBoardCommunity(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardCommunity(id));
    }


    @PostMapping("/test/writedto")
    public ResponseEntity<?> writeBoardCommunity(@RequestBody BoardWriteDTO dto) {
        log.info("\ndto >> " + dto.toString());

        BoardCommunity addBoard = dto.toBoardCommunity();

        BoardCommunity result = boardService.addBoardCommunity(addBoard);

        return ResponseEntity.ok(result);
    }

    @PutMapping("")
    public ResponseEntity<?> updateBoardCommunity(@RequestBody BoardCommunity updateBoard,
                                                  Principal principal
    ) {



        log.info("\nupdateBoardCommunity >> " + updateBoard.toString());
        BoardCommunity result = boardService.updateBoardCommunity(updateBoard);

        return ResponseEntity.ok(result);
    }
}
