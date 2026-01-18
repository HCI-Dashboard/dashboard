package kr.co.bnbsoft.dashboard.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
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
}
