package edu.school21.sockets.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.property")
public class SocketsApplicationConfig {

    @Autowired
    Environment environment;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(environment.getProperty("hikari.username"));
        hikariConfig.setPassword(environment.getProperty("hikari.password"));
        hikariConfig.setJdbcUrl(environment.getProperty("hikari.jdbcUrl"));
        return hikariConfig;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HikariDataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }
}
