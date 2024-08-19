package kr.bit.animalinc.entity.game;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGameStockStatus is a Querydsl query type for GameStockStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGameStockStatus extends EntityPathBase<GameStockStatus> {

    private static final long serialVersionUID = 1709001457L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGameStockStatus gameStockStatus = new QGameStockStatus("gameStockStatus");

    public final QGameRoom gameRoom;

    public final StringPath id = createString("id");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath stockId = createString("stockId");

    public final NumberPath<Integer> turn = createNumber("turn", Integer.class);

    public final NumberPath<Float> weight = createNumber("weight", Float.class);

    public QGameStockStatus(String variable) {
        this(GameStockStatus.class, forVariable(variable), INITS);
    }

    public QGameStockStatus(Path<? extends GameStockStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGameStockStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGameStockStatus(PathMetadata metadata, PathInits inits) {
        this(GameStockStatus.class, metadata, inits);
    }

    public QGameStockStatus(Class<? extends GameStockStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gameRoom = inits.isInitialized("gameRoom") ? new QGameRoom(forProperty("gameRoom")) : null;
    }

}

