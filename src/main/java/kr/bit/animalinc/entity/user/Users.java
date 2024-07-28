package kr.bit.animalinc.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "memRoleList")
@Getter
@Setter
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long user_num;

    private String userId;
    private String userRealname;
    private String userNickname;
    private String userPw;
    private String userPwConfirm;
    private String userPhone;
    private String userEmail;
    private Date userBirthdate;
    private int userPoint;
    private int userRuby;
    private String userGrade;
    private int userReportnum;
    private String userItem;
    private String socialProvider;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memRoleList = new ArrayList<>();
}