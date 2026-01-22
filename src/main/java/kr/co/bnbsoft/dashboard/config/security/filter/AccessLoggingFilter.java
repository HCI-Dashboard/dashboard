package kr.co.bnbsoft.dashboard.config.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.retrotv.framework.foundation.common.util.IPUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.bnbsoft.dashboard.domain.access.AccessLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLoggingFilter extends OncePerRequestFilter {
    private final AccessLogService accessLogService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            username = "Anonymous";
        } else {
            Object principal = authentication.getPrincipal();
            username = switch (principal) {
                case UserDetails user -> user.getUsername();
                case String user -> user;
                default -> "Anonymous";
            };
        }

        String uri = request.getRequestURI();
        String clientIp = IPUtils.getIPAddr(request);

        accessLogService.accessLogging(username, uri, clientIp);

        doFilter(request, response, filterChain);
    }
}
