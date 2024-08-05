package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@ToString(exclude = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bcId;

    private String type;

    @Column(nullable = false, length = 20)
    private String bcCode;

    @Column(nullable = false, length = 100)
    private String title;

    private String content;

//    private List<File> files;

    private String writeDate;

    private Long userNum;

    @OneToMany(mappedBy = "boardCommunity", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
