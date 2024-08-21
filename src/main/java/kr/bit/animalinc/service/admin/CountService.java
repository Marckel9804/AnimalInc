package kr.bit.animalinc.service.admin;

import kr.bit.animalinc.dto.admin.CountDTO;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    public boolean increaseUserCount(Long userNum) {
        Users user = userRepository.findById(userNum).orElse(null);
        if (user == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        String userVisitKey = "user:visit:" + today.toString(); // 날짜별로 키를 생성
        String userId = user.getUserNum().toString(); // 사용자 ID를 문자열로 변환
        String todayVisitsKey = "site:visits:" + today.toString();

        // 사용자가 오늘 이미 집계되었는지 확인
        Boolean isMember = redisTemplate.opsForSet().isMember(userVisitKey, userId);

        if (Boolean.FALSE.equals(isMember)) {
            // Redis에서 오늘 날짜의 방문자 수 증가
            redisTemplate.opsForValue().increment(todayVisitsKey);

            // 오늘 날짜의 방문자 SET에 사용자 ID 추가
            redisTemplate.opsForSet().add(userVisitKey, userId);

            // 만료 시간 설정: 하루 뒤에 만료되도록 설정 (선택 사항)
            redisTemplate.expire(userVisitKey, Duration.ofDays(1));
        }

        // 오늘의 총 방문자 수를 가져옴
        String visits = redisTemplate.opsForValue().get(todayVisitsKey);

        System.out.println("Today's visit count: " + visits);

        return true;
    }

    public CountDTO getUserCountDTO(LocalDate date) {

        String todayVisitsKey = "site:visits:" + date.toString();
        String visits = redisTemplate.opsForValue().get(todayVisitsKey);
        if(visits == null){
            visits = "0";
        }
        return new CountDTO(date, Integer.parseInt(visits));
    }

    // 월별 사용자수 출력 출력
    public List<CountDTO> getUCByYearMonth(int year, int month) {

        List<CountDTO> userCounts = new ArrayList<>();

        // 해당 연도와 월의 첫 번째 날을 계산합니다.
        LocalDate startDate = LocalDate.of(year, month, 1);

        // 해당 연도와 월의 마지막 날을 계산합니다.
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 날짜 범위를 반복하면서 각 날짜에 대해 레디스 키를 확인합니다.
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String todayVisitsKey = "site:visits:" + date.toString();
            String visits = redisTemplate.opsForValue().get(todayVisitsKey);

            // visits 값이 null이면 "0"으로 설정합니다.
            if (visits == null) {
                visits = "0";
            }

            // UserCountDTO 객체를 생성하고 리스트에 추가합니다.
            userCounts.add(new CountDTO(date, Integer.parseInt(visits)));
        }

        return userCounts;
    }

    // 신고수 출력
    public List<CountDTO> getRCByYearMonth(int year, int month) {
        List<CountDTO> reportCounts = new ArrayList<>();

        // 해당 연도와 월의 첫째 날과 마지막 날을 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String todayreportsKey = "site:reports:" + date.toString();
            String reports = redisTemplate.opsForValue().get(todayreportsKey);

            if (reports == null) { reports = "0"; }

            reportCounts.add(new CountDTO(date, Integer.parseInt(reports)));
        }

        return reportCounts;
    }

    // 티어수 출력
    public List<CountDTO> getTCByYearMonth(int year, int month) {
        List<CountDTO> tierCounts = new ArrayList<>();

        // 해당 연도와 월의 첫째 날과 마지막 날을 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String todaytiersKey = "site:tiers:" + date.toString();
            String tiers = redisTemplate.opsForValue().get(todaytiersKey);

            if (tiers == null) { tiers = "0"; }

            tierCounts.add(new CountDTO(date, Integer.parseInt(tiers)));
        }

        return tierCounts;
    }
}
