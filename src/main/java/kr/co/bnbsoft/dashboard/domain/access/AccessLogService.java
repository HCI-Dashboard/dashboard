package kr.co.bnbsoft.dashboard.domain.access;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessLogService {
    private final AccessLogRepository accessLogRepository;

    @Transactional
    public void accessLogging(String username, String ip, String uri, int httpStatus) {
        accessLogRepository.save(
            new AccessLogEntity(
                null,
                username,
                ip,
                uri,
                httpStatus,
                null
            )
        );
    }
}
