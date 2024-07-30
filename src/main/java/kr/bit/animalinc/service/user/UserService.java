package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Users register(Users user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        return userRepository.save(user);
    }


}
