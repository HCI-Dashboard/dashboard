package dev.retrotv.dashboard.domain.authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String>, QAuthorityRepository {

}
