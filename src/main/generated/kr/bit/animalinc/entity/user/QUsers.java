package kr.bit.animalinc.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = 2003946620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsers users = new QUsers("users");

    public final ListPath<kr.bit.animalinc.entity.board.BoardFAQ, kr.bit.animalinc.entity.board.QBoardFAQ> boardFAQS = this.<kr.bit.animalinc.entity.board.BoardFAQ, kr.bit.animalinc.entity.board.QBoardFAQ>createList("boardFAQS", kr.bit.animalinc.entity.board.BoardFAQ.class, kr.bit.animalinc.entity.board.QBoardFAQ.class, PathInits.DIRECT2);

    public final kr.bit.animalinc.entity.shop.QAnimal lastGachaResult;

    public final ListPath<MemberRole, EnumPath<MemberRole>> memRoleList = this.<MemberRole, EnumPath<MemberRole>>createList("memRoleList", MemberRole.class, EnumPath.class, PathInits.DIRECT2);

    public final ListPath<kr.bit.animalinc.entity.shop.Animal, kr.bit.animalinc.entity.shop.QAnimal> ownedAnimals = this.<kr.bit.animalinc.entity.shop.Animal, kr.bit.animalinc.entity.shop.QAnimal>createList("ownedAnimals", kr.bit.animalinc.entity.shop.Animal.class, kr.bit.animalinc.entity.shop.QAnimal.class, PathInits.DIRECT2);

    public final StringPath platform = createString("platform");

    public final kr.bit.animalinc.entity.shop.QAnimal selectedAnimal;

    public final BooleanPath slogin = createBoolean("slogin");

    public final DatePath<java.time.LocalDate> userBirthdate = createDate("userBirthdate", java.time.LocalDate.class);

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userGrade = createString("userGrade");

    public final ListPath<UserItem, QUserItem> userItems = this.<UserItem, QUserItem>createList("userItems", UserItem.class, QUserItem.class, PathInits.DIRECT2);

    public final StringPath userNickname = createString("userNickname");

    public final NumberPath<Long> userNum = createNumber("userNum", Long.class);

    public final StringPath userPicture = createString("userPicture");

    public final NumberPath<Integer> userPoint = createNumber("userPoint", Integer.class);

    public final StringPath userPw = createString("userPw");

    public final StringPath userRealname = createString("userRealname");

    public final NumberPath<Integer> userReportnum = createNumber("userReportnum", Integer.class);

    public final NumberPath<Integer> userRuby = createNumber("userRuby", Integer.class);

    public QUsers(String variable) {
        this(Users.class, forVariable(variable), INITS);
    }

    public QUsers(Path<? extends Users> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsers(PathMetadata metadata, PathInits inits) {
        this(Users.class, metadata, inits);
    }

    public QUsers(Class<? extends Users> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lastGachaResult = inits.isInitialized("lastGachaResult") ? new kr.bit.animalinc.entity.shop.QAnimal(forProperty("lastGachaResult")) : null;
        this.selectedAnimal = inits.isInitialized("selectedAnimal") ? new kr.bit.animalinc.entity.shop.QAnimal(forProperty("selectedAnimal")) : null;
    }

}

