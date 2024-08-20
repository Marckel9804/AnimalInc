package kr.bit.animalinc.service;

import kr.bit.animalinc.entity.admin.BanList;
import kr.bit.animalinc.entity.admin.TierCount;
import kr.bit.animalinc.entity.admin.UserCount;
import kr.bit.animalinc.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class AdminServiceTests {
    @Autowired
    private AdminService adminService;

    @Test
    public void testAddBanlist() {
        Date now = new Date(System.currentTimeMillis());
        Date unlock = new Date(System.currentTimeMillis());


        BanList input = BanList.builder()
                .userNum(12344L)
                .unlockDate(unlock)
                .banReason("불법적인 행위")
                .bannedDate(now)
                .build();
        log.info(adminService.addBanList(input).toString());
    }

    @Test
    public void testReadBanList() {
//        log.info("\n"+adminService.getAllBanLists().toString());
//
//        log.info("\n"+adminService.getBanList(123L).toString());
    }


    
    @Test
    public void testAddBanList() {
        Date now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMonth(12);
        now.setYear(2020);
        now.setDate(24);
        Date unlock = new Date();
        unlock.setHours(0);
        unlock.setMinutes(0);
        unlock.setSeconds(0);
        unlock.setMonth(12);
        unlock.setYear(2020);
        unlock.setDate(28);
        
        BanList banList = BanList.builder()
                .userNum(123L)
                .unlockDate(unlock)
                .bannedDate(now)
                .banReason("아몰랑")
                .build();

        log.info(adminService.addBanList(banList).toString());
    }

    @Test
    public void testAddTierCount() {
        for (int i = 0; i < 10; i++) {
            Date now = new Date(System.currentTimeMillis());
            now.setMonth(now.getMonth());
            now.setDate(now.getDate() + i);

            TierCount tierCount = new TierCount();
            tierCount.setTier("BRONZE");
            tierCount.setCount(100+i*10);
            tierCount.setTuDate(now);
            adminService.addTierCount(tierCount);
        }
        log.info("\n\n"+adminService.getAllTierCount().toString());
    }
    @Test
    public void testAddUserCount() {

        for (int i = 0; i < 10; i++) {
            Date now = new Date(System.currentTimeMillis());
            now.setMonth(now.getMonth() + 1);
            now.setDate(now.getDate() + i);

            UserCount userCount = new UserCount();
            userCount.setCount(120);
            userCount.setCuDate(now);

            adminService.addUserCount(userCount);
        }

        log.info("\n"+adminService.getAllUserCount());
    }



    @Test
    public void testReadUserCountByMonty() {
        List<UserCount> userCountList = adminService.findUSByMonth(9);
        for (UserCount userCount : userCountList) {
            System.out.println(userCount);
        }
    }

}
