package kr.bit.animalinc.controller.admin;

import kr.bit.animalinc.dto.admin.BanDTO;
import kr.bit.animalinc.dto.admin.UserCountDTO;
import kr.bit.animalinc.entity.admin.BanList;
import kr.bit.animalinc.entity.admin.TierCount;
import kr.bit.animalinc.entity.admin.UserCount;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.admin.AdminService;
import kr.bit.animalinc.service.admin.CountService;
import kr.bit.animalinc.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final CountService countService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserList() {
        List<Users> usersList = userService.findAll();

        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/ban")
    public ResponseEntity<?> getBanList() {
        List<BanList> banList = adminService.getAllBanLists();
        return ResponseEntity.ok(banList);
    }

    @PostMapping("/ban")
    public ResponseEntity<?> addBan(@RequestBody BanDTO banDTO) {

        BanList addBan = banDTO.toEntity();
        BanList result = adminService.addBanList(addBan);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/ban")
    public ResponseEntity<?> updateBan(@RequestBody BanList banList) {
        BanList before = adminService.getBanList(banList.getUserNum());
        before.setBanReason(banList.getBanReason());
        before.setUnlockDate(banList.getUnlockDate());
        BanList result = adminService.updateBanList(before);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/ban/{id}")
    public ResponseEntity<?> deleteBan(@PathVariable long userNum) {
        String result = adminService.deleteBanList(userNum);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/usercount/{year}/{month}")
    public ResponseEntity<?> getUserCount(@PathVariable int year, @PathVariable int month) {
        List<UserCount> result = adminService.findUSByMonth(month);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/redis/usercount/{year}/{month}")
    public ResponseEntity<?> getRedisUserCount(@PathVariable int year, @PathVariable int month) {
        List<UserCountDTO> result = countService.getUCByYearMonth(year, month);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tiercount/{year}/{month}")
    public ResponseEntity<?> getTierCount(@PathVariable int year, @PathVariable int month) {
        List<TierCount> result = adminService.findTC(year,month);
        System.out.println(result);
        return ResponseEntity.ok(result);
    }
}
