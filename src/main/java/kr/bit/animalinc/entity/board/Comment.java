package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import kr.bit.animalinc.entity.user.Users;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;
    private Date creatDate;

    @ManyToOne
    @JoinColumn(name = "bc_id", nullable = false)
    private BoardCommunity boardCommunity;

    @ManyToOne
    @JoinColumn(name = "user_num", nullable = false)
    private Users cUser;
}
