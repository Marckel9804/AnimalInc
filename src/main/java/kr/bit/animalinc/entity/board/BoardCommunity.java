package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import kr.bit.animalinc.entity.user.Users;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
//@ToString(exclude = "author")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bcId;

    @Column(nullable = false, length = 20)
    private String bcCode;

    @Column(nullable = false, length = 100)
    private String title;

    private String content;

//    private List<File> files;

    private Date writeDate;

    @ManyToOne
    @JoinColumn(name = "user_num", nullable = false)
    private Users bcUser;

    @OneToMany(mappedBy = "boardCommunity", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
