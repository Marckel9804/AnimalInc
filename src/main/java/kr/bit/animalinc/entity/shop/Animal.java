package kr.bit.animalinc.entity.shop;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "aniamal") // 테이블 이름 명시
public class Animal {
    @Id
    private int animalId;
    private String animalName;
    private String animalDescription;
    private double animalProbability;
    private String animalImage;
    private int animalCatalogNumber;
    private int userRuby;

    public int getAnimalId() { // 반환 타입을 int로 변경
        return animalId;
    }
}
