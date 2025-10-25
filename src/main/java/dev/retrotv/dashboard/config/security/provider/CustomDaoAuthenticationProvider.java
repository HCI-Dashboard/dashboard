package dev.retrotv.dashboard.config.security.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.retrotv.dashboard.domain.user.UserService;
import lombok.RequiredArgsConstructor;

/**
 * UsernameNotFoundException/BadCredentialsException 예외를 분리하기 위한 Provider
 */
@Configuration
@RequiredArgsConstructor
public class CustomDaoAuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);

        /*
         * UsernameNotFoundException 발생 시, BadCredentialsException 발생 여부
         * true: UsernameNotFoundException 발생 시, BadCredentialsException으로 전환
         * false: UsernameNotFoundException 발생 시, BadCredentialsException으로 전환하지 않음
         */
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
