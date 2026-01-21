package kr.co.bnbsoft.dashboard.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByAuthorities(Collection<? extends GrantedAuthority> authorities);
}
