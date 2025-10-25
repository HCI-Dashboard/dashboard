package dev.retrotv.dashboard.domain.user;

import java.util.List;

public interface QUserRepository {
    List<UserEntity> selectUsers(UserDTO userDTO);
}
