package kr.bit.animalinc.entity.game;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGameUsersStatus is a Querydsl query type for GameUsersStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGameUsersStatus extends EntityPathBase<GameUsersStatus> {

    private static final long serialVersionUID = -1911266205L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGameUsersStatus gameUsersStatus = new QGameUsersStatus("gameUsersStatus");

    public final NumberPath<Integer> badNews = createNumber("badNews", Integer.class);

    public final NumberPath<Integer> cash = createNumber("cash", Integer.class);

    public final NumberPath<Integer> elec1 = createNumber("elec1", Integer.class);

    public final NumberPath<Integer> elec2 = createNumber("elec2", Integer.class);

    public final NumberPath<Integer> elec3 = createNumber("elec3", Integer.class);

    public final NumberPath<Integer> elec4 = createNumber("elec4", Integer.class);

    public final NumberPath<Integer> enter1 = createNumber("enter1", Integer.class);

    public final NumberPath<Integer> enter2 = createNumber("enter2", Integer.class);

    public final NumberPath<Integer> enter3 = createNumber("enter3", Integer.class);

    public final NumberPath<Integer> enter4 = createNumber("enter4", Integer.class);

    public final NumberPath<Integer> fakeNews = createNumber("fakeNews", Integer.class);

    public final NumberPath<Integer> food1 = createNumber("food1", Integer.class);

    public final NumberPath<Integer> food2 = createNumber("food2", Integer.class);

    public final NumberPath<Integer> food3 = createNumber("food3", Integer.class);

    public final NumberPath<Integer> food4 = createNumber("food4", Integer.class);

    public final QGameRoom gameRoom;

    public final NumberPath<Integer> goodNews = createNumber("goodNews", Integer.class);

    public final NumberPath<Integer> lottery = createNumber("lottery", Integer.class);

    public final NumberPath<Integer> newsCount = createNumber("newsCount", Integer.class);

    public final NumberPath<Integer> ship1 = createNumber("ship1", Integer.class);

    public final NumberPath<Integer> ship2 = createNumber("ship2", Integer.class);

    public final NumberPath<Integer> ship3 = createNumber("ship3", Integer.class);

    public final NumberPath<Integer> ship4 = createNumber("ship4", Integer.class);

    public final NumberPath<Integer> shortSelling = createNumber("shortSelling", Integer.class);

    public final NumberPath<Integer> tech1 = createNumber("tech1", Integer.class);

    public final NumberPath<Integer> tech2 = createNumber("tech2", Integer.class);

    public final NumberPath<Integer> tech3 = createNumber("tech3", Integer.class);

    public final NumberPath<Integer> tech4 = createNumber("tech4", Integer.class);

    public final NumberPath<Integer> timeMachine = createNumber("timeMachine", Integer.class);

    public final NumberPath<Long> userNum = createNumber("userNum", Long.class);

    public final NumberPath<Integer> worthInfo = createNumber("worthInfo", Integer.class);

    public QGameUsersStatus(String variable) {
        this(GameUsersStatus.class, forVariable(variable), INITS);
    }

    public QGameUsersStatus(Path<? extends GameUsersStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGameUsersStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGameUsersStatus(PathMetadata metadata, PathInits inits) {
        this(GameUsersStatus.class, metadata, inits);
    }

    public QGameUsersStatus(Class<? extends GameUsersStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gameRoom = inits.isInitialized("gameRoom") ? new QGameRoom(forProperty("gameRoom")) : null;
    }

}

