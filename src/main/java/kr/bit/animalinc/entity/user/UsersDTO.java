package kr.bit.animalinc.entity.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class UsersDTO extends User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long userNum;

    private String userEmail;
    private String userRealname;
    private String userNickname;
    private String userPw;
    private LocalDate userBirthdate;
    private int userPoint; //회원이 가지고 있는 포인트
    private int userRuby; //회원이 가지고 있는 루비(캐시)
    private String userGrade; //회원의 등급(티어)
    private int userReportnum; //회원이 신고받은 횟수
    private String userItem; //회원이 가지고 있는 아이템
    private boolean slogin;

    private List<String> roleName; //회원 역할(관리자와 회원 권한을 구분하기 위해서)

    public UsersDTO(String userEmail, String userRealname, String userNickname, boolean slogin, List<String> roleName) {
        super(userEmail != null ? userEmail : "", userRealname != null ? userRealname : "",
                roleName.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));

        this.userEmail = userEmail != null ? userEmail : "";
        this.userRealname = userRealname != null ? userRealname : "";
        this.userNickname = userNickname != null ? userNickname : "";
        this.slogin = slogin;
        this.roleName = roleName != null ? roleName : List.of();
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
//        map.put("userNum", userNum); // 가차상점추가
        map.put("userEmail", userEmail);
        map.put("userRealname", userRealname);
        map.put("userNickname", userNickname);
        map.put("slogin", slogin);
        map.put("roleName", roleName);
        return map;
    }
}