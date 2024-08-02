package kr.bit.animalinc.repository.user;

import kr.bit.animalinc.entity.user.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserEmail(String userEmail);

    Optional<Users> findByUserNickname(String userName);

    @EntityGraph(attributePaths = {"memRoleList"})
    @Query("select user from Users user where user.userEmail = :userEmail")
    Users getRole(@Param("userEmail") String userEmail);
}
