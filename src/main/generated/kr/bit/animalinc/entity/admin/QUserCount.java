package kr.bit.animalinc.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserCount is a Querydsl query type for UserCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCount extends EntityPathBase<UserCount> {

    private static final long serialVersionUID = 132338670L;

    public static final QUserCount userCount = new QUserCount("userCount");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final DateTimePath<java.util.Date> cuDate = createDateTime("cuDate", java.util.Date.class);

    public final NumberPath<Long> cuId = createNumber("cuId", Long.class);

    public QUserCount(String variable) {
        super(UserCount.class, forVariable(variable));
    }

    public QUserCount(Path<? extends UserCount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserCount(PathMetadata metadata) {
        super(UserCount.class, metadata);
    }

}

