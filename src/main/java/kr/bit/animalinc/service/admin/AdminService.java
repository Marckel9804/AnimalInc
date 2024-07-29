package kr.bit.animalinc.service.admin;

import kr.bit.animalinc.entity.admin.BanList;
import kr.bit.animalinc.entity.admin.TierCount;
import kr.bit.animalinc.entity.admin.UserCount;
import kr.bit.animalinc.repository.admin.BanListRepository;
import kr.bit.animalinc.repository.admin.TierCountRepository;
import kr.bit.animalinc.repository.admin.UserCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
        BanList result = banListRepository.findByUsernum(user_num);
        return result;
    }

    @Transactional
    public BanList updateBanList(BanList banList) {
        BanList result = banListRepository.save(banList);
        return result;
    }

    @Transactional
    public String deleteBanList(Long user_num) {
        BanList target = banListRepository.findByUsernum(user_num);
        banListRepository.delete(target);
        return "success";
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
}
