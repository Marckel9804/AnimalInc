package kr.bit.animalinc.entity.game;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGameRoom is a Querydsl query type for GameRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGameRoom extends EntityPathBase<GameRoom> {

    private static final long serialVersionUID = 872911026L;

    public static final QGameRoom gameRoom = new QGameRoom("gameRoom");

    public final NumberPath<Integer> bots = createNumber("bots", Integer.class);

    public final StringPath gameRoomId = createString("gameRoomId");

    public final DateTimePath<java.util.Date> gameTime = createDateTime("gameTime", java.util.Date.class);

    public final NumberPath<Integer> players = createNumber("players", Integer.class);

    public final StringPath roomName = createString("roomName");

    public final StringPath tier = createString("tier");

    public final NumberPath<Integer> turn = createNumber("turn", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QGameRoom(String variable) {
        super(GameRoom.class, forVariable(variable));
    }

    public QGameRoom(Path<? extends GameRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGameRoom(PathMetadata metadata) {
        super(GameRoom.class, metadata);
    }

}

