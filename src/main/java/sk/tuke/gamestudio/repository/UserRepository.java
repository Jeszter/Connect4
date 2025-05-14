package sk.tuke.gamestudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.gamestudio.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}