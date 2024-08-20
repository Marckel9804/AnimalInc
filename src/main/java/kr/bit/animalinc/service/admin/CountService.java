package kr.bit.animalinc.service.admin;

import kr.bit.animalinc.dto.admin.UserCountDTO;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    public UserCountDTO getUserCountDTO(LocalDate date) {

        String todayVisitsKey = "site:visits:" + date.toString();
        String visits = redisTemplate.opsForValue().get(todayVisitsKey);
        if(visits == null){
            visits = "0";
        }
        return new UserCountDTO(date, Integer.parseInt(visits));
    }

    public List<UserCountDTO> getUCByYearMonth(int year, int month) {

        List<UserCountDTO> userCounts = new ArrayList<>();

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
            userCounts.add(new UserCountDTO(date, Integer.parseInt(visits)));
        }

        return userCounts;
    }
}
