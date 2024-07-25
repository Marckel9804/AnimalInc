package kr.bit.animalinc.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
//@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banId;

    private Long usernum;

    private Date unlockDate;
    private String banReason;
    private Date bannedDate;
}
