package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faq_id;

    @Column(length = 50, nullable = false)
    private String qUsername;

}
