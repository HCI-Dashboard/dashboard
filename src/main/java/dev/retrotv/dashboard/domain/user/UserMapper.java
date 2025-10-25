package dev.retrotv.dashboard.domain.user;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import dev.retrotv.dashboard.domain.authority.AuthorityDTO;
import dev.retrotv.dashboard.domain.authority.AuthorityEntity;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "newPassword", ignore = true)
    @Mapping(target = "authorities", source = "authorities")
    UserDTO toDTO(UserEntity userEntity, Set<AuthorityEntity> authorities);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "newPassword", ignore = true)
    @Mapping(target = "authorities", source = "authorities")
    UserDTO toExcludePasswordDTO(UserEntity userEntity, Set<AuthorityEntity> authorities);

    default UserEntity toEntity(UserDTO userDTO) {
        return UserEntity.builder()
                         .username(userDTO.getUsername())
                         .password(userDTO.getPassword())
                         .email(userDTO.getEmail())
                         .authorities(
                             userDTO.getAuthorities() != null ?
                                userDTO.getAuthorities()
                                        .stream()
                                        .map(AuthorityDTO::toEntity)
                                        .collect(Collectors.toSet()) : null
                         )
                         .nonExpired(userDTO.getNonExpired() == null ? true : userDTO.getNonExpired())
                         .nonLocked(userDTO.getNonLocked() == null ? true : userDTO.getNonLocked())
                         .credentialsNonExpired(userDTO.getCredentialsNonExpired() == null ? true : userDTO.getCredentialsNonExpired())
                         .enabled(userDTO.getEnabled() == null ? true : userDTO.getEnabled())
                         .build();
    }
}
