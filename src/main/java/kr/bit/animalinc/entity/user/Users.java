package kr.bit.animalinc.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.bit.animalinc.entity.board.BoardFAQ;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @Column(columnDefinition = "integer default 0")
    private int userReportnum; //회원이 신고받은 횟수
    private boolean slogin;

    private String platform;

    private String userPicture; //프로필 사진 URL

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memRoleList = new ArrayList<>();


    //여기서 부터
    @OneToMany(mappedBy = "qUser" , cascade = CascadeType.ALL)
    private List<BoardFAQ> boardFAQS;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserItem> userItems = new ArrayList<>();

    //회원으로 회원가입하면, users_memrolelist 테이블에 usernum과 권한이 1(USER)로 저장됩니다. 관리자(ADMIN)는 0으로 저장
    public void addRole(MemberRole role) {memRoleList.add(role);}

    public void addUserItem(Item item) { //유저가 보유한 아이템에 추가
        UserItem userItem = new UserItem();
        userItem.setUser(this);
        userItem.setItem(item);
        userItems.add(userItem);
    }

    public void removeUserItem(Item item) { //유저가 아이템을 사용했을 때 제거되도록
        userItems.removeIf(ui -> ui.getItem().equals(item));
    }
}
