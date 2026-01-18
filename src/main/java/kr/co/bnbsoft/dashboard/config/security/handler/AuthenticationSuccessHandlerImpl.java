package kr.co.bnbsoft.dashboard.config.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증에 성공했을 때 발생하는 인증 이벤트를 처리하는 Handler
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        log.debug("=== AuthenticationSuccessHandlerImpl ===");
        log.debug("인증 성공: {}", authentication.getName());

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
