package kr.bit.animalinc.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserItem is a Querydsl query type for UserItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserItem extends EntityPathBase<UserItem> {

    private static final long serialVersionUID = -472794486L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserItem userItem = new QUserItem("userItem");

    public final QItem item;

    public final QUsers user;

    public final NumberPath<Long> userItemId = createNumber("userItemId", Long.class);

    public QUserItem(String variable) {
        this(UserItem.class, forVariable(variable), INITS);
    }

    public QUserItem(Path<? extends UserItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserItem(PathMetadata metadata, PathInits inits) {
        this(UserItem.class, metadata, inits);
    }

    public QUserItem(Class<? extends UserItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item")) : null;
        this.user = inits.isInitialized("user") ? new QUsers(forProperty("user"), inits.get("user")) : null;
    }

}

