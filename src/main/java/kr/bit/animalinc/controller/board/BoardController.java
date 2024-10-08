package kr.bit.animalinc.controller.board;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import kr.bit.animalinc.config.BucketConfig;
import kr.bit.animalinc.dto.board.BoardWriteDTO;
import kr.bit.animalinc.entity.board.BoardCommunity;
import kr.bit.animalinc.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BucketConfig bucketConfig;

    @GetMapping("/test")
    public ResponseEntity<Map<String,Object>> testBoardController() {
        BoardCommunity res = boardService.getBoardCommunity(1L);

        return ResponseEntity.ok(Map.of("data", res));
    }

    @GetMapping("")
    public Page<BoardCommunity> getBoardCommunities(
            @RequestParam(defaultValue = "notice") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardCommunities(type, page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBoard(
            @RequestParam String title,
            @RequestParam String tag,
            @RequestParam(defaultValue = "notice") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        System.out.println("search tag is" + tag);

        return ResponseEntity.ok(boardService.searchBoard(title, tag, type, page, size));
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
    public ResponseEntity<?> updateBoardCommunity(@RequestBody BoardCommunity updateBoard) {

        log.info("\nupdateBoardCommunity >> " + updateBoard.toString());
        BoardCommunity result = boardService.updateBoardCommunity(updateBoard);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(
            Principal principal
    ) {
        String email = principal.getName();

        return ResponseEntity.ok(Map.of("email", email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoardCommunity(@PathVariable Long id) {

        boardService.deleteBoardCommunity(id);

        return ResponseEntity.ok(Map.of("data", "deleted"));
    }




}
