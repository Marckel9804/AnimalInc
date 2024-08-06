package kr.bit.animalinc.entity.game.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockHistory {
    @Id
    String id;

    @ManyToOne
    @JoinColumn(name = "stockId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stock stock;

    private int year;

    private int month;
    private float weight;
}
