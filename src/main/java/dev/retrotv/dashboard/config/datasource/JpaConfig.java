package dev.retrotv.dashboard.config.datasource;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaAuditing
public class JpaConfig {

    /*
     * JPA @CreatedBy 및 @LastModifiedBy 어노테이션을 선언한 컬럼에 사용자 정보를 제공하는 빈
     */
    @Bean
    AuditorAware<String> auditorProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = null != authentication && authentication.isAuthenticated()
            ? (UserDetails) authentication.getPrincipal()
            : null;

        // 로그인한 사용자 정보가 없으면 Anonymous를 반환
        String userId = Optional.ofNullable(user)
                                .map(UserDetails::getUsername)
                                .orElse("Anonymous");

        log.debug("로그인 사용자: {}", userId);

        return () -> Optional.of(userId);
    }
}
