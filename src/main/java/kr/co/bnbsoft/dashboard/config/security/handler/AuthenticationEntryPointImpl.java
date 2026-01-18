package kr.co.bnbsoft.dashboard.config.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증되지 않은 사용자가 보호된 엔트리 포인트에 액세스하려고 할 때 발생하는 예외를 처리하는 Handler
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        log.debug("=== AuthenticationEntryPointImpl ===");
        log.debug("인증되지 않은 사용자: {}", authException.getMessage());

        // 응답으로 401 Unauthorized 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
