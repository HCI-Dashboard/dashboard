package kr.co.bnbsoft.dashboard.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import dev.retrotv.framework.foundation.common.filter.RequestLoggingFilter;
import jakarta.servlet.DispatcherType;
import kr.co.bnbsoft.dashboard.config.security.filter.AccessLoggingFilter;
import kr.co.bnbsoft.dashboard.config.security.filter.WrappingFilter;

@Configuration
public class FilterRegistrationConfig {

    // RequestWrappingFilter를 가장 먼저 등록하여 모든 요청을 래핑
    @Bean
    FilterRegistrationBean<WrappingFilter> firstFilterRegistration(WrappingFilter filter) {
        FilterRegistrationBean<WrappingFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        reg.addUrlPatterns("/*");
        reg.setDispatcherTypes(DispatcherType.REQUEST);

        return reg;
    }

    // AccessLoggingFilter를 두 번째로 등록하여 모든 요청 정보를 로깅
    @Bean
    FilterRegistrationBean<AccessLoggingFilter> secondFilterRegistrationBean(AccessLoggingFilter filter) {
        FilterRegistrationBean<AccessLoggingFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        reg.addUrlPatterns("/*");
        reg.setDispatcherTypes(DispatcherType.REQUEST);

        return reg;
    }

    // RequestLoggingFilter를 세 번째로 등록하여 요청 및 응답 정보를 로깅
    @Bean
    FilterRegistrationBean<RequestLoggingFilter> thirdFilterRegistrationBean() {
        FilterRegistrationBean<RequestLoggingFilter> reg = new FilterRegistrationBean<>(new RequestLoggingFilter());
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        reg.addUrlPatterns("/*");
        reg.setDispatcherTypes(DispatcherType.REQUEST);

        return reg;
    }
}
