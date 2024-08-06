package kr.bit.animalinc.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString(exclude = "memRoleList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users{
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

    private String platform;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memRoleList = new ArrayList<>();

    //회원으로 회원가입하면, users_memrolelist 테이블에 usernum과 권한이 1(USER)로 저장됩니다. 관리자(ADMIN)는 0으로 저장
    public void addRole(MemberRole role) {memRoleList.add(role);}
}