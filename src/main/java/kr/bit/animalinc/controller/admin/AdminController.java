package kr.bit.animalinc.controller.admin;

import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserList() {
        List<Users> usersList = userService.findAll();

        return ResponseEntity.ok(usersList);
    }

}
