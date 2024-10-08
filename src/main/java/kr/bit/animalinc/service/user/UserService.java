package kr.bit.animalinc.service.user;

import kr.bit.animalinc.entity.admin.BanList;
import kr.bit.animalinc.entity.shop.Animal;
import kr.bit.animalinc.entity.user.*;
import kr.bit.animalinc.repository.admin.BanListRepository;
import kr.bit.animalinc.repository.shop.AnimalRepository;
import kr.bit.animalinc.repository.shop.ItemRepository;
import kr.bit.animalinc.repository.shop.UserItemRepository;
import kr.bit.animalinc.repository.user.UserRepository;
import kr.bit.animalinc.util.UserBannedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final AnimalRepository animalRepository;
    private final BanListRepository banListRepository;

    @Transactional
    public Users login(String email, String password) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            //밴 여부 확인
            Optional<BanList> banInfo = banListRepository.findByUserNum(user.getUserNum());
            if (banInfo.isPresent()) {
                BanList ban = banInfo.get();
                if (ban.getUnlockDate().before(new Date())) {
                    banListRepository.delete(ban);
                    banListRepository.flush();
                } else {
                    throw new UserBannedException(ban.getBanReason(), ban.getUnlockDate());
                }
            }

            // 일반 로그인 여부 확인
            if (user.getPlatform() == null || "default".equals(user.getPlatform())) {
                // 비밀번호 일치 여부 확인
                if (passwordEncoder.matches(password, user.getUserPw())) {
                    return user;
                }
            }
        }
        return null;
    }

    @Transactional
    public Users register(Users user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));

        //USER 권한 설정
        user.addRole(MemberRole.USER);
        user.setPlatform("default"); // 플랫폼을 "default"로 설정

        Users registeredUser = userRepository.save(user);

        setDefaultAnimal(registeredUser, 1);

        return registeredUser;
    }

    public boolean checkEmailExists(String email) {
        return userRepository.findByUserEmail(email).isPresent();
    }

    public boolean checkNickname(String nickname) {
        boolean exists = userRepository.findByUserNickname(nickname).isPresent();
        return !exists;
    }


    @Transactional
    public Users socialLogin(String name, String email, String platform) {
        log.info("Attempting social login for email: {}", email);
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);

        Users user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (!user.getPlatform().equals(platform)) {
                throw new IllegalStateException("이미 해당 이메일로 " + getRegisteredMethod(email) + "에서 가입하셨습니다.");
            }

            Optional<BanList> banInfo = banListRepository.findByUserNum(user.getUserNum());
            if (banInfo.isPresent()) {
                BanList ban = banInfo.get();
                log.info("Checking ban for userNum: {}", user.getUserNum());
                if (ban.getUnlockDate().before(new Date())) {
                    log.info("Ban expired, removing ban for userNum: {}", user.getUserNum());
                    banListRepository.delete(ban);
                    banListRepository.flush();  // 추가
                } else {
                    log.info("Ban still active for userNum: {} until {}", user.getUserNum(), ban.getUnlockDate());
                    throw new UserBannedException(ban.getBanReason(), ban.getUnlockDate());
                }

            }
        } else {
            synchronized (this) {
                optionalUser = userRepository.findByUserEmail(email);
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                    if (!user.getPlatform().equals(platform)) {
                        throw new IllegalStateException("이미 해당 이메일로 " + getRegisteredMethod(email) + "에서 가입하셨습니다.");
                    }
                    log.info("User already exists with email: {}", email);
                } else {
                    user = new Users();
                    user.setUserEmail(email);
                    user.setUserRealname(name);
                    user.setSlogin(true);
                    user.setPlatform(platform); // 소셜 로그인 플랫폼 설정
                    user.setUserNickname(null);
                    user.addRole(MemberRole.USER);
                    user = userRepository.save(user);
                    log.info("New user registered with email: {}", email);
                }
            }
        }

        if (user.isSlogin()) {
            log.info("User already logged in, logging out existing session: {}", email);
            user.setSlogin(false);
        }

        user.setSlogin(true);
        userRepository.save(user);
        log.info("Social login successful for user: {}", email);
        return user;
    }

    public String getRegisteredMethod(String email) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (user.isSlogin()) {
                String userPlatform = user.getPlatform();
                if (userPlatform.equals("Naver"))
                    return "네이버";
                else if (userPlatform.equals("Kakao"))
                    return "카카오";
                else if (userPlatform.equals("Google"))
                    return "구글";
                else
                    return "다른 방법";
            } else {
                return "일반 로그인";
            }
        }
        return "알 수 없음";
    }

    @Transactional
    public Users completeProfile(String email, String birthdate, String nickname) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setUserBirthdate(LocalDate.parse(birthdate));
            user.setUserNickname(nickname);
            Users updatedUser = userRepository.save(user);

            setDefaultAnimal(updatedUser, 1);

            return updatedUser;
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    @Transactional
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // 새로운 메서드 추가
    @Transactional
    public Users findByEmail(String email) {
        return userRepository.findByUserEmail(email).orElse(null);
    }

    @Transactional
    public Users updateProfile(String email, String userRealname, String userNickname, String userBirthdate) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setUserRealname(userRealname);
            user.setUserNickname(userNickname);

            try {
                // yyyyMMdd 형식의 문자열을 LocalDate로 변환
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate parsedDate = LocalDate.parse(userBirthdate, formatter);
                user.setUserBirthdate(parsedDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid birthdate format. Please use 'yyyyMMdd' format.");
            }

            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public boolean deleteUser(String email) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return true;
        }
        return false;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public boolean changePassword(String email, String currentPassword, String newPassword) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (passwordEncoder.matches(currentPassword, user.getUserPw())) {
                user.setUserPw(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public List<UsersDTO> getRankings() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getUserGrade() != null && user.getUserPoint() >= 0)
                .sorted((u1, u2) -> {
                    int gradeComparison = compareGrade(u2.getUserGrade(), u1.getUserGrade());
                    return gradeComparison != 0 ? gradeComparison : Integer.compare(u2.getUserPoint(), u1.getUserPoint());
                })
                .map(user -> {
                    UsersDTO userDTO = new UsersDTO(
                            user.getUserNum(),
                            user.getUserEmail(),
                            user.getUserRealname(),
                            user.getUserNickname(),
                            user.isSlogin(),
                            user.getMemRoleList().stream().map(Enum::name).collect(Collectors.toList())
                    );
                    userDTO.setUserGrade(user.getUserGrade());
                    userDTO.setUserPoint(user.getUserPoint());
                    return userDTO;
                })
                .collect(Collectors.toList());

    }

    private int compareGrade(String grade1, String grade2) {
        List<String> gradeOrder = List.of("Bronze", "Silver", "Gold");
        return Integer.compare(gradeOrder.indexOf(grade1), gradeOrder.indexOf(grade2));
    }

    public boolean updateUserProfilePicture(String email, String userPicture) {
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setUserPicture(userPicture);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public void acquireItem(Long userNum, Long itemId) {
        Users user = userRepository.findById(userNum).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("User not found"));

        UserItem userItem = UserItem.builder()
                .user(user)
                .item(item)
                .build();

        userItemRepository.save(userItem);
    }

    public List<UserItem> getUserItems(Long userNum) {
        Users user = userRepository.findById(userNum).orElseThrow(() -> new RuntimeException("User not found"));
        return userItemRepository.findByUser(user);
    }

    // 새로운 메서드 추가: 사용자가 동물을 선택하는 기능
    @Transactional
    public boolean selectAnimal(String email, Long animalId) {
        // 사용자 정보를 가져오며, 소유한 동물 목록을 함께 로드
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            // 소유한 동물 목록에서 선택된 동물을 찾음
            Animal selectedAnimal = user.getOwnedAnimals().stream()
                    .filter(animal -> animal.getAnimalId() == animalId)  // '==' 연산자로 기본 자료형 비교
                    .findFirst()
                    .orElse(null);


            // 선택된 동물이 있을 경우, 사용자의 선택된 동물로 설정
            if (selectedAnimal != null) {
                user.setSelectedAnimal(selectedAnimal);
                userRepository.save(user); // 변경 사항 저장
                return true; // 선택 성공
            }
        }
        return false; // 선택 실패
    }

    // 루비 업데이트 메서드
    @Transactional
    public void updateUserRuby(String email, int rubyAmount) {
        // 이메일로 사용자 조회
        Users user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 기존 루비 수량에 결제한 루비 수량을 더한 새로운 루비 수량 계산
        int newRubyAmount = user.getUserRuby() + rubyAmount;

        // 루비 수량이 음수가 되지 않도록 체크
        if (newRubyAmount < 0) {
            throw new IllegalArgumentException("Ruby amount cannot be negative");
        }

        // 새로운 루비 수량을 사용자 엔터티에 설정
        user.setUserRuby(newRubyAmount);

        // 변경된 사용자 정보를 데이터베이스에 저장
        userRepository.save(user);
    }

    @Transactional
    public void setDefaultAnimal(Users user, int defaultAnimalId) {
        // 기본 동물을 데이터베이스에서 조회
        Animal defaultAnimal = animalRepository.findById(defaultAnimalId)
                .orElseThrow(() -> new RuntimeException("Default Animal not found with id: " + defaultAnimalId));

        // 사용자의 소유 동물 목록에 기본 동물이 없을 경우 추가
        if (user.getOwnedAnimals().stream().noneMatch(animal -> animal.getAnimalId() == defaultAnimalId)) {
            user.addOwnedAnimal(defaultAnimal);
        }

        // 사용자의 선택된 동물로 기본 동물 설정
        user.setSelectedAnimal(defaultAnimal);

        // 변경된 사용자 정보를 데이터베이스에 저장
        userRepository.save(user);
    }

    @Transactional
    public void giveRewards(Long userNum, int ruby, int point){
        Users user = userRepository.findById(userNum).orElseThrow(() -> new RuntimeException("User not found with userNum: " + userNum));
        user.setUserRuby(user.getUserRuby()+ruby);
        user.setUserPoint(user.getUserPoint()+point);
        userRepository.save(user);
    }
}



