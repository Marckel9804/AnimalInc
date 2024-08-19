package kr.bit.animalinc.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTierCount is a Querydsl query type for TierCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTierCount extends EntityPathBase<TierCount> {

    private static final long serialVersionUID = 1691558967L;

    public static final QTierCount tierCount = new QTierCount("tierCount");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final StringPath tier = createString("tier");

    public final DateTimePath<java.util.Date> tuDate = createDateTime("tuDate", java.util.Date.class);

    public final NumberPath<Long> tuId = createNumber("tuId", Long.class);

    public QTierCount(String variable) {
        super(TierCount.class, forVariable(variable));
    }

    public QTierCount(Path<? extends TierCount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTierCount(PathMetadata metadata) {
        super(TierCount.class, metadata);
    }

}

