package kr.bit.animalinc.dto.game;

import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LadderGameDTO {

    private Users users;

    public LadderGameDTO toLadderGameDTO(Users users) {
        return LadderGameDTO.builder()
                .users(users)
                .build();
    }

}
