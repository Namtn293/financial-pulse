package com.vcc.bigdata.stock.infrastructure.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
/**
 * Configuration class to enable ShedLock distributed scheduling locks and define the LockProvider
 * using Spring's JDBC template.
 */

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource){
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .usingDbTime()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .build()
        );
    }
}
