package io.github.bboy22dxdavid.sang_backEnd.controller;

import org.springframework.beans.factory.annotation.Value;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfiguration {
    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5433/avaliacao}")
    private String url;

    @Value("${spring.datasource.username:sang}")
    private String username;

    @Value("${spring.datasource.password:sang}")
    private String password;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driver;



    @Bean
    public DataSource hikariDataSource(){
        HikariConfig config = new HikariConfig();


        config.setJdbcUrl(this.url);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setDriverClassName(this.driver);

        // Seus parâmetros customizados do pool permanecem aqui:
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setPoolName("avaliacao-db-pool");
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(100000);
        config.setConnectionTestQuery("select 1");

        return new HikariDataSource(config);
    }
}
