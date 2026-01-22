package kr.co.bnbsoft.dashboard.config.security;

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

import jakarta.servlet.DispatcherType;
import kr.co.bnbsoft.dashboard.config.security.filter.GetUserInfoFilter;
import kr.co.bnbsoft.dashboard.config.security.handler.AccessDeniedHandlerImpl;
import kr.co.bnbsoft.dashboard.config.security.handler.AuthenticationEntryPointImpl;
import kr.co.bnbsoft.dashboard.config.security.handler.AuthenticationFailureHandlerImpl;
import kr.co.bnbsoft.dashboard.config.security.handler.AuthenticationSuccessHandlerImpl;
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

    private final GetUserInfoFilter getUserInfoFilter;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AuthenticationFailureHandlerImpl authenticationFailHandler;

    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String INIT_ADMIN_URL = "/api/v1/users/init-admin";
    private static final String LOGIN_SUBMIT_URL = "/api/v1/login-submit";
    private static final String LOGOUT_SUBMIT_URL = "/api/v1/logout-submit";
    private static final String SIGNUP_URL = "/api/v1/users/signup";
    private static final String STATIC_FILE_URL = "/static/**";

    private static final String[] PERMIT_URL_ARRAY = {
          INIT_ADMIN_URL
        , LOGIN_SUBMIT_URL
        , LOGOUT_SUBMIT_URL
        , SIGNUP_URL
        , STATIC_FILE_URL
    };

    // SecurityFilterChain 빈 등록
    @Bean
    @SuppressWarnings("java:S6437") // java:S6437: PASSWORD_PARAMETER 경고 제거
    SecurityFilterChain filterChain(HttpSecurity http) {
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
                    log.debug("스프링 시큐리티를 비활성화 합니다.");
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

            // 익명 사용자 설정
            .anonymous(a ->
                a

                    // 익명 사용자 principal 설정
                    .principal("Anonymous")

                    // 익명 사용자 권한 설정
                    .authorities("ROLE_GUEST")
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
                    .deleteCookies("SESSION")

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

            /*
             * username 및 password 파라미터가 잘못 된 경우, UsernamePasswordAuthenticationFilter 이후 전파가 안되므로
             * 로깅을 위해 GetUserInfoFilter가 UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정 함
             */
            .addFilterBefore(getUserInfoFilter, UsernamePasswordAuthenticationFilter.class)
            ;

        return http.build();
    }
}
