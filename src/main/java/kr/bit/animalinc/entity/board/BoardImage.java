package kr.bit.animalinc.entity.board;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bcimgId;

    private String imgName;

    @ManyToOne
    @JoinColumn(name = "bcId", nullable = false)
    private BoardCommunity boardCommunity;
}
