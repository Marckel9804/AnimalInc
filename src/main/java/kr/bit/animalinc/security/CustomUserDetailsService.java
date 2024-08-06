package kr.bit.animalinc.security;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.entity.user.UsersDTO;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepository.getRole(username);  //회원정보+권한정보

        if (user == null) {
            throw new UsernameNotFoundException("User Not Found!");
        }

        UsersDTO usersDTO = new UsersDTO(
                user.getUserEmail(),
                user.getUserPw(),
                user.getUserNickname(),
                user.isSlogin(),
                user.getMemRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()),
                user.getUserGrade(),
                user.getUserPoint()
        );

        log.info(String.valueOf(usersDTO));
        return usersDTO;
    }
}
