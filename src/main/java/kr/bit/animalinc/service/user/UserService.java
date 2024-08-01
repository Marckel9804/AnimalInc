package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public Users login(String userEmail, String password) {
        log.info("Attempting login for email: {}", userEmail);
        Optional<Users> optionalUser = userRepository.findByUserEmail(userEmail);
        log.info("User repository call complete");
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            log.info("User found in database: {}", user.getUserEmail());
            log.info("Comparing passwords: input={}, stored={}", password, user.getUserPw());
            if (passwordEncoder.matches(password, user.getUserPw())) {
                log.info("Password match for user: {}", userEmail);
                return user;
            } else {
                log.info("Password mismatch for user: {}", userEmail);
            }
        } else {
            log.info("No user found in database for email: {}", userEmail);
        }
        return null;
    }

    public Users register(Users user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        return userRepository.save(user);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.findByUserEmail(email).isPresent();
    }

    public boolean checkNickname(String nickname) {
        boolean exists = userRepository.findByUserNickname(nickname).isPresent();
        return !exists;
    }

    public Users socialLogin(String platform, String name, String email, String birthdate) {
        log.info("Attempting social login for email: {}", email);
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);

        LocalDate birthDateParsed = LocalDate.parse(birthdate); // birthdate를 LocalDate로 파싱


        Users user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            // 신규 사용자 등록
            user = new Users();
            user.setUserEmail(email);
            user.setUserRealname(name);
            user.setUserBirthdate(birthDateParsed);
            user.setSlogin(true); // 소셜 로그인 상태 설정
            user.setUserNickname(null); // 초기에는 닉네임이 없음
            user = userRepository.save(user);
            log.info("New user registered with email: {}", email);
        }

        if (user.isSlogin()) {
            log.info("User already logged in, logging out existing session: {}", email);
            user.setSlogin(false); // 기존 세션 로그아웃 처리
        }

        user.setSlogin(true); // 새로운 로그인 세션 설정
        userRepository.save(user);
        log.info("Social login successful for user: {}", email);
        return user;
    }

    public void logout(String userEmail) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(userEmail);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setSlogin(false);
            userRepository.save(user);
            log.info("User logged out: {}", userEmail);
        } else {
            log.warn("User not found for email: {}", userEmail);
        }
    }
}
