package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
