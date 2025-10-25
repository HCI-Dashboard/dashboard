package dev.retrotv.dashboard.domain.user;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.retrotv.framework.foundation.common.AbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends AbstractService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 사용자입니다.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> findUsers(UserDTO userDTO) {
        return userRepository.selectUsers(userDTO)
                             .stream()
                             .map(UserEntity::toDTO)
                             .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUser(username);
    }

    @Transactional
    public void createUser(UserDTO userDTO) {
        userRepository.findByUsername(userDTO.getUsername())
                      .ifPresent(user -> {
                          throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
                      });

        UserEntity user = userDTO.toEntity();
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UserDTO userDTO) {
        UserEntity user = findUser(userDTO.getUsername());
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(UserDTO userDTO) {
        String username = userDTO.getUsername();
        UserEntity user = findUser(username);

        String oldPassword = userDTO.getPassword();
        String newPassword = userDTO.getNewPassword();

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.updatePassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void changeNewPassword(String username, String newPassword) {
        UserEntity user = findUser(username);
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void forgotPassword(String username) {
        UserEntity user = findUser(username);
        String email = user.getEmail();
        log.debug("{} 유저가 비밀번호 변경을 요청했습니다.", email);
    }

    private UserEntity findUser(String username) {
        log.debug("username: {}", username);
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
    }
}
