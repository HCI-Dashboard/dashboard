package kr.co.bnbsoft.dashboard.domain.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.retrotv.framework.foundation.common.AbstractService;
import dev.retrotv.framework.foundation.common.exception.http.BadRequestException;
import dev.retrotv.framework.persistence.jpa.embedded.Use;
import kr.co.bnbsoft.dashboard.domain.authority.AuthorityEntity;
import kr.co.bnbsoft.dashboard.domain.authority.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends AbstractService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 사용자입니다.";

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 정보 조회
     * @param username
     * @return UserDetails
     * @exception UsernameNotFoundException 사용자가 존재하지 않는 경우 던져짐
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("username: {}", username);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    /**
     * 관리자 계정 초기화
     * @param initUserDTO
     * @exception BadRequestException 이미 관리자 계정이 존재하는 경우 던져짐
     */
    @Transactional
    public void initManagerUser(InitUserDTO initUserDTO) {

        // ROLE_ADMIN 권한이 이미 존재하는지 확인
        Optional<AuthorityEntity> findAdminRole = authorityRepository.findById("ROLE_ADMIN");
        findAdminRole.ifPresent(authority -> {
            throw new BadRequestException("어드민 계정이 이미 존재합니다.");
        });

        // 관리자 권한 생성
        AuthorityEntity adminAuthority = authorityRepository.saveAndFlush(
            AuthorityEntity.builder()
                .authorityCode("ROLE_ADMIN")
                .authorityName("관리자")
                .description("관리자용 계정에 부여하는 권한입니다.")
                .use(
                    Use.builder()
                        .yn(true)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.of(9999, 12, 31))
                        .build()
                )
                .build()
        );

        // 관리자 계정 생성
        userRepository.saveAndFlush(
            UserEntity.builder()
                .username(initUserDTO.getUsername())
                .password(passwordEncoder.encode(initUserDTO.getPassword()))
                .email(null)
                .authorities(Set.of(adminAuthority))
                .build()
        );
    }
}
