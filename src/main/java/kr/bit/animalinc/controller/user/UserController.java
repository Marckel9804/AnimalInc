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
//    private final UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<Users> registerUser(Users user) {
//        return ResponseEntity.ok(userService.saveUser(user));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Users> loginUser(Long id, String password) {
//        return ResponseEntity.ok(userService.findByIdAndPassword(id, password));
//    }
//
//    @PutMapping("/logout")
//    public ResponseEntity<Void> logoutUser(Long id) {
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{id}")
//    public ResponseEntity<Users> updateUser(@RequestBody Users updatedUser) {
//        return ResponseEntity.ok(userService.updateUser(updatedUser));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
}
