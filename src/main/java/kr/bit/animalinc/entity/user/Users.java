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
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long user_num;

    private String user_id;
    private String user_realname;
    private String user_nickname;
    private String user_pw;
    private String user_pwConfirm;
    private String user_phone;
    private String user_email;
    private Date user_birthdate;
    private int user_point;
    private int user_ruby;
    private String user_grade;
    private int user_reportnum;
    private String user_item;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memRoleList = new ArrayList<>();

}
