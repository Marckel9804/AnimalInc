package kr.bit.animalinc.entity.game.stock;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStockHistory is a Querydsl query type for StockHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStockHistory extends EntityPathBase<StockHistory> {

    private static final long serialVersionUID = 437200635L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStockHistory stockHistory = new QStockHistory("stockHistory");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final QStock stock;

    public final NumberPath<Float> weight = createNumber("weight", Float.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QStockHistory(String variable) {
        this(StockHistory.class, forVariable(variable), INITS);
    }

    public QStockHistory(Path<? extends StockHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStockHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStockHistory(PathMetadata metadata, PathInits inits) {
        this(StockHistory.class, metadata, inits);
    }

    public QStockHistory(Class<? extends StockHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stock = inits.isInitialized("stock") ? new QStock(forProperty("stock")) : null;
    }

}

