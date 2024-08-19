package kr.bit.animalinc.entity.admin;

import jakarta.persistence.*;
import kr.bit.animalinc.dto.admin.BanDTO;
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

    private Long userNum;

    private Date unlockDate;
    private String banReason;
    private Date bannedDate;


}
