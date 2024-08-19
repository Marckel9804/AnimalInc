package kr.bit.animalinc.entity.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardImage is a Querydsl query type for BoardImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardImage extends EntityPathBase<BoardImage> {

    private static final long serialVersionUID = 1442230132L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardImage boardImage = new QBoardImage("boardImage");

    public final NumberPath<Integer> bcimgId = createNumber("bcimgId", Integer.class);

    public final QBoardCommunity boardCommunity;

    public final StringPath imgName = createString("imgName");

    public QBoardImage(String variable) {
        this(BoardImage.class, forVariable(variable), INITS);
    }

    public QBoardImage(Path<? extends BoardImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardImage(PathMetadata metadata, PathInits inits) {
        this(BoardImage.class, metadata, inits);
    }

    public QBoardImage(Class<? extends BoardImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boardCommunity = inits.isInitialized("boardCommunity") ? new QBoardCommunity(forProperty("boardCommunity")) : null;
    }

}

