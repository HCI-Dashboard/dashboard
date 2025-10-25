package dev.retrotv.dashboard.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import jakarta.servlet.DispatcherType;
import dev.retrotv.framework.foundation.common.filter.RequestLoggingFilter;
import dev.retrotv.dashboard.config.security.filter.GetUserInfoFilter;
import dev.retrotv.dashboard.config.security.handler.AccessDeniedHandlerImpl;
import dev.retrotv.dashboard.config.security.handler.AuthenticationEntryPointImpl;
import dev.retrotv.dashboard.config.security.handler.AuthenticationFailureHandlerImpl;
import dev.retrotv.dashboard.config.security.handler.AuthenticationSuccessHandlerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true) // Spring Security 디버그 모드 활성화
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

    @Value("${spring.security.disable:false}")
    private boolean securityDisable;

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final GetUserInfoFilter getUserInfoFilter;
    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AuthenticationFailureHandlerImpl authenticationFailHandler;

    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String LOGIN_SUBMIT_URL = "/api/v1/login-submit";
    private static final String LOGOUT_SUBMIT_URL = "/api/v1/logout-submit";
    private static final String SIGNUP_URL = "/api/v1/users/signup";

    private static final String[] PERMIT_URL_ARRAY = {
          LOGIN_SUBMIT_URL
        , LOGOUT_SUBMIT_URL
        , SIGNUP_URL
        , "/static/**"
    };

    @Bean
    @SuppressWarnings("java:S6437") // java:S6437: PASSWORD_PARAMETER 경고 제거
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            // CSRF 비활성화
            .csrf(CsrfConfigurer::disable)

            // HTTP Basic 인증 비활성화
            .httpBasic(HttpBasicConfigurer::disable)

            // X-Frame-Options 설정
            .headers(headerConfig ->
                headerConfig.frameOptions(FrameOptionsConfig::deny)
            )

            // 허용 URL 설정
            .authorizeHttpRequests(auth -> {

                // security.disable=true 설정 시, 모든 URL 접근 허용
                if (securityDisable) {
                    auth.anyRequest().permitAll();
                    return;
                }

                auth

                    /*
                     * Spring Security 6 부터는 URL 접근 전 단계에서도 인증을 수행함,
                     * dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()는 해당 단계를 검증과 관계없이 허용
                     */
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()

                    // PERMIT_URL_ARRAY에 포함된 URL은 인증 없이 접근 가능
                    .requestMatchers(PERMIT_URL_ARRAY).permitAll()

                    // 그 외 URL은 인증 필요
                    .anyRequest().authenticated();
            })

            // 세션 설정
            .sessionManagement(session ->
                session

                       // 최대 세션 수
                       .maximumSessions(1)

                       // 중복 로그인 방지 (true: 예외 발생, false: 기존 세션 만료)
                       .maxSessionsPreventsLogin(false)
            )

            // 로그인 설정
            .formLogin(login ->
                login

                     // 로그인 진행 URL
                     .loginProcessingUrl(LOGIN_SUBMIT_URL)

                     // Username 파라미터 명
                     .usernameParameter(USERNAME_PARAMETER)

                     // Password 파라미터 명
                     .passwordParameter(PASSWORD_PARAMETER)

                     // 로그인 실패 핸들러 설정
                     .failureHandler(authenticationFailHandler)

                     // 로그인 성공 핸들러 설정
                     .successHandler(authenticationSuccessHandler)
            )

            // 로그아웃 설정
            .logout(logout ->
                logout

                      // 로그아웃 진행 URL
                      .logoutUrl(LOGOUT_SUBMIT_URL)

                      // 로그아웃 핸들러 설정
                      .addLogoutHandler(new SecurityContextLogoutHandler())

                      // 삭제할 세션 쿠키 명
                      .deleteCookies("SESSIONID")

                      // 세션 무효화
                      .invalidateHttpSession(true)

                      // 인증 정보 삭제
                      .clearAuthentication(true)
            )

            // 접근 예외 처리 설정
            .exceptionHandling(exception ->
                exception

                         // 접근 거부 핸들러 설정
                         .accessDeniedHandler(accessDeniedHandler)

                         // 인증 진입점 설정
                         .authenticationEntryPoint(authenticationEntryPoint)
            )

            // 사용할 AuthenticationProvider
            .authenticationProvider(daoAuthenticationProvider)

            // 필터 설정
            .addFilterBefore(requestLoggingFilter, DisableEncodeUrlFilter.class)
            .addFilterBefore(getUserInfoFilter, UsernamePasswordAuthenticationFilter.class)
            ;

        return http.build();
    }
}
