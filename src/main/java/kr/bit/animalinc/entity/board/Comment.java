package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String content;

    @ManyToOne
    @JoinColumn(name = "bcId", nullable = false)
    private BoardCommunity boardCommunity;

    //    @ManyToOne
//    @JoinColumn(name = "user_num", nullable = false)
//    private Users user;
}
