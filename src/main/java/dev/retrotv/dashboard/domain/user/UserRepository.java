package dev.retrotv.dashboard.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, QUserRepository {
    Optional<UserEntity> findByUsername(String username);
    void deleteByUsername(String username);
}
