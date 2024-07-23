package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean isPasswordMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    public boolean isUsernameExist(String username) {
        return userRepository.findbyUsername(username) != null;
    }

    public Users findByIdAndPassword(Long id, String password) {
        return userRepository.findByIdAndPassword(id, password);
    }

    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    public Users deleteUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
