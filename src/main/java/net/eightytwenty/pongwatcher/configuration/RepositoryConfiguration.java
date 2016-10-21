package net.eightytwenty.pongwatcher.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "net.eightytwenty.pongwatcher.data.model")
@EnableJpaRepositories(basePackages = {"net.eightytwenty.pongwatcher.data"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}