package kr.bit.animalinc.entity.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date unlockDate;
    private String banReason;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bannedDate;


}
