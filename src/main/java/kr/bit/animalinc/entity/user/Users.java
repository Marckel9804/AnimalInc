package kr.bit.animalinc.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class Users extends User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long userNum;

    private String userId;
    private String userRealname;
    private String userNickname;
    private String userPw;
    private String userEmail;
    private Date userBirthdate;
    private int userPoint; //회원이 가지고 있는 포인트
    private int userRuby; //회원이 가지고 있는 루비(캐시)
    private String userGrade; //회원의 등급(티어)
    private int userReportnum; //회원이 신고받은 횟수
    private String userItem; //회원이 가지고 있는 아이템
    private boolean slogin;

    private List<String> roleName = new ArrayList<>(); //회원 역할(관리자와 회원 권한을 구분하기 위해서)

    public Users(String userEmail, String userPw, String userNickname, boolean slogin
            , List<String> roleName){

        super(userEmail, userPw, roleName.stream().map(str ->
                new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));

        this.userEmail=userEmail;
        this.userPw=userPw;
        this.userNickname=userNickname;
        this.slogin=slogin;
        this.roleName=roleName;
    }

    public Map<String, Object> getClaims(){

        Map<String, Object> map=new HashMap<>();
        map.put("email",userEmail);
        map.put("password",userPw);
        map.put("nickname",userNickname);
        map.put("slogin",slogin);
        map.put("roleName",roleName);
        return map;
    }
}