package kr.bit.animalinc.service;

import kr.bit.animalinc.entity.admin.BanList;
import kr.bit.animalinc.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@Slf4j
public class AdminServiceTests {
    @Autowired
    private AdminService adminService;

    @Test
    public void testAddBanlist() {
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

        BanList input = BanList.builder()
                .userNum(123L)
                .unlockDate(unlock)
                .banReason("불법적인 행위")
                .bannedDate(now)
                .build();
        log.info(adminService.addBanList(input).toString());
    }

    @Test
    public void testReadBanList() {
        log.info("\n"+adminService.getAllBanLists().toString());

        log.info("\n"+adminService.getBanList(123L).toString());
    }



}
