package kr.co.bnbsoft.dashboard.config.datasource;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaRepositories(
    basePackages = "kr.co.bnbsoft.dashboard.domain",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DataSourceConfig {

    // JPA에서 사용할 기본 DataSource 설정
    @Primary
    @Bean("mainDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    DataSourceProperties mainDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("mainDataSource")
    DataSource mainDataSource() {
        return mainDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Primary
    @Bean("transactionManager")
    PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // Spring Session에서 사용할 DataSource 설정
    @Bean("sessionDataSourceProperties")
    @ConfigurationProperties("session.datasource")
    DataSourceProperties sessionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("sessionDataSource")
    @SpringSessionDataSource
    DataSource sessionDataSource() {
        return sessionDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean("sessionTransactionManager")
    PlatformTransactionManager sessionTransactionManager(@Qualifier("sessionDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /*
     * JPA @CreatedBy 및 @LastModifiedBy 어노테이션을 선언한 컬럼에 사용자 정보를 제공하는 빈
     */
    @Bean
    AuditorAware<String> auditorProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return () -> Optional.of("Anonymous");
        }

        Object principal = authentication.getPrincipal();
        String userId = switch (principal) {
            case UserDetails user -> user.getUsername();
            case String user -> user;
            default -> "Anonymous";
        };

        log.debug("로그인 사용자: {}", userId);

        return () -> Optional.of(userId);
    }
}
