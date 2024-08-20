package kr.bit.animalinc.entity.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cuId;

    private Date cuDate;
    private int count;

}
