package kr.bit.animalinc.dto.admin;

import kr.bit.animalinc.entity.admin.BanList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BanDTO {
    private long userNum;
    private Date unlockDate;
    private String banReason;
    private Date bannedDate;


    public BanList toEntity() {
        return BanList.builder()
                .userNum(userNum)
                .bannedDate(bannedDate)
                .unlockDate(unlockDate)
                .banReason(banReason)
                .build();
    }
}
