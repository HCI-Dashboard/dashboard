package dev.retrotv.dashboard.domain.authority;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);
    AuthorityDTO toDTO(AuthorityEntity user);

    @Mapping(target = "isNew", ignore = true)
    AuthorityEntity toEntity(AuthorityDTO userDTO);
}
