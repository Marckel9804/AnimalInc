package kr.bit.animalinc.service.game;

import kr.bit.animalinc.dto.game.LadderGameDTO;
import kr.bit.animalinc.entity.game.GameRoom;
import kr.bit.animalinc.entity.game.GameUsersStatus;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.game.GameRoomRepository;
import kr.bit.animalinc.repository.game.GameUsersStatusRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LadderGameService {

    @Autowired
    private GameRoomRepository gameRoomRepository;
    @Autowired
    private GameUsersStatusRepository gameUsersStatusRepository;
    @Autowired
    private UserRepository userRepository;

    // 게임방 정보로 참가자들 정보를 받아온다.
    public List<LadderGameDTO> getParticipants(String roomId) {
        // 1. 게임방 번호로 game users status 조회
        Optional<GameRoom> gameRoom = gameRoomRepository.findById(roomId);
        List<GameUsersStatus> gameUsersStatuses = null;
        if (gameRoom.isPresent()) {
            log.info("gameroom 정보 ! {}", gameRoom.get());
            gameUsersStatuses = gameUsersStatusRepository.findByGameRoom(gameRoom.get());
        }
        log.info("gameUsersStatuses 크기: {}", gameUsersStatuses.size());
        // 2. userNum 으로 users 조회
        List<Users> users = new ArrayList<>();
        for (GameUsersStatus status : gameUsersStatuses) {
            Optional<Users> getUser = userRepository.findById(status.getUserNum());
            if(getUser.isPresent()) {
                log.info("user 가 없다구여? {}", getUser.get());
                users.add(getUser.get());
            } else {
                log.warn("User not found for userNum: {}", status.getUserNum());
            }
        }
        // 3. Entity 를 DTO 로 변환 후 리턴
        List<LadderGameDTO> ladderGameDTOList = new ArrayList<>();
        for (Users user : users) {
            ladderGameDTOList.add(new LadderGameDTO().toLadderGameDTO(user));
        }
        return ladderGameDTOList;
    }

}
