package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
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

//    private List<File> fileList;

    private Date writeDate;

    //    @ManyToOne
//    @JoinColumn(name = "user_num", nullable = false)
//    private Users user;
}
