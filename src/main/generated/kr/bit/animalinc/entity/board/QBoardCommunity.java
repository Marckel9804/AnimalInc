package kr.bit.animalinc.entity.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardCommunity is a Querydsl query type for BoardCommunity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardCommunity extends EntityPathBase<BoardCommunity> {

    private static final long serialVersionUID = -273776254L;

    public static final QBoardCommunity boardCommunity = new QBoardCommunity("boardCommunity");

    public final StringPath bcCode = createString("bcCode");

    public final NumberPath<Long> bcId = createNumber("bcId", Long.class);

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final ListPath<String, StringPath> files = this.<String, StringPath>createList("files", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final StringPath userEmail = createString("userEmail");

    public final StringPath writeDate = createString("writeDate");

    public QBoardCommunity(String variable) {
        super(BoardCommunity.class, forVariable(variable));
    }

    public QBoardCommunity(Path<? extends BoardCommunity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardCommunity(PathMetadata metadata) {
        super(BoardCommunity.class, metadata);
    }

}

