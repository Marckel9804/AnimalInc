package kr.bit.animalinc.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanList is a Querydsl query type for BanList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanList extends EntityPathBase<BanList> {

    private static final long serialVersionUID = -177263689L;

    public static final QBanList banList = new QBanList("banList");

    public final NumberPath<Long> banId = createNumber("banId", Long.class);

    public final DateTimePath<java.util.Date> bannedDate = createDateTime("bannedDate", java.util.Date.class);

    public final StringPath banReason = createString("banReason");

    public final DateTimePath<java.util.Date> unlockDate = createDateTime("unlockDate", java.util.Date.class);

    public final NumberPath<Long> userNum = createNumber("userNum", Long.class);

    public QBanList(String variable) {
        super(BanList.class, forVariable(variable));
    }

    public QBanList(Path<? extends BanList> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanList(PathMetadata metadata) {
        super(BanList.class, metadata);
    }

}

