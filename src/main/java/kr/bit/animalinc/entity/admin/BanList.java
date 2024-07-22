package kr.bit.animalinc.entity.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banId;

    private Date unlockDate;
    private String banReason;
    private Date bannedDate;

//    @ManyToOne
//    @JoinColumn(name = "userNum", nullable = false)
//    private Users user;
}
