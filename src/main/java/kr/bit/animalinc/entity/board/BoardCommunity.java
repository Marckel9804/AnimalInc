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


    private String writeDate;

    private String userEmail;

    @ElementCollection
    @CollectionTable(name = "imgFiles", joinColumns = @JoinColumn(name = "bcId"))
    @Column(name = "fileName")
    private List<String> files;

//    @OneToMany(mappedBy = "boardCommunity", cascade = CascadeType.ALL)
//    private List<BoardImage> boardImageList;

    @OneToMany(mappedBy = "boardCommunity", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
