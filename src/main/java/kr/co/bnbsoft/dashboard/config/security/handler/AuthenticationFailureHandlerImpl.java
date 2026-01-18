package kr.co.bnbsoft.dashboard.config.security.handler;

import java.io.IOException;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증에 실패했을 때 발생하는 예외를 처리하는 Handler
 */
@Slf4j
@Component
@SuppressWarnings("java:S2068")
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private static final String USERNAME_OR_PASSWORD_INCORRECT = "존재하지 않는 사용자이거나 비밀번호가 다릅니다.";
    private static final String EXPIRED_ACCOUNT = "만료된 계정입니다.";
    private static final String LOCKED_ACCOUNT = "잠긴 계정입니다.";
    private static final String DISABLED_ACCOUNT = "비활성화된 계정입니다.";
    private static final String CREDENTIALS_EXPIRED = "비밀번호 유효기간이 만료되었습니다.";
    private static final String UNKNOWN_REASON = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException, ServletException {
        log.debug("=== AuthenticationFailureHandlerImpl ===");
        log.debug("인증 실패: {}", exception.getMessage());
        log.debug("예외 종류: {}", exception.getClass().getName());

        String errorMessage = null;

        // 존재하지 않는 사용자
        if (exception instanceof UsernameNotFoundException) {
            errorMessage = USERNAME_OR_PASSWORD_INCORRECT;

        // 비밀번호가 다름 (존재하지 않는 사용자와 동일한 메시지)
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = USERNAME_OR_PASSWORD_INCORRECT;

        // 만료된 계정
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = EXPIRED_ACCOUNT;

        // 잠긴 계정
        } else if (exception instanceof LockedException) {
            errorMessage = LOCKED_ACCOUNT;

        // 비활성화된 계정
        } else if (exception instanceof DisabledException) {
            errorMessage = DISABLED_ACCOUNT;

        // 비밀번호 유효기간 만료
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = CREDENTIALS_EXPIRED;

        // 그 외 인증실패
        } else {
            errorMessage = UNKNOWN_REASON;
        }

        log.debug("errorMessage: {}", errorMessage);

        request.setAttribute("errorMessage", errorMessage);
    }
}
