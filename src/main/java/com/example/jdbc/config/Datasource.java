package com.example.jdbc.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Datasource {
//    @Bean
//    public Datasource datasource(){
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//        driverManagerDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        driverManagerDataSource.setUrl("jdbc:sqlserver://localhost;database=op_pusat");
//        driverManagerDataSource.setUsername("Demo");
//        driverManagerDataSource.setPassword("Uat46");
//        return driverManagerDataSource;
//    }

    @Bean
    @ConfigurationProperties("spring.datasource.oppusat")
    public DataSourceProperties opPusatProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.testdb")
    public DataSourceProperties devProperties(){
        return new DataSourceProperties();
    }


    @Bean
    public DataSource devDataSource() {
        return devProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public DataSource opPusatDataSource() {
        return opPusatProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("opPusatDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    @Bean
    public JdbcTemplate testDbJdbcTemplate(@Qualifier("devDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
