package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Users login(String userId, String password) {
        Optional<Users> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getUserPw())) {
                return user;
            }
        }
        return null;
    }

    public Users register(String userId, String password, String name, String email, String birthdate) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            return null; // 이미 존재하는 사용자
        }

        Users newUser = Users.builder()
                .userId(userId)
                .userPw(passwordEncoder.encode(password))
                .userRealname(name)
                .userEmail(email)
                .userBirthdate(Date.valueOf(birthdate))
                .build();

        return userRepository.save(newUser);
    }

    public Users handleSocialLogin(String socialId, String platform, String name, String email) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            Users newUser = Users.builder()
                    .userEmail(email)
                    .socialProvider(platform)
                    .userRealname(name)
                    .build();
            return userRepository.save(newUser);
        }
    }
}
