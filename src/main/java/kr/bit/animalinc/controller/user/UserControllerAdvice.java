package kr.bit.animalinc.controller.user;

import kr.bit.animalinc.util.UserBannedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class UserControllerAdvice {

    @ExceptionHandler(UserBannedException.class)
    public ResponseEntity<Map<String, String>> handleUserBannedException(UserBannedException ex) {
        log.error("Handling UserBannedException: Ban reason - {}, Unlock date - {}", ex.getBanReason(), ex.getUnlockDate());
        Map<String, String> response = new HashMap<>();
        response.put("message", "현재 회원님의 계정은 이용이 정지되어 로그인할 수 없습니다.");
        response.put("banReason", ex.getBanReason());
        response.put("unlockDate", ex.getUnlockDate().toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
