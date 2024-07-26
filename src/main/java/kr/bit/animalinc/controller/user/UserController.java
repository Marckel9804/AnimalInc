package kr.bit.animalinc.controller.user;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

}
