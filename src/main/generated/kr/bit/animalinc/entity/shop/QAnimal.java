package kr.bit.animalinc.entity.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnimal is a Querydsl query type for Animal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnimal extends EntityPathBase<Animal> {

    private static final long serialVersionUID = -1503995779L;

    public static final QAnimal animal = new QAnimal("animal");

    public final NumberPath<Integer> animalCatalogNumber = createNumber("animalCatalogNumber", Integer.class);

    public final StringPath animalDescription = createString("animalDescription");

    public final NumberPath<Integer> animalId = createNumber("animalId", Integer.class);

    public final StringPath animalImage = createString("animalImage");

    public final StringPath animalName = createString("animalName");

    public final NumberPath<Double> animalProbability = createNumber("animalProbability", Double.class);

    public final NumberPath<Integer> userRuby = createNumber("userRuby", Integer.class);

    public QAnimal(String variable) {
        super(Animal.class, forVariable(variable));
    }

    public QAnimal(Path<? extends Animal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnimal(PathMetadata metadata) {
        super(Animal.class, metadata);
    }

}

