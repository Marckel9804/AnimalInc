package kr.bit.animalinc.entity.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardFAQ is a Querydsl query type for BoardFAQ
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFAQ extends EntityPathBase<BoardFAQ> {

    private static final long serialVersionUID = -11911313L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardFAQ boardFAQ = new QBoardFAQ("boardFAQ");

    public final StringPath adminUserNum = createString("adminUserNum");

    public final StringPath code = createString("code");

    public final StringPath content = createString("content");

    public final NumberPath<Long> faq_id = createNumber("faq_id", Long.class);

    public final kr.bit.animalinc.entity.user.QUsers qUser;

    public final DateTimePath<java.util.Date> reportDate = createDateTime("reportDate", java.util.Date.class);

    public final StringPath reportUserNum = createString("reportUserNum");

    public final StringPath title = createString("title");

    public QBoardFAQ(String variable) {
        this(BoardFAQ.class, forVariable(variable), INITS);
    }

    public QBoardFAQ(Path<? extends BoardFAQ> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardFAQ(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardFAQ(PathMetadata metadata, PathInits inits) {
        this(BoardFAQ.class, metadata, inits);
    }

    public QBoardFAQ(Class<? extends BoardFAQ> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.qUser = inits.isInitialized("qUser") ? new kr.bit.animalinc.entity.user.QUsers(forProperty("qUser"), inits.get("qUser")) : null;
    }

}

