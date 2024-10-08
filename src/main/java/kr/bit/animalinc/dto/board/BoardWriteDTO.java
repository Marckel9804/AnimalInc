package kr.bit.animalinc.dto.board;

import kr.bit.animalinc.entity.board.BoardCommunity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteDTO {
    private String type;
    private String userEmail;
    private String bcCode;
    private String title;
    private String content;
    private List<String> files;
    private String writeDate;

    public BoardCommunity toBoardCommunity() {


        BoardCommunity tmp = BoardCommunity.builder()
                .type(type)
                .userEmail(userEmail)
                .bcCode(bcCode)
                .title(title)
                .content(content)
                .writeDate(writeDate)
                .files(files)
                .build();

        return tmp;
    }
}
