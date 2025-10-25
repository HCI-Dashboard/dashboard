package dev.retrotv.dashboard.domain.user;

import java.util.List;
import java.util.Set;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import dev.retrotv.dashboard.domain.authority.AuthorityDTO;
import lombok.RequiredArgsConstructor;

import static dev.retrotv.dashboard.domain.user.QUserEntity.userEntity;
import static dev.retrotv.dashboard.domain.authority.QAuthorityEntity.authorityEntity;

@RequiredArgsConstructor
public class QUserRepositoryImpl implements QUserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserEntity> selectUsers(UserDTO userDTO) {
        return queryFactory.select(userEntity)
                           .from(userEntity)
                           .leftJoin(userEntity.authorities, authorityEntity)
                           .on(authorityEntity.in(userEntity.authorities))
                           .where(
                                  includeUsername(userDTO.getUsername())
                                , includeEmail(userDTO.getEmail())
                                , hasAuthority(userDTO.getAuthorities())
                                , isExpired(userDTO.getNonExpired())
                                , isLocked(userDTO.getNonLocked())
                                , isCredentialsExpired(userDTO.getCredentialsNonExpired())
                                , isEnabled(userDTO.getEnabled())
                           )
                           .fetch();
    }

    private BooleanExpression includeUsername(String username) {
        return username != null ? userEntity.username.contains(username) : null;
    }

    private BooleanExpression includeEmail(String email) {
        return email != null ? userEntity.email.contains(email) : null;
    }

    private BooleanExpression hasAuthority(Set<AuthorityDTO> authorities) {
        return authorities != null ? authorityEntity.authorityCode.in(authorities.stream()
                                                                                 .map(AuthorityDTO::getAuthorityCode)
                                                                                 .toList()) : null;
    }

    private BooleanExpression isExpired(Boolean nonExpired) {
        return nonExpired != null ? userEntity.nonExpired.eq(nonExpired) : null;
    }

    private BooleanExpression isLocked(Boolean nonLocked) {
        return nonLocked != null ? userEntity.nonLocked.eq(nonLocked) : null;
    }

    private BooleanExpression isCredentialsExpired(Boolean credentialsNonExpired) {
        return credentialsNonExpired != null ? userEntity.credentialsNonExpired.eq(credentialsNonExpired) : null;
    }

    private BooleanExpression isEnabled(Boolean enabled) {
        return enabled != null ? userEntity.enabled.eq(enabled) : null;
    }
}
