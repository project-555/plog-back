package com.plogcareers.backend.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.plogcareers.backend.*.repository.postgres")
@EnableRedisRepositories(basePackages = "com.plogcareers.backend.*.repository.redis")
public class RepositoryConfig {
}
