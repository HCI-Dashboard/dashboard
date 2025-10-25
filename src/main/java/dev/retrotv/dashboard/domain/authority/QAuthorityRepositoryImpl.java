package dev.retrotv.dashboard.domain.authority;

import java.util.List;
import java.util.Set;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static dev.retrotv.dashboard.domain.authority.QAuthorityEntity.authorityEntity;

@RequiredArgsConstructor
public class QAuthorityRepositoryImpl implements QAuthorityRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthorityEntity> selectAuthorities(String authorityCode, String authorityName) {
        return queryFactory.selectFrom(authorityEntity)
                           .where(
                               authorityCode != null ? authorityEntity.authorityCode.like(authorityCode) : null,
                               authorityName != null ? authorityEntity.authorityName.like(authorityName) : null
                           )
                           .fetch();
    }

    @Override
    public List<AuthorityEntity> selectAuthorities(Set<String> codes) {
        return queryFactory.selectFrom(authorityEntity)
                           .where(authorityEntity.authorityCode.in(codes))
                           .fetch();
    }

    @Override
    public List<AuthorityEntity> selectAuthorities(AuthorityDTO authorityDTO) {
        return queryFactory.selectFrom(QAuthorityEntity.authorityEntity)
                           .where(
                               authorityEntity.authorityCode.like(authorityDTO.getAuthorityCode()),
                               authorityEntity.authorityName.like(authorityDTO.getAuthorityName())
                           )
                           .fetch();
    }
}
