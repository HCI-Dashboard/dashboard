package dev.retrotv.dashboard.config.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {
    private final EntityManager entityManager;

    /*
     * JPAQueryFactory 빈 등록
     */
    @Bean
    JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
