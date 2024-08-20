package kr.bit.animalinc.entity.admin;

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
public class TierCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tuId;

    @Column(nullable = false, length = 30)
    private String tier;

    private Date tuDate;
    private int count;
}
