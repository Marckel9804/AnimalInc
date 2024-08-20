package kr.bit.animalinc.service;

import kr.bit.animalinc.entity.user.MemberRole;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import kr.bit.animalinc.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserServiceTest {

//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testInsertUser() {
//
//        Users user = new Users();
//
//        user.addRole(MemberRole.ADMIN);
//        user.addRole(MemberRole.USER);
//        user.setUserEmail("ch1234@naver.com");
//        user.setUserPw("1234");
//        user.setUserNickname("서창호");
//        user.setUserRealname("서창호");
//
//        Users result = userRepository.save(user);
//
//        System.out.println(result);
//
//
//    }

}
