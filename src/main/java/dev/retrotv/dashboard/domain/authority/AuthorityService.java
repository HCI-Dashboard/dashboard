package dev.retrotv.dashboard.domain.authority;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.retrotv.framework.foundation.common.AbstractService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService extends AbstractService {
    private final AuthorityRepository authorityRepository;

    @Transactional(readOnly = true)
    public List<AuthorityDTO> getAuthorities(String authorityCode, String authorityName) {
        return authorityRepository.selectAuthorities(authorityCode, authorityName)
                                  .stream()
                                  .map(AuthorityEntity::toDTO)
                                  .toList();
    }

    @Transactional
    public void saveAuthorities(List<AuthorityDTO> authorityDTOs) {
        authorityRepository.saveAll(
            authorityDTOs.stream()
                         .map(AuthorityDTO::toEntity)
                         .toList()
        );
    }

    @Transactional
    public void updateAuthorities(List<AuthorityDTO> authorityDTOs) {
        List<AuthorityEntity> authorities = authorityDTOs.stream()
                                                         .map(AuthorityDTO::toEntity)
                                                         .toList();
        authorities.forEach(AuthorityEntity::updateMode);
        
        authorityRepository.saveAll(authorities);
    }

    @Transactional
    public void deleteAuthorities(Set<String> codes) {
        List<AuthorityEntity> authorities = authorityRepository.selectAuthorities(codes);
        int count = authorities.size();
        log.debug("삭제할 권한 개수: {}", count);

        authorityRepository.deleteAll(authorities);
    }
}
