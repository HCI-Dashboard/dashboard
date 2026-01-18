package kr.co.bnbsoft.dashboard.config.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증에는 성공 했으나 해당 자원에 접근 권한이 없는 경우 발생하는 예외를 처리하는 Handler
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.debug("=== AccessDeniedHandlerImpl ===");
        log.debug("접근 거부: {}", accessDeniedException.getMessage());

        // 응답으로 403 Forbidden 반환
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
