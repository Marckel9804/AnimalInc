package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.user.MemberRole;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Users login(String email, String password) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            // 일반 로그인 여부 확인
            if (user.getPlatform() == null || "default".equals(user.getPlatform())) {
                // 비밀번호 일치 여부 확인
                if (passwordEncoder.matches(password, user.getUserPw())) {
                    return user;
                }
            }
        }
        return null;
    }

    public Users register(Users user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));

        //USER 권한 설정
        user.addRole(MemberRole.USER);
        user.setPlatform("default"); // 플랫폼을 "default"로 설정

        return userRepository.save(user);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.findByUserEmail(email).isPresent();
    }

    public boolean checkNickname(String nickname) {
        boolean exists = userRepository.findByUserNickname(nickname).isPresent();
        return !exists;
    }


    @Transactional
    public Users socialLogin(String name, String email, String platform) {
        log.info("Attempting social login for email: {}", email);
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);

        Users user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (!user.getPlatform().equals(platform)) {
                throw new IllegalStateException("이미 해당 이메일로 " + getRegisteredMethod(email) + "에서 가입하셨습니다.");
            }
            log.info("User already exists with email: {}", email);
        } else {
            synchronized (this) {
                optionalUser = userRepository.findByUserEmail(email);
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                    if (!user.getPlatform().equals(platform)) {
                        throw new IllegalStateException("이미 해당 이메일로 " + getRegisteredMethod(email) + "에서 가입하셨습니다.");
                    }
                    log.info("User already exists with email: {}", email);
                } else {
                    user = new Users();
                    user.setUserEmail(email);
                    user.setUserRealname(name);
                    user.setSlogin(true);
                    user.setPlatform(platform); // 소셜 로그인 플랫폼 설정
                    user.setUserNickname(null);
                    user.addRole(MemberRole.USER);
                    user = userRepository.save(user);
                    log.info("New user registered with email: {}", email);
                }
            }
        }

        if (user.isSlogin()) {
            log.info("User already logged in, logging out existing session: {}", email);
            user.setSlogin(false);
        }

        user.setSlogin(true);
        userRepository.save(user);
        log.info("Social login successful for user: {}", email);
        return user;
    }

    public String getRegisteredMethod(String email) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (user.isSlogin()) {
                String userPlatform = user.getPlatform();
                if (userPlatform.equals("Naver"))
                    return "네이버";
                else if (userPlatform.equals("Kakao"))
                    return "카카오";
                else if (userPlatform.equals("Google"))
                    return "구글";
                else
                    return "다른 방법";
            } else {
                return "일반 로그인";
            }
        }
        return "알 수 없음";
    }

    @Transactional
    public Users completeProfile(String email, String birthdate, String nickname) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setUserBirthdate(LocalDate.parse(birthdate));
            user.setUserNickname(nickname);
            return userRepository.save(user);
        } else {
            throw new IllegalStateException("User not found");
        }


    }

// 새로운 메서드 추가
@Transactional
public Users findByEmail(String email) {
    return userRepository.findByUserEmail(email).orElse(null);
}
}


