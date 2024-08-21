package kr.bit.animalinc.service.admin;

import kr.bit.animalinc.entity.admin.BanList;
import kr.bit.animalinc.entity.admin.TierCount;
import kr.bit.animalinc.entity.admin.UserCount;
import kr.bit.animalinc.repository.admin.BanListRepository;
import kr.bit.animalinc.repository.admin.TierCountRepository;
import kr.bit.animalinc.repository.admin.UserCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final BanListRepository banListRepository;
    private final TierCountRepository tierCountRepository;
    private final UserCountRepository userCountRepository;

    // 유저 Ban에 관한 CRUD
    @Transactional
    public BanList addBanList(BanList banList) {
        log.warn("\naddBanList : " + banList.toString());
        return banListRepository.save(banList);
    }

    @Transactional
    public List<BanList> getAllBanLists() {
        checkAndUpdateBanStatus();

        List<BanList> banLists = banListRepository.findAll();
        String result = "\nBan List : \n\t"
                + banLists.stream()
                .map(BanList::toString)
                .collect(Collectors.joining("\n\t"));

        log.info(result);
        return banLists;
    }

    @Transactional
    public BanList getBanList(Long user_num) {
        BanList banList = banListRepository.findByUserNum(user_num)
                .orElse(null);  // Optional이 비어 있을 경우 null을 반환

        if (banList != null && isBanExpired(banList)) {
            banListRepository.delete(banList);
            return null;
        }

        return banList;
    }


    @Transactional
    public BanList updateBanList(BanList banList) {
        Optional<BanList> target = banListRepository.findByUserNum(banList.getUserNum());

        if (target.isPresent()) {
            BanList existingBanList = target.get();
            existingBanList.setBanReason(banList.getBanReason());
            existingBanList.setUnlockDate(banList.getUnlockDate());
            return banListRepository.save(existingBanList);
        } else {
            throw new RuntimeException("BanList entry not found for user_num: " + banList.getUserNum());
        }
    }

    @Transactional
    public String deleteBanList(Long user_num) {
        Optional<BanList> target = banListRepository.findByUserNum(user_num);

        if (target.isPresent()) {
            banListRepository.delete(target.get());
            return "success";
        } else {
            return "BanList entry not found";
        }
    }


    // TierCount에 대한 CRUD
    @Transactional
    public TierCount addTierCount(TierCount tierCount) {
        log.warn("\ninsert TierCount : " + tierCount);
        TierCount input = TierCount.builder()
                .count(tierCount.getCount())
                .tier(tierCount.getTier())
                .tuDate(tierCount.getTuDate())
                .build();

        return tierCountRepository.save(input);
    }

    @Transactional
    public TierCount getTierCount(Date date) {
        TierCount result = tierCountRepository.findByTuDate(date);
        log.info("\nresult TierCount : " + result);
        return result;
    }
    @Transactional
    public List<TierCount> getAllTierCount() {
        List<TierCount> result = tierCountRepository.findAll();
        return result;
    }

    @Transactional
    public TierCount updateTierCount(TierCount tierCount) {

        TierCount target = tierCountRepository.findByTuDate(tierCount.getTuDate());
        target.setCount(tierCount.getCount());
        target.setTier(tierCount.getTier());

        TierCount result = tierCountRepository.save(target);
        return result;
    }

    @Transactional
    public String deleteTierCount(Long id) {
        tierCountRepository.deleteById(id);
        return "success";
    }

    // UserCount에 대한 CRUD

    @Transactional
    public UserCount addUserCount(UserCount userCount) {
        UserCount input = UserCount.builder()
                .count(userCount.getCount())
                .cuDate(userCount.getCuDate())
                .build();
        UserCount result = userCountRepository.save(input);
        return result;
    }

    @Transactional
    public UserCount getUserCount(Date date) {
        UserCount result = userCountRepository.findByCuDate(date);
        return result;
    }

    @Transactional
    public List<UserCount> getAllUserCount() {
        List<UserCount> result = userCountRepository.findAll();
        return result;
    }

    @Transactional
    public UserCount updateUserCount(UserCount userCount) {
        UserCount target = userCountRepository.findByCuDate(userCount.getCuDate());
        target.setCount(userCount.getCount());
        UserCount result = userCountRepository.save(target);
        return result;
    }

    @Transactional
    public String deleteUserCount(Long id) {
        userCountRepository.deleteById(id);
        return "success";
    }

    @Transactional
    public List<UserCount> findUSByMonth(int Month) {
        List<UserCount> result = userCountRepository.findByMonth(Month);
        return result;
    }

    @Transactional
    public List<UserCount> findUS(int year,int month) {
        List<UserCount> result = userCountRepository.findByYearAndMonth(year,month);
        return result;
    }

    @Transactional
    public List<TierCount> findTC(int year,int month) {
        List<TierCount> result = tierCountRepository.findByYearAndMonth(year,month);
        return result;
    }

    @Transactional
    public void checkAndUpdateBanStatus() {
        List<BanList> banLists = banListRepository.findAll();
        for (BanList banList : banLists) {
            if (isBanExpired(banList)) {
                banListRepository.delete(banList);
            }
        }
    }

    private boolean isBanExpired(BanList banList) {
        return banList.getUnlockDate().before(new Date());
    }
}
