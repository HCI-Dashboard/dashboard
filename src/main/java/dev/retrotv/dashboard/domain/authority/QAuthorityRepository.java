package dev.retrotv.dashboard.domain.authority;

import java.util.List;
import java.util.Set;

public interface QAuthorityRepository {
    List<AuthorityEntity> selectAuthorities(Set<String> codes);
    List<AuthorityEntity> selectAuthorities(AuthorityDTO authorityDTO);
    List<AuthorityEntity> selectAuthorities(String authorityCode, String authorityName);
}
