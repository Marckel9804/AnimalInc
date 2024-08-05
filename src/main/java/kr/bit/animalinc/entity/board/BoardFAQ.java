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
public class BoardFAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faq_id;

    private String title;
    private String code;
    private String reportUserNum;
    private String adminUserNum;
    private Date reportDate;

    private String content;
    // private List<File> files;

    @ManyToOne
    @JoinColumn(name = "userNum", nullable = false)
    private Users qUser;


}
